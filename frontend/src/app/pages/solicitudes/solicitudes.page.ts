import { Component } from '@angular/core';
import { CustomRequest } from '../../core/models/custom-request.model';
import { AuthService } from '../../core/services/auth.service';
import { CustomRequestService } from '../../core/services/custom-request.service';

@Component({
  selector: 'app-solicitudes',
  templateUrl: './solicitudes.page.html',
  styleUrls: ['./solicitudes.page.scss'],
  standalone: false,
})
export class SolicitudesPage {
  solicitudes: CustomRequest[] = [];
  loading = false;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private customRequestService: CustomRequestService
  ) {}

  ionViewWillEnter(): void {
    this.loadSolicitudes();
  }

  loadSolicitudes(): void {
    const user = this.authService.getCurrentUser();
    if (!user) {
      this.errorMessage = 'Debes iniciar sesion para ver tus solicitudes.';
      return;
    }
    this.loading = true;
    this.errorMessage = '';
    this.customRequestService.getRequestsByUser(user.id).subscribe({
      next: (data) => {
        this.loading = false;
        this.solicitudes = data.sort(
          (a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err?.error?.message ?? 'No se han podido cargar las solicitudes.';
      },
    });
  }

  getEstadoColor(estado: string): string {
    switch (estado) {
      case 'APROBADA':  return 'success';
      case 'RECHAZADA': return 'danger';
      default:          return 'warning';
    }
  }

  getEstadoLabel(estado: string): string {
    switch (estado) {
      case 'APROBADA':  return '✅ Aprobada';
      case 'RECHAZADA': return '❌ Rechazada';
      default:          return '⏳ Pendiente';
    }
  }
}
