import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CarService } from '../../core/services/car.service';
import { CarSummary } from '../../core/models/car.model';

@Component({
  selector: 'app-customize',
  templateUrl: './customize.page.html',
  styleUrls: ['./customize.page.scss'],
  standalone: false,
})
export class CustomizePage {
  summary: CarSummary | null = null;
  loading = false;
  errorMessage = '';
  saving = false;
  newNomenclatura = '';
  newDescripcion = '';

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

  loadSummary(id: string): void {
    this.loading = true;
    this.carService.getCarSummary(id).subscribe({
      next: (summary) => {
        this.loading = false;
        this.summary = summary;
        this.newNomenclatura = summary.nomenclatura;
        this.newDescripcion = summary.descripcion ?? '';
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err?.error?.message ?? 'No se ha podido cargar el resumen.';
      },
    });
  }

  saveChanges(): void {
    if (!this.summary) return;
    const payload: { nomenclatura?: string; descripcion?: string } = {};
    if (this.newNomenclatura && this.newNomenclatura !== this.summary.nomenclatura) {
      payload.nomenclatura = this.newNomenclatura;
    }
    if (this.newDescripcion && this.newDescripcion !== this.summary.descripcion) {
      payload.descripcion = this.newDescripcion;
    }
    if (Object.keys(payload).length === 0) {
      return;
    }
    this.saving = true;
    this.carService.patchCar(this.summary.id, payload).subscribe({
      next: () => {
        this.saving = false;
        if (this.summary) {
          this.loadSummary(this.summary.id);
        }
      },
      error: (err) => {
        this.saving = false;
        this.errorMessage = err?.error?.message ?? 'No se ha podido guardar los cambios.';
      },
    });
  }

  goToGarage(): void {
    this.router.navigateByUrl('/garaje', { replaceUrl: true });
  }
}
