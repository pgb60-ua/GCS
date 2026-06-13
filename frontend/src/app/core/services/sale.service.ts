import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SaleRequest } from '../models/sale-request.model';
import { Sale } from '../models/sale.model';

@Injectable({
  providedIn: 'root',
})
export class SaleService {
  private readonly baseUrl = `${environment.apiUrl}/ventas`;

  constructor(private http: HttpClient) {}

  createSale(usuarioId: string, cocheId: string, metodoPago: string): Observable<Sale> {
    const request: SaleRequest = { usuarioId, cocheId, metodoPago };
    return this.http.post<Sale>(this.baseUrl, request);
  }

  getSalesByUser(usuarioId: string): Observable<Sale[]> {
    return this.http.get<Sale[]>(`${this.baseUrl}/usuario/${usuarioId}`);
  }

  getSaleById(id: string): Observable<Sale> {
    return this.http.get<Sale>(`${this.baseUrl}/${id}`);
  }
}
