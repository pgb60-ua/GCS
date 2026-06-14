import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Part } from '../models/part.model';
import { CarPart } from '../models/car-part.model';

@Injectable({
  providedIn: 'root',
})
export class PartService {
  private readonly baseUrl = `${environment.apiUrl}/piezas`;
  private readonly cocheUrl = `${environment.apiUrl}/coches`;

  constructor(private http: HttpClient) {}

  getCatalogParts(): Observable<Part[]> {
    return this.http.get<Part[]>(`${this.baseUrl}/catalogo`);
  }

  getPartsByType(type: string): Observable<Part[]> {
    return this.http.get<Part[]>(`${this.baseUrl}/tipo/${type}`);
  }

  getCarParts(carId: string): Observable<CarPart[]> {
    return this.http.get<CarPart[]>(`${this.cocheUrl}/${carId}/piezas`);
  }

  addPartToCar(carId: string, piezaId: string, cantidad: number, notas?: string): Observable<CarPart> {
    return this.http.post<CarPart>(`${this.cocheUrl}/${carId}/piezas`, { piezaId, cantidad, notas });
  }

  updateCarPart(carId: string, cpId: string, cantidad: number, notas?: string): Observable<CarPart> {
    return this.http.put<CarPart>(`${this.cocheUrl}/${carId}/piezas/${cpId}`, { cantidad, notas });
  }

  removeCarPart(carId: string, cpId: string): Observable<void> {
    return this.http.delete<void>(`${this.cocheUrl}/${carId}/piezas/${cpId}`);
  }
}
