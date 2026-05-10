import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  username: string = '';

  constructor(private auth: AuthService) {}

  onLogin() {
    if (this.username.trim() !== '') {
      this.auth.login(this.username.trim());
    }
  }
}
