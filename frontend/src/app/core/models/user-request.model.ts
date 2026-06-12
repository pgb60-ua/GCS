export interface UserRequest {
  nombre: string;
  email: string;
  password: string;
  rol: 'CLIENTE' | 'ADMIN';
}
