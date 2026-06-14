export interface Part {
  id: string;
  nombre: string;
  tipoPieza: 'MOTOR' | 'PINTURA' | 'SUSPENSION' | 'AERODINAMICA';
  descripcion: string | null;
  precio: number;
  disponible: boolean;
  esCatalogo: boolean;
  motor?: MotorDetail | null;
  pintura?: PinturaDetail | null;
  suspension?: SuspensionDetail | null;
  aerodinamica?: AerodinamicaDetail | null;
}

export interface MotorDetail {
  potenciaCv: number;
  combustible: string;
  cilindrada: number;
  fabricante: string;
  tipo: string;
}

export interface PinturaDetail {
  colorHex: string;
  acabado: string;
  liveryOficial: boolean;
}

export interface SuspensionDetail {
  tipo: string;
  material: string;
  dureza: number;
  eje: string;
}

export interface AerodinamicaDetail {
  coeficienteArrastre: number;
  material: string;
  zona: string;
}
