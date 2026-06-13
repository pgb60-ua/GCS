import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Review } from '../models/review.model';
import { ReviewSummary } from '../models/review-summary.model';

interface ReviewPayload {
  usuarioId: string;
  cocheId: string;
  puntuacion: number;
  comentario: string;
}

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  private readonly base = `${environment.apiUrl}/resenias`;

  constructor(private http: HttpClient) {}

  getReviewsByCar(cocheId: string): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.base}/coche/${cocheId}`);
  }

  getReviewsByUser(usuarioId: string): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.base}/usuario/${usuarioId}`);
  }

  getReviewSummary(cocheId: string): Observable<ReviewSummary> {
    return this.http.get<ReviewSummary>(`${this.base}/coche/${cocheId}/resumen`);
  }

  createReview(
    usuarioId: string,
    cocheId: string,
    puntuacion: number,
    comentario: string
  ): Observable<Review> {
    const payload: ReviewPayload = { usuarioId, cocheId, puntuacion, comentario };
    return this.http.post<Review>(this.base, payload);
  }

  updateReview(id: string, payload: ReviewPayload): Observable<Review> {
    return this.http.put<Review>(`${this.base}/${id}`, payload);
  }

  deleteReview(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
