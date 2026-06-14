import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastController } from '@ionic/angular';
import { CustomRequestService } from '../../core/services/custom-request.service';
import { AuthService } from '../../core/services/auth.service';
import { CarService } from '../../core/services/car.service';
import { PartService } from '../../core/services/part.service';
import { CarSummary } from '../../core/models/car.model';
import { Part } from '../../core/models/part.model';
import { CarPart } from '../../core/models/car-part.model';

type PartCategory = 'MOTOR' | 'PINTURA' | 'SUSPENSION' | 'AERODINAMICA';

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

  requestDescripcion = '';
  sendingRequest = false;
  requestError = '';
  selectedCategory: PartCategory = 'MOTOR';
  catalogParts: Part[] = [];
  filteredParts: Part[] = [];
  installedParts: CarPart[] = [];
  loadingParts = false;

  categories: { value: PartCategory; label: string }[] = [
    { value: 'MOTOR', label: 'Motor' },
    { value: 'PINTURA', label: 'Pintura' },
    { value: 'SUSPENSION', label: 'Suspensión' },
    { value: 'AERODINAMICA', label: 'Aerodinámica' },
  ];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private carService: CarService,
    private customRequestService: CustomRequestService,
    private authService: AuthService,
    private toastController: ToastController,
    private partService: PartService,

  ) { }

  ionViewWillEnter(): void {
    this.errorMessage = '';
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.errorMessage = 'ID de coche no valido.';
      return;
    }
    this.loadAll(id);
  }

  private loadAll(id: string): void {
    this.loading = true;
    this.carService.getCarSummary(id).subscribe({
      next: (summary) => {
        this.summary = summary;
        this.newNomenclatura = summary.nomenclatura;
        this.newDescripcion = summary.descripcion ?? '';
        this.loadCatalogParts();
        this.loadInstalledParts(id);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err?.error?.message ?? 'No se ha podido cargar el resumen.';
      },
    });
  }

  private loadCatalogParts(): void {
    this.partService.getCatalogParts().subscribe({
      next: (parts) => {
        this.catalogParts = parts;
        this.filterParts();
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      },
    });
  }

  private loadInstalledParts(carId: string): void {
    this.partService.getCarParts(carId).subscribe({
      next: (parts) => {
        this.installedParts = parts;
      },
      error: () => {
        this.installedParts = [];
      },
    });
  }

  onCategoryChange(event: any): void {
    const value = event?.detail?.value ?? event;
    this.selectedCategory = value as PartCategory;
    this.filterParts();
  }

  private filterParts(): void {
    this.filteredParts = this.catalogParts.filter(
      (p) => p.tipoPieza === this.selectedCategory
    );
  }

  getCategoryLabel(value: string): string {
    return this.categories.find((c) => c.value === value)?.label ?? value;
  }

  isPartInstalled(part: Part): boolean {
    return this.installedParts.some((cp) => cp.piezaId === part.id);
  }

  getInstalledPartForCategory(category: string): CarPart | undefined {
    return this.installedParts.find((cp) => cp.tipoPieza === category);
  }

  getInstalledCarPartByPiezaId(piezaId: string): CarPart | undefined {
    return this.installedParts.find((cp) => cp.piezaId === piezaId);
  }

  selectPart(part: Part): void {
    if (!this.summary) return;
    this.loadingParts = true;
    this.partService.addPartToCar(this.summary.id, part.id, 1).subscribe({
      next: () => {
        this.loadingParts = false;
        this.showToast(`${part.nombre} instalada correctamente`, 'success');
        this.reloadAfterChange();
      },
      error: (err) => {
        this.loadingParts = false;
        this.showToast(err?.error?.message ?? 'Error al instalar la pieza', 'danger');
      },
    });
  }

  removePart(carPart: CarPart | undefined): void {
    if (!this.summary || !carPart) return;
    this.loadingParts = true;
    this.partService.removeCarPart(this.summary.id, carPart.id).subscribe({
      next: () => {
        this.loadingParts = false;
        this.showToast(`${carPart.nombrePieza} eliminada`, 'warning');
        this.reloadAfterChange();
      },
      error: (err) => {
        this.loadingParts = false;
        this.showToast(err?.error?.message ?? 'Error al quitar la pieza', 'danger');
      },
    });
  }

  private reloadAfterChange(): void {
    if (!this.summary) return;
    const id = this.summary.id;
    this.carService.getCarSummary(id).subscribe({
      next: (s) => {
        this.summary = s;
        this.newNomenclatura = s.nomenclatura;
        this.newDescripcion = s.descripcion ?? '';
      },
    });
    this.loadInstalledParts(id);
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
        this.showToast('Cambios guardados', 'success');
        if (this.summary) {
          this.carService.getCarSummary(this.summary.id).subscribe({
            next: (s) => {
              this.summary = s;
              this.newNomenclatura = s.nomenclatura;
              this.newDescripcion = s.descripcion ?? '';
            },
          });
        }
      },
      error: (err) => {
        this.saving = false;
        this.errorMessage = err?.error?.message ?? 'No se ha podido guardar los cambios.';
      },
    });
  }

  sendCustomRequest(): void {
    this.requestError = '';
    const desc = this.requestDescripcion.trim();
    if (!desc) {
      this.requestError = 'La descripcion no puede estar vacia.';
      return;
    }
    const user = this.authService.getCurrentUser();
    if (!user || !this.summary) return;

    this.sendingRequest = true;
    this.customRequestService.createRequest(user.id, this.summary.id, desc).subscribe({
      next: async () => {
        this.sendingRequest = false;
        this.requestDescripcion = '';
        const toast = await this.toastController.create({
          message: '✅ Solicitud enviada correctamente. El equipo la revisara pronto.',
          duration: 3000,
          color: 'success',
          position: 'bottom',
        });
        await toast.present();
      },
      error: (err) => {
        this.sendingRequest = false;
        this.requestError = err?.error?.message ?? 'No se ha podido enviar la solicitud.';
      },
    });
  }

  goToGarage(): void {
    this.router.navigateByUrl('/garaje', { replaceUrl: true });
  }

  goToCheckout(): void {
    if (!this.summary) return;
    this.router.navigateByUrl(`/checkout/${this.summary.id}`);
  }
  async showToast(message: string, color: string): Promise<void> {
    const toast = await this.toastController.create({
      message,
      color,
      duration: 2500,
      position: 'bottom',
    });
    await toast.present();
  }
}

