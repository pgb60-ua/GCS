import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Car, CarSummary } from '../models/car.model';

@Injectable({
  providedIn: 'root',
})
export class CarService {
  private readonly baseUrl = `${environment.apiUrl}/coches`;

  constructor(private http: HttpClient) {}

  getAllCars(): Observable<Car[]> {
    return this.http.get<Car[]>(this.baseUrl);
  }

  getBaseCars(): Observable<Car[]> {
    return this.http.get<Car[]>(`${this.baseUrl}/base`);
  }

  getCarById(id: string): Observable<Car> {
    return this.http.get<Car>(`${this.baseUrl}/${id}`);
  }

  getCarsByUser(userId: string): Observable<Car[]> {
    return this.http.get<Car[]>(`${this.baseUrl}/usuario/${userId}`);
  }

  getCarsByTeam(equipoF1: string): Observable<Car[]> {
    return this.http.get<Car[]>(`${this.baseUrl}/equipo/${encodeURIComponent(equipoF1)}`);
  }

  getCarSummary(id: string): Observable<CarSummary> {
    return this.http.get<CarSummary>(`${this.baseUrl}/${id}/resumen`);
  }

  duplicateBaseCar(baseCarId: string, userId: string): Observable<Car> {
    return this.http.post<Car>(`${this.baseUrl}/${baseCarId}/duplicar`, { usuarioId: userId });
  }

  patchCar(id: string, payload: Partial<Car>): Observable<Car> {
    return this.http.patch<Car>(`${this.baseUrl}/${id}`, payload);
  }
}
