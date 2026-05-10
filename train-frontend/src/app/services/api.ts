import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = '/api';

  constructor(private http: HttpClient) {}

  getStations(): Observable<any> { return this.http.get(`${this.baseUrl}/stations`); }
  getTrains(): Observable<any> { return this.http.get(`${this.baseUrl}/trains`); }
  getRoutes(): Observable<any> { return this.http.get(`${this.baseUrl}/routes`); }
  findRoute(from: number, to: number, after: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/routes/find?from=${from}&to=${to}&after=${after}`);
  }
  bookTicket(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/bookings`, data);
  }

  delayTrain(id: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/trains/${id}/delay`, {});
  }
  getBookingsByTrain(trainId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/bookings/train/${trainId}`);
  }
  addTrain(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/trains`, data);
  }
  deleteTrain(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/trains/${id}`);
  }
  addRoute(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/routes`, data);
  }
  deleteRoute(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/routes/${id}`);
  }
  addStationToRoute(routeId: number, stationId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/routes/${routeId}/stations/${stationId}`, {});
  }
  removeStationFromRoute(routeId: number, stationId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/routes/${routeId}/stations/${stationId}`);
  }
}
