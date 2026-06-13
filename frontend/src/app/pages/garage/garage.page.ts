import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CarService } from '../../core/services/car.service';
import { AuthService } from '../../core/services/auth.service';
import { Car } from '../../core/models/car.model';

@Component({
  selector: 'app-garage',
  templateUrl: './garage.page.html',
  styleUrls: ['./garage.page.scss'],
  standalone: false,
})
export class GaragePage {
  cars: Car[] = [];
  loading = false;
  errorMessage = '';

  constructor(
    private router: Router,
    private carService: CarService,
    private authService: AuthService
  ) {}

  ionViewWillEnter(): void {
    this.errorMessage = '';
    const user = this.authService.getCurrentUser();
    if (!user) {
      this.router.navigateByUrl('/login', { replaceUrl: true });
      return;
    }
    this.loading = true;
    this.carService.getCarsByUser(user.id).subscribe({
      next: (cars) => {
        this.loading = false;
        this.cars = cars;
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err?.error?.message ?? 'No se ha podido cargar el garaje.';
      },
    });
  }

  openCar(id: string): void {
    this.router.navigateByUrl(`/personalizar/${id}`, { replaceUrl: true });
  }
}
