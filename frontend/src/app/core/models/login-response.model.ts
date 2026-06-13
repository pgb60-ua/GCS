export interface LoginResponse {
  id: string;
  nombre: string;
  email: string;
  rol: 'CLIENTE' | 'ADMIN';
}
