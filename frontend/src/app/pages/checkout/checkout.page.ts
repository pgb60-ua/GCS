import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CarSummary } from '../../core/models/car.model';
import { AuthService } from '../../core/services/auth.service';
import { CarService } from '../../core/services/car.service';
import { SaleService } from '../../core/services/sale.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.page.html',
  styleUrls: ['./checkout.page.scss'],
  standalone: false,
})
export class CheckoutPage {
  summary: CarSummary | null = null;
  loading = false;
  submitting = false;
  errorMessage = '';
  metodoPago = 'Tarjeta demo';
  readonly paymentMethods = ['Tarjeta demo', 'Transferencia demo', 'PayPal demo'];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private carService: CarService,
    private saleService: SaleService
  ) {}

  ionViewWillEnter(): void {
    this.errorMessage = '';
    const user = this.authService.getCurrentUser();
    if (!user) {
      this.router.navigateByUrl('/login', { replaceUrl: true });
      return;
    }

    const cocheId = this.route.snapshot.paramMap.get('cocheId');
    if (!cocheId) {
      this.errorMessage = 'ID de coche no valido.';
      return;
    }

    this.loadSummary(cocheId);
  }

  confirmPurchase(): void {
    const user = this.authService.getCurrentUser();
    if (!user) {
      this.router.navigateByUrl('/login', { replaceUrl: true });
      return;
    }
    if (!this.summary || this.submitting) {
      return;
    }

    this.submitting = true;
    this.errorMessage = '';
    this.saleService.createSale(user.id, this.summary.id, this.metodoPago).subscribe({
      next: (sale) => {
        this.submitting = false;
        this.router.navigateByUrl('/compras', {
          replaceUrl: true,
          state: { saleId: sale.id },
        });
      },
      error: (err) => {
        this.submitting = false;
        this.errorMessage = err?.error?.message ?? 'No se ha podido confirmar la compra.';
      },
    });
  }

  goBack(): void {
    if (this.summary) {
      this.router.navigateByUrl(`/personalizar/${this.summary.id}`, { replaceUrl: true });
      return;
    }
    this.router.navigateByUrl('/garaje', { replaceUrl: true });
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
        this.errorMessage = err?.error?.message ?? 'No se ha podido cargar el resumen del coche.';
      },
    });
  }
}
