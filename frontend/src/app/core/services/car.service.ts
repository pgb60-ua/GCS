import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Car } from '../models/car.model';

@Injectable({
  providedIn: 'root',
})
export class CarService {
  private readonly base = `${environment.apiUrl}/coches`;

  constructor(private http: HttpClient) {}

  getAllCars(): Observable<Car[]> {
    return this.http.get<Car[]>(this.base);
  }

  getBaseCars(): Observable<Car[]> {
    return this.http.get<Car[]>(`${this.base}/base`);
  }

  getCarById(id: string): Observable<Car> {
    return this.http.get<Car>(`${this.base}/${id}`);
  }

  getCarsByUser(userId: string): Observable<Car[]> {
    return this.http.get<Car[]>(`${this.base}/usuario/${userId}`);
  }

  getCarsByTeam(equipoF1: string): Observable<Car[]> {
    return this.http.get<Car[]>(`${this.base}/equipo/${encodeURIComponent(equipoF1)}`);
  }
}
