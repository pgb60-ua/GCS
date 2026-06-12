import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest } from '../models/login-request.model';
import { LoginResponse } from '../models/login-response.model';
import { UserRequest } from '../models/user-request.model';
import { User } from '../models/user.model';

const STORAGE_KEY = 'gcs_current_user';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(this.readStoredUser());
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<LoginResponse> {
    const request: LoginRequest = { email, password };
    return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, request).pipe(
      tap((response) => this.setCurrentUser(response))
    );
  }

  register(nombre: string, email: string, password: string): Observable<User> {
    const request: UserRequest = {
      nombre,
      email,
      password,
      rol: 'CLIENTE',
    };
    return this.http.post<User>(`${environment.apiUrl}/usuarios`, request).pipe(
      tap((response) => this.setCurrentUser(response))
    );
  }

  logout(): void {
    localStorage.removeItem(STORAGE_KEY);
    this.currentUserSubject.next(null);
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  isLoggedIn(): boolean {
    return this.currentUserSubject.value !== null;
  }

  isAdmin(): boolean {
    return this.currentUserSubject.value?.rol === 'ADMIN';
  }

  isCliente(): boolean {
    return this.currentUserSubject.value?.rol === 'CLIENTE';
  }

  private setCurrentUser(user: LoginResponse | User): void {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  private readStoredUser(): User | null {
    const rawUser = localStorage.getItem(STORAGE_KEY);
    if (!rawUser) {
      return null;
    }

    try {
      return JSON.parse(rawUser) as User;
    } catch {
      localStorage.removeItem(STORAGE_KEY);
      return null;
    }
  }
}
