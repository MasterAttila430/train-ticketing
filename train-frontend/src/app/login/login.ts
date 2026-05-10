import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  username = '';
  email = '';
  role = 'customer';
  errorMsg = '';

  constructor(private auth: AuthService) {}

  onLogin() {
    if (!this.username.trim()) {
      this.errorMsg = 'Please enter your name.';
      return;
    }

    if (this.role === 'customer') {
      if (!this.email.trim() || !this.email.includes('@')) {
        this.errorMsg = 'Please enter a valid email address.';
        return;
      }
    }

    const userEmail = this.role === 'customer' ? this.email.trim() : 'admin@train.com';
    this.auth.login(this.username.trim(), userEmail, this.role);
  }
}
