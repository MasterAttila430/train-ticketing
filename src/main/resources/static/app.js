let allStations = [];

document.addEventListener("DOMContentLoaded", () => {
    loadStations();
    document.getElementById("roleSwitch").addEventListener("change", (e) => {
        if (e.target.value === "admin") {
            document.getElementById("customerPanel").classList.add("hidden");
            document.getElementById("adminPanel").classList.remove("hidden");
            loadAdminData();
        } else {
            document.getElementById("customerPanel").classList.remove("hidden");
            document.getElementById("adminPanel").classList.add("hidden");
        }
    });
});

async function loadStations() {
    try {
        const response = await fetch('/api/stations');
        allStations = await response.json();
        let optionsHtml = '<option value="">Select a station...</option>';
        allStations.forEach(station => optionsHtml += `<option value="${station.id}">${station.name}</option>`);
        document.getElementById("fromStation").innerHTML = optionsHtml;
        document.getElementById("toStation").innerHTML = optionsHtml;
    } catch (error) { console.error("Error loading stations", error); }
}

async function searchRoute() {
    const fromId = document.getElementById("fromStation").value;
    const toId = document.getElementById("toStation").value;
    const time = document.getElementById("departureTime").value;
    // ÚJ: Kiolvassuk hány jegyet akar venni
    const seats = document.getElementById("numSeats").value || 1;
    const resultsDiv = document.getElementById("searchResults");

    resultsDiv.classList.remove("hidden");
    if (!fromId || !toId || !time) {
        resultsDiv.innerHTML = "Please select all fields.";
        return;
    }

    resultsDiv.innerHTML = "Processing search...";
    try {
        const response = await fetch(`/api/routes/find?from=${fromId}&to=${toId}&after=${time}`);
        if (!response.ok) {
            resultsDiv.innerHTML = "<b style='color:red;'>No routes found!</b> Try a different time or route.";
            return;
        }

        const segments = await response.json();
        let currentTrain = "";
        let trainGroups = [];

        segments.forEach((seg, index) => {
            if (seg.trainName !== currentTrain) {
                if (currentTrain !== "") {
                    trainGroups[trainGroups.length - 1].end = segments[index - 1].toStation;
                    trainGroups[trainGroups.length - 1].endTime = segments[index - 1].arrival;
                }
                currentTrain = seg.trainName;
                trainGroups.push({
                    trainName: currentTrain,
                    start: seg.fromStation,
                    startTime: seg.departure
                });
            }
            if (index === segments.length - 1) {
                trainGroups[trainGroups.length - 1].end = seg.toStation;
                trainGroups[trainGroups.length - 1].endTime = seg.arrival;
            }
        });

        let html = "<h4>Search Results:</h4>";
        trainGroups.forEach(group => {
            const depTime = new Date(group.startTime).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
            const arrTime = new Date(group.endTime).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});

            html += `
                <div class="itinerary-card">
                    <h5 class="itinerary-header">Train: ${group.trainName}</h5>
                    <div class="time-box">
                        <div class="time-item">
                            <small>DEPARTURE</small><br>
                            <b>${depTime}</b><br>
                            <small>${group.start}</small>
                        </div>
                        <div class="time-item" style="display:flex; align-items:center; justify-content:center;">
                            <span>-></span>
                        </div>
                        <div class="time-item">
                            <small>ARRIVAL</small><br>
                            <b>${arrTime}</b><br>
                            <small>${group.end}</small>
                        </div>
                    </div>
                    <div style="text-align: right; margin-top: 10px;">
                        <button onclick="bookTrainByName('${group.trainName}', '${group.start}', '${group.end}', ${seats})" style="background: #28a745; color:white; border:none;">Book ${seats} Ticket(s)</button>
                    </div>
                </div>`;
        });
        resultsDiv.innerHTML = html;
    } catch (error) { resultsDiv.innerHTML = "Error connecting to server."; }
}

