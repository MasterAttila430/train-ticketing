import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ApiService } from '../services/api';
import { AuthService } from '../services/auth';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-management',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './management.html',
  styleUrl: './management.css'
})
export class ManagementComponent implements OnInit {
  delayTrainId: number | null = null;
  newTrain = { name: '', capacity: null, routeId: null };

  bookingTrainId: number | null = null;
  bookings: any[] = [];
  trains: any[] = [];
  routes: any[] = [];

  newRouteName: string = '';
  modRouteId: number | null = null;
  modStationId: number | null = null;

  constructor(private api: ApiService, private auth: AuthService, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.loadAdminData();
  }

  loadAdminData() {
    this.api.getTrains().subscribe((data: any) => {
      this.trains = data;
      this.cdr.detectChanges();
    });
    this.api.getRoutes().subscribe((data: any) => {
      this.routes = data;
      this.cdr.detectChanges();
    });
  }

  notifyDelay() {
    if (!this.delayTrainId) return;
    this.api.delayTrain(this.delayTrainId).subscribe({
      next: () => {
        alert("Delay registered! Notification emails are sending in the background.");
        this.delayTrainId = null;
        this.cdr.detectChanges();
      },
      error: () => alert("Error: Could not register delay.")
    });
  }

  createTrain() {
    if (!this.newTrain.name || !this.newTrain.capacity || !this.newTrain.routeId) {
      alert("Please fill in all train details.");
      return;
    }
    this.api.addTrain(this.newTrain).subscribe({
      next: () => {
        alert("New train successfully created!");
        this.newTrain = { name: '', capacity: null, routeId: null };
        this.loadAdminData();
      },
      error: () => alert("Failed to create train.")
    });
  }

  deleteTrain(id: number) {
    this.api.deleteTrain(id).subscribe({
      next: () => this.loadAdminData(),
      error: () => alert("Failed to delete train.")
    });
  }

  viewBookings() {
    if (!this.bookingTrainId) return;
    this.api.getBookingsByTrain(this.bookingTrainId).subscribe({
      next: (data: any) => {
        this.bookings = data;
        this.cdr.detectChanges();
      },
      error: () => {
        alert("Could not load bookings.");
        this.bookings = [];
        this.cdr.detectChanges();
      }
    });
  }

  createRoute() {
    if (!this.newRouteName) return;
    this.api.addRoute({ name: this.newRouteName, stations: [] }).subscribe({
      next: () => {
        alert("Route added successfully.");
        this.newRouteName = '';
        this.loadAdminData();
      },
      error: () => alert("Failed to add route.")
    });
  }

  deleteRoute(id: number) {
    this.api.deleteRoute(id).subscribe({
      next: () => this.loadAdminData(),
      error: () => alert("Failed to delete route.")
    });
  }

  modifyRouteStation(action: 'add' | 'remove') {
    if (!this.modRouteId || !this.modStationId) return;
    if (action === 'add') {
      this.api.addStationToRoute(this.modRouteId, this.modStationId).subscribe({
        next: () => { alert("Station added to route!"); this.loadAdminData(); },
        error: () => alert("Failed to add station.")
      });
    } else {
      this.api.removeStationFromRoute(this.modRouteId, this.modStationId).subscribe({
        next: () => { alert("Station removed from route!"); this.loadAdminData(); },
        error: () => alert("Failed to remove station.")
      });
    }
  }

  logout() {
    this.auth.logout();
  }
}
