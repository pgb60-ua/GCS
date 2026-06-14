import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Sale } from '../../core/models/sale.model';
import { AuthService } from '../../core/services/auth.service';
import { SaleService } from '../../core/services/sale.service';

@Component({
  selector: 'app-purchases',
  templateUrl: './purchases.page.html',
  styleUrls: ['./purchases.page.scss'],
  standalone: false,
})
export class PurchasesPage {
  sales: Sale[] = [];
  loading = false;
  errorMessage = '';
  highlightedSaleId: string | null = null;

  constructor(
    private router: Router,
    private authService: AuthService,
    private saleService: SaleService
  ) {}

  ionViewWillEnter(): void {
    this.highlightedSaleId = this.consumeHighlightedSaleId();
    this.errorMessage = '';

    const user = this.authService.getCurrentUser();
    if (!user) {
      this.router.navigateByUrl('/login', { replaceUrl: true });
      return;
    }

    this.loading = true;
    this.saleService.getSalesByUser(user.id).subscribe({
      next: (sales) => {
        this.loading = false;
        this.sales = sales;
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err?.error?.message ?? 'No se ha podido cargar el historial de compras.';
      },
    });
  }

  openCar(sale: Sale): void {
    this.router.navigateByUrl(`/personalizar/${sale.cocheId}`, { replaceUrl: true });
  }

  private consumeHighlightedSaleId(): string | null {
    const saleId = window.history.state?.saleId ?? null;
    if (saleId) {
      const nextState = { ...window.history.state };
      delete nextState.saleId;
      window.history.replaceState(nextState, document.title, window.location.href);
    }
    return saleId;
  }
}
