import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ApiService } from '../services/api';
import { AuthService } from '../services/auth';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-booking',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './booking.html',
  styleUrl: './booking.css'
})
export class BookingComponent implements OnInit {
  stations: any[] = [];
  trains: any[] = []; // Ide töltjük be a vonatokat a vásárláshoz
  rawResults: any[] = [];
  groupedResults: any[] = []; // Ide kerülnek az összevont kártyák
  username: string | null = '';

  searchData = { from: null, to: null, after: '' };
  passengers: number = 1; // Utasok száma

  constructor(private api: ApiService, private auth: AuthService, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.username = this.auth.getUsername();
    this.api.getStations().subscribe((data: any) => {
      this.stations = data;
      this.cdr.detectChanges();
    });
    // Lekérjük a vonatokat is, hogy tudjuk az ID-jukat a vásárláshoz
    this.api.getTrains().subscribe((data: any) => {
      this.trains = data;
    });
  }

  search() {
    if(!this.searchData.from || !this.searchData.to || !this.searchData.after) {
      alert("Please fill in all search fields.");
      return;
    }

    this.api.findRoute(this.searchData.from, this.searchData.to, this.searchData.after).subscribe({
      next: (data: any) => {
        this.rawResults = data;
        this.groupResults(); // Itt hívjuk meg az összevonást!
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error(err);
        alert("No routes found for the selected criteria.");
        this.groupedResults = [];
      }
    });
  }

  // Ez a függvény vonja össze az azonos nevű vonatokat egy kártyává
  groupResults() {
    this.groupedResults = [];
    if (this.rawResults.length === 0) return;

    let currentTrain = "";
    let currentGroup: any = null;

    this.rawResults.forEach((seg: any, index: number) => {
      if (seg.trainName !== currentTrain) {
        if (currentGroup) {
          currentGroup.endStation = this.rawResults[index - 1].toStation;
          currentGroup.endTime = this.rawResults[index - 1].arrival;
          this.groupedResults.push(currentGroup);
        }
        currentTrain = seg.trainName;
        currentGroup = {
          trainName: currentTrain,
          startStation: seg.fromStation,
          startTime: seg.departure
        };
      }
      if (index === this.rawResults.length - 1) {
        currentGroup.endStation = seg.toStation;
        currentGroup.endTime = seg.arrival;
        this.groupedResults.push(currentGroup);
      }
    });
  }

  // Jegyvásárlás indítása
  bookTicket(trainName: string, startStationName: string, endStationName: string) {
    const train = this.trains.find(t => t.name === trainName);
    const depStation = this.stations.find(s => s.name === startStationName);
    const arrStation = this.stations.find(s => s.name === endStationName);

    if(!train || !depStation || !arrStation) {
      alert("Error mapping data for booking.");
      return;
    }

    const request = {
      trainId: train.id,
      departureStationId: depStation.id,
      arrivalStationId: arrStation.id,
      customerEmail: this.auth.getEmail() || 'customer@train.com',
      numberOfSeats: this.passengers
    };

    this.api.bookTicket(request).subscribe({
      next: () => alert("Booking confirmed! Email sent to customer@train.com"),
      error: (err: any) => alert("Booking Failed: " + (err.error?.error || "Unknown error"))
    });
  }

  logout() {
    this.auth.logout();
  }
}
