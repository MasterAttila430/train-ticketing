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
  trains: any[] = [];
  rawResults: any[] = [];
  groupedResults: any[] = [];
  username: string | null = '';
  bookingMessage = '';

  searchData = { from: null, to: null, after: '' };
  passengers: number = 1;

  constructor(private api: ApiService, private auth: AuthService, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.username = this.auth.getUsername();
    this.api.getStations().subscribe((data: any) => {
      this.stations = data;
      this.cdr.detectChanges();
    });
    this.api.getTrains().subscribe((data: any) => {
      this.trains = data;
    });
  }

  search() {
    if (!this.searchData.from || !this.searchData.to || !this.searchData.after) {
      alert('Please fill in all search fields.');
      return;
    }
    this.api.findRoute(this.searchData.from, this.searchData.to, this.searchData.after).subscribe({
      next: (data: any) => {
        this.rawResults = data;
        this.groupResults();
        this.cdr.detectChanges();
      },
      error: () => {
        alert('No routes found for the selected criteria.');
        this.groupedResults = [];
      }
    });
  }

  groupResults() {
    this.groupedResults = [];
    if (this.rawResults.length === 0) return;
    let currentTrain = '';
    let currentGroup: any = null;
    this.rawResults.forEach((seg: any, index: number) => {
      if (seg.trainName !== currentTrain) {
        if (currentGroup) {
          currentGroup.endStation = this.rawResults[index - 1].toStation;
          currentGroup.endTime = this.rawResults[index - 1].arrival;
          this.groupedResults.push(currentGroup);
        }
        currentTrain = seg.trainName;
        currentGroup = { trainName: currentTrain, startStation: seg.fromStation, startTime: seg.departure };
      }
      if (index === this.rawResults.length - 1) {
        currentGroup.endStation = seg.toStation;
        currentGroup.endTime = seg.arrival;
        this.groupedResults.push(currentGroup);
      }
    });
  }

  bookTicket(trainName: string, startStationName: string, endStationName: string) {
    const train = this.trains.find(t => t.name === trainName);
    const depStation = this.stations.find(s => s.name === startStationName);
    const arrStation = this.stations.find(s => s.name === endStationName);

    if (!train || !depStation || !arrStation) {
      this.bookingMessage = ' Error mapping data for booking.';
      setTimeout(() => this.bookingMessage = '', 4000);
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
      next: () => {
        this.bookingMessage = ` Booking confirmed for ${trainName}! Email sent.`;
        setTimeout(() => this.bookingMessage = '', 4000);
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        this.bookingMessage = ` Booking Failed: ${err.error?.error || 'Unknown error'}`;
        setTimeout(() => this.bookingMessage = '', 4000);
        this.cdr.detectChanges();
      }
    });
  }

  logout() {
    this.auth.logout();
  }
}
