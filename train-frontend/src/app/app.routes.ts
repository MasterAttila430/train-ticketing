import { Routes } from '@angular/router';
import { LoginComponent } from './login/login';
import { BookingComponent } from './booking/booking';
import { ManagementComponent } from './management/management';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'booking', component: BookingComponent },
  { path: 'management', component: ManagementComponent }
];