// ÚJ: A függvény most már fogadja a 'seats' paramétert
async function bookTrainByName(trainName, fromName, toName, seats) {
    try {
        const fromStation = allStations.find(s => s.name === fromName);
        const toStation = allStations.find(s => s.name === toName);
        const trainsRes = await fetch('/api/trains');
        const trains = await trainsRes.json();
        const train = trains.find(t => t.name === trainName);

        const requestBody = {
            trainId: train.id,
            departureStationId: fromStation.id,
            arrivalStationId: toStation.id,
            customerEmail: "customer@train.com",
            numberOfSeats: parseInt(seats) // Backend felé küldött valós szám
        };

        const res = await fetch('/api/bookings', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });

        if (res.ok) alert("Booking confirmed! Email sent to customer@train.com");
        else {
            const err = await res.json();
            alert("Booking Failed: " + err.error); // Hibauzenet kiíratása pirossal
        }
    } catch (e) { alert("Server error during booking."); }
}

async function loadAdminData() {
    const trainsRes = await fetch('/api/trains');
    const trains = await trainsRes.json();
    let tHtml = "<table><tr><th>ID</th><th>Name</th><th>Cap</th><th>Route ID</th><th>Action</th></tr>";
    trains.forEach(t => tHtml += `<tr><td>${t.id}</td><td>${t.name}</td><td>${t.capacity}</td><td>${t.route ? t.route.id : '-'}</td><td><button onclick="deleteTrain(${t.id})">Del</button></td></tr>`);
    document.getElementById("trainList").innerHTML = tHtml + "</table>";

    const routesRes = await fetch('/api/routes');
    const routes = await routesRes.json();
    let rHtml = "<table><tr><th>ID</th><th>Route Name</th><th>Stations</th><th>Action</th></tr>";
    routes.forEach(r => {
        const sNames = r.stations.map(s => s.name).join(' -> ');
        rHtml += `<tr><td>${r.id}</td><td>${r.name}</td><td>${sNames}</td><td><button onclick="deleteRoute(${r.id})">Del</button></td></tr>`;
    });
    document.getElementById("routeList").innerHTML = rHtml + "</table>";
}

async function delayTrain() {
    const id = document.getElementById("delayTrainId").value;
    if(!id) return alert("Enter Train ID");
    const res = await fetch(`/api/trains/${id}/delay`, { method: 'POST' });
    if (res.ok) alert("Delay processed. Notification emails sent!");
    else alert("Train ID not found.");
}

async function viewBookings() {
    const id = document.getElementById("bookingTrainId").value;
    if(!id) return alert("Enter Train ID");
    const res = await fetch(`/api/bookings/train/${id}`);
    if (res.ok) {
        const bookings = await res.json();
        let html = `<b>Bookings for Train #${id}:</b><ul>`;
        bookings.forEach(b => html += `<li>Ticket #${b.id} | Email: ${b.customerEmail} | Seats: ${b.numberOfSeats}</li>`);
        document.getElementById("bookingResults").innerHTML = html + "</ul>";
    } else document.getElementById("bookingResults").innerHTML = "No bookings found.";
}

// TRAIN CRUD
async function addTrain() {
    const name = document.getElementById("newTrainName").value;
    const capacity = document.getElementById("newTrainCapacity").value;
    const routeId = document.getElementById("newTrainRouteId").value;
    const res = await fetch('/api/trains', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: name, capacity: capacity, routeId: routeId })
    });
    if (res.ok) { alert("Train added successfully."); loadAdminData(); }
    else alert("Failed to add train.");
}

async function deleteTrain(id) {
    const res = await fetch(`/api/trains/${id}`, { method: 'DELETE' });
    if (res.ok) loadAdminData();
    else alert("Failed to delete train.");
}

// ROUTE CRUD
async function addRoute() {
    const name = document.getElementById("newRouteName").value;
    const res = await fetch('/api/routes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: name, stations: [] })
    });
    if (res.ok) { alert("Route added successfully."); loadAdminData(); }
    else alert("Failed to add route.");
}

async function deleteRoute(id) {
    const res = await fetch(`/api/routes/${id}`, { method: 'DELETE' });
    if (res.ok) loadAdminData();
    else alert("Failed to delete route.");
}

async function modifyRouteStation(action) {
    const routeId = document.getElementById("modRouteId").value;
    const stationId = document.getElementById("modStationId").value;
    const method = action === 'add' ? 'POST' : 'DELETE';

    const res = await fetch(`/api/routes/${routeId}/stations/${stationId}`, { method: method });
    if (res.ok) { alert("Route updated successfully."); loadAdminData(); }
    else alert("Failed to update route.");
}