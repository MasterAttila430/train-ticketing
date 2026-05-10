import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private router: Router) {}

  login(username: string, email: string, role: string) {
    localStorage.setItem('username', username);
    localStorage.setItem('email', email);
    localStorage.setItem('userRole', role);

    if (role === 'admin') {
      this.router.navigate(['/management']);
    } else {
      this.router.navigate(['/booking']);
    }
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  getUsername() { return localStorage.getItem('username'); }
  getEmail() { return localStorage.getItem('email'); }
  getRole() { return localStorage.getItem('userRole'); }
}
