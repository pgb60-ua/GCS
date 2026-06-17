import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CarSummary } from '../../core/models/car.model';
import { CarService } from '../../core/services/car.service';

@Component({
  selector: 'app-purchased-car',
  templateUrl: './purchased-car.page.html',
  styleUrls: ['./purchased-car.page.scss'],
  standalone: false,
})
export class PurchasedCarPage {
  summary: CarSummary | null = null;
  loading = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private carService: CarService
  ) {}

  ionViewWillEnter(): void {
    this.errorMessage = '';
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.errorMessage = 'ID de coche no valido.';
      return;
    }
    this.loadSummary(id);
  }

  private loadSummary(cocheId: string): void {
    this.loading = true;
    this.carService.getCarSummary(cocheId).subscribe({
      next: (summary) => {
        this.loading = false;
        this.summary = summary;
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err?.error?.message ?? 'No se ha podido cargar el coche.';
      },
    });
  }

  goBack(): void {
    this.router.navigateByUrl('/compras', { replaceUrl: true });
  }
}
