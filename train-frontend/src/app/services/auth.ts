import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private router: Router) {}

  login(username: string) {
    const role = (username === 'admin') ? 'admin' : 'customer';
    localStorage.setItem('userRole', role);
    localStorage.setItem('username', username);

    // Átirányítás a megfelelő oldalra
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
}
