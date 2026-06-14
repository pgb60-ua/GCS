import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CustomRequest } from '../models/custom-request.model';
import { ReviewRequest } from '../models/review-request.model';

@Injectable({
  providedIn: 'root',
})
export class CustomRequestService {
  private readonly baseUrl = `${environment.apiUrl}/solicitudes-pieza`;

  constructor(private http: HttpClient) {}

  createRequest(usuarioId: string, cocheId: string, descripcion: string): Observable<CustomRequest> {
    return this.http.post<CustomRequest>(this.baseUrl, { usuarioId, cocheId, descripcion });
  }

  getRequestsByUser(usuarioId: string): Observable<CustomRequest[]> {
    return this.http.get<CustomRequest[]>(`${this.baseUrl}/usuario/${usuarioId}`);
  }

  getAllRequests(): Observable<CustomRequest[]> {
    return this.http.get<CustomRequest[]>(this.baseUrl);
  }

  getRequestsByStatus(estado: string): Observable<CustomRequest[]> {
    return this.http.get<CustomRequest[]>(`${this.baseUrl}/estado/${estado}`);
  }

  reviewRequest(id: string, estado: 'APROBADA' | 'RECHAZADA', respuestaAdmin: string): Observable<CustomRequest> {
    const body: ReviewRequest = { estado, respuestaAdmin };
    return this.http.patch<CustomRequest>(`${this.baseUrl}/${id}/revisar`, body);
  }

  deleteRequest(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
