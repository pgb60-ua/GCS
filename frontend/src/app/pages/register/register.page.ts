import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.page.html',
  styleUrls: ['./register.page.scss'],
  standalone: false,
})
export class RegisterPage {
  nombre = '';
  email = '';
  password = '';
  confirmPassword = '';
  loading = false;
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  ionViewWillEnter(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigateByUrl('/catalogo', { replaceUrl: true });
    }
  }

  submit(): void {
    this.errorMessage = '';

    if (!this.nombre.trim() || !this.email.trim() || !this.password) {
      this.errorMessage = 'Completa nombre, email y password.';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Las passwords no coinciden.';
      return;
    }

    this.loading = true;
    this.authService.register(this.nombre, this.email, this.password).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigateByUrl('/catalogo', { replaceUrl: true });
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage = error?.error?.message ?? 'No se ha podido crear la cuenta.';
      },
    });
  }
}
