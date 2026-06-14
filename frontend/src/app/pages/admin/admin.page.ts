import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastController } from '@ionic/angular';
import { CustomRequest } from '../../core/models/custom-request.model';
import { AuthService } from '../../core/services/auth.service';
import { CustomRequestService } from '../../core/services/custom-request.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.page.html',
  styleUrls: ['./admin.page.scss'],
  standalone: false,
})
export class AdminPage {
  activeSection: 'solicitudes' | 'resumen' = 'solicitudes';

  solicitudesPendientes: CustomRequest[] = [];
  todasSolicitudes: CustomRequest[] = [];
  filtroEstado: string = 'PENDIENTE';
  loadingSolicitudes = false;
  solicitudesError = '';

  selectedSolicitud: CustomRequest | null = null;
  respuestaAdmin = '';
  reviewing = false;
  reviewError = '';

  constructor(
    private authService: AuthService,
    private customRequestService: CustomRequestService,
    private toastController: ToastController,
    private router: Router
  ) { }

  ionViewWillEnter(): void {
    if (!this.authService.isAdmin()) {
      this.router.navigateByUrl('/catalogo', { replaceUrl: true });
      return;
    }
    this.loadSolicitudes(this.filtroEstado);
  }

  loadSolicitudes(estado: string): void {
    this.filtroEstado = estado;
    this.loadingSolicitudes = true;
    this.solicitudesError = '';
    this.selectedSolicitud = null;

    const obs = estado === 'TODAS'
      ? this.customRequestService.getAllRequests()
      : this.customRequestService.getRequestsByStatus(estado);

    obs.subscribe({
      next: (data) => {
        this.loadingSolicitudes = false;
        this.todasSolicitudes = data.sort(
          (a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
      },
      error: (err) => {
        this.loadingSolicitudes = false;
        this.solicitudesError = err?.error?.message ?? 'No se han podido cargar las solicitudes.';
      },
    });
  }

  selectSolicitud(s: CustomRequest): void {
    this.selectedSolicitud = s;
    this.respuestaAdmin = '';
    this.reviewError = '';
  }

  clearSelected(): void {
    this.selectedSolicitud = null;
    this.respuestaAdmin = '';
    this.reviewError = '';
  }

  async aprobar(): Promise<void> {
    await this.doReview('APROBADA');
  }

  async rechazar(): Promise<void> {
    await this.doReview('RECHAZADA');
  }

  private async doReview(estado: 'APROBADA' | 'RECHAZADA'): Promise<void> {
    this.reviewError = '';
    if (!this.respuestaAdmin.trim()) {
      this.reviewError = 'Debes escribir una respuesta antes de revisar la solicitud.';
      return;
    }
    if (!this.selectedSolicitud) return;

    this.reviewing = true;
    this.customRequestService.reviewRequest(
      this.selectedSolicitud.id,
      estado,
      this.respuestaAdmin
    ).subscribe({
      next: async () => {
        this.reviewing = false;
        this.clearSelected();
        this.loadSolicitudes(this.filtroEstado);

        const toast = await this.toastController.create({
          message: estado === 'APROBADA'
            ? '✅ Solicitud aprobada correctamente.'
            : '❌ Solicitud rechazada.',
          duration: 2500,
          color: estado === 'APROBADA' ? 'success' : 'danger',
          position: 'bottom',
        });
        await toast.present();
      },
      error: (err) => {
        this.reviewing = false;
        this.reviewError = err?.error?.message ?? 'No se ha podido revisar la solicitud.';
      },
    });
  }

  getEstadoColor(estado: string): string {
    switch (estado) {
      case 'APROBADA': return 'success';
      case 'RECHAZADA': return 'danger';
      default: return 'warning';
    }
  }

  getEstadoLabel(estado: string): string {
    switch (estado) {
      case 'APROBADA': return '✅ Aprobada';
      case 'RECHAZADA': return '❌ Rechazada';
      default: return '⏳ Pendiente';
    }
  }
}
