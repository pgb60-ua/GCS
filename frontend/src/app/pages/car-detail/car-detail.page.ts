import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CarService } from '../../core/services/car.service';
import { AuthService } from '../../core/services/auth.service';
import { Car } from '../../core/models/car.model';

@Component({
  selector: 'app-car-detail',
  templateUrl: './car-detail.page.html',
  styleUrls: ['./car-detail.page.scss'],
  standalone: false,
})
export class CarDetailPage {
  car: Car | null = null;
  loading = false;
  errorMessage = '';
  duplicating = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private carService: CarService,
    private authService: AuthService
  ) {}

  ionViewWillEnter(): void {
    this.errorMessage = '';
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.errorMessage = 'ID de coche no valido.';
      return;
    }
    this.loading = true;
    this.carService.getCarById(id).subscribe({
      next: (car) => {
        this.loading = false;
        this.car = car;
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err?.error?.message ?? 'No se ha podido cargar el coche.';
      },
    });
  }

  personalize(): void {
    if (!this.car) return;
    if (!this.authService.isLoggedIn()) {
      this.router.navigateByUrl('/login', { replaceUrl: true });
      return;
    }
    const user = this.authService.getCurrentUser();
    if (!user) {
      this.router.navigateByUrl('/login', { replaceUrl: true });
      return;
    }
    this.duplicating = true;
    this.carService.duplicateBaseCar(this.car.id, user.id).subscribe({
      next: (newCar) => {
        this.duplicating = false;
        this.router.navigateByUrl(`/personalizar/${newCar.id}`, { replaceUrl: true });
      },
      error: (err) => {
        this.duplicating = false;
        this.errorMessage = err?.error?.message ?? 'No se ha podido personalizar el coche.';
      },
    });
  }
}
