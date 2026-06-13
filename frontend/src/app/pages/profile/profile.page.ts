import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Review } from '../../core/models/review.model';
import { User } from '../../core/models/user.model';
import { AuthService } from '../../core/services/auth.service';
import { ReviewService } from '../../core/services/review.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.page.html',
  styleUrls: ['./profile.page.scss'],
  standalone: false,
})
export class ProfilePage {
  user: User | null = null;
  reviews: Review[] = [];
  loading = true;
  saveLoading = false;
  errorMessage = '';
  saveMessage = '';

  formNombre = '';
  formEmail = '';
  formPassword = '';

  constructor(
    private authService: AuthService,
    private reviewService: ReviewService,
    private router: Router
  ) {}

  ionViewWillEnter(): void {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      this.router.navigateByUrl('/login', { replaceUrl: true });
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.saveMessage = '';

    forkJoin({
      user: this.authService.getUserById(currentUser.id),
      reviews: this.reviewService.getReviewsByUser(currentUser.id),
    }).subscribe({
      next: ({ user, reviews }) => {
        this.user = user;
        this.formNombre = user.nombre;
        this.formEmail = user.email;
        this.reviews = reviews;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = error?.error?.message ?? 'No se pudo cargar el perfil.';
        this.loading = false;
      },
    });
  }

  saveProfile(): void {
    if (!this.user) {
      return;
    }

    this.errorMessage = '';
    this.saveMessage = '';

    const nombre = this.formNombre.trim();
    const email = this.formEmail.trim();

    if (!nombre || !email) {
      this.errorMessage = 'Nombre y email son obligatorios.';
      return;
    }

    if (this.formPassword && this.formPassword.length < 4) {
      this.errorMessage = 'La password debe tener al menos 4 caracteres.';
      return;
    }

    this.saveLoading = true;
    this.authService
      .updateUserById(this.user.id, {
        nombre,
        email,
        password: this.formPassword || undefined,
      })
      .subscribe({
        next: (updated) => {
          this.user = updated;
          this.formNombre = updated.nombre;
          this.formEmail = updated.email;
          this.formPassword = '';
          this.saveLoading = false;
          this.saveMessage = 'Perfil actualizado correctamente.';
        },
        error: (error) => {
          this.saveLoading = false;
          this.errorMessage = error?.error?.message ?? 'No se pudo actualizar el perfil.';
        },
      });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login', { replaceUrl: true });
  }

  getStars(rating: number): string {
    const fullStars = '★'.repeat(Math.max(0, Math.min(5, rating)));
    const emptyStars = '☆'.repeat(Math.max(0, 5 - rating));
    return `${fullStars}${emptyStars}`;
  }
}
