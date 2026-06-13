import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Car, CarSummary } from '../models/car.model';

@Injectable({
  providedIn: 'root',
})
export class CarService {
  constructor(private http: HttpClient) {}

  getBaseCars(): Observable<Car[]> {
    return this.http.get<Car[]>(`${environment.apiUrl}/coches/base`);
  }

  getAllCars(): Observable<Car[]> {
    return this.http.get<Car[]>(`${environment.apiUrl}/coches`);
  }

  getCarById(id: string): Observable<Car> {
    return this.http.get<Car>(`${environment.apiUrl}/coches/${id}`);
  }

  getCarsByUser(userId: string): Observable<Car[]> {
    return this.http.get<Car[]>(`${environment.apiUrl}/coches/usuario/${userId}`);
  }

  getCarSummary(id: string): Observable<CarSummary> {
    return this.http.get<CarSummary>(`${environment.apiUrl}/coches/${id}/resumen`);
  }

  duplicateBaseCar(baseCarId: string, userId: string): Observable<Car> {
    return this.http.post<Car>(`${environment.apiUrl}/coches/${baseCarId}/duplicar`, { usuarioId: userId });
  }

  patchCar(id: string, payload: Partial<Car>): Observable<Car> {
    return this.http.patch<Car>(`${environment.apiUrl}/coches/${id}`, payload);
  }
}
