import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../core/models/user.model';
import { AuthService } from '../core/services/auth.service';
import { CarService } from '../core/services/car.service';
import { Car } from '../core/models/car.model';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: false,
})
export class HomePage {
  currentUser: User | null = null;
  cars: Car[] = [];
  loading = false;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private carService: CarService,
    private router: Router
  ) {}

  ionViewWillEnter(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.loadCars();
  }

  loadCars(): void {
    this.loading = true;
    this.errorMessage = '';
    this.carService.getBaseCars().subscribe({
      next: (cars) => {
        this.loading = false;
        this.cars = cars;
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err?.error?.message ?? 'No se ha podido cargar el catalogo.';
      },
    });
  }

  openCar(id: string): void {
    this.router.navigateByUrl(`/coches/${id}`, { replaceUrl: true });
  }
}
