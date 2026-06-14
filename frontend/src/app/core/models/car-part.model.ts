export interface CarPart {
  id: string;
  cocheId: string;
  piezaId: string;
  nombrePieza: string;
  tipoPieza: string;
  precio: number;
  cantidad: number;
  subtotal: number;
  notas: string | null;
}
