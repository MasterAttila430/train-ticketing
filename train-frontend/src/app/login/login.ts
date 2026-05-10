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
  password = '';
  role = 'customer';
  errorMsg = '';

  constructor(private auth: AuthService) {}

  onLogin() {
    if (!this.username.trim() || !this.password.trim()) {
      this.errorMsg = 'Please enter username and password.';
      return;
    }

    if (this.role === 'admin') {
      if (this.password !== 'admin') {
        this.errorMsg = 'Invalid admin password.';
        return;
      }
      this.auth.login(this.username.trim(), '', 'admin');
      return;
    }

    if (!this.email.trim() || !this.email.includes('@')) {
      this.errorMsg = 'Please enter a valid email address.';
      return;
    }

    this.auth.login(this.username.trim(), this.email.trim(), 'customer');
  }
}
