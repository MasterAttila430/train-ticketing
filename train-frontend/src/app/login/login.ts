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
    if (!this.username.trim() || !this.email.trim()) {
      this.errorMsg = 'Please enter your username and email.';
      return;
    }
    if (!this.email.includes('@')) {
      this.errorMsg = 'Please enter a valid email address.';
      return;
    }
    this.auth.login(this.username.trim(), this.email.trim(), this.role);
  }
}
