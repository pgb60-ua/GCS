export type SaleStatus = 'PENDIENTE' | 'PAGADO' | 'CANCELADO';

export interface Sale {
  id: string;
  usuarioId: string;
  usuarioNombre: string | null;
  cocheId: string;
  cocheNombre: string | null;
  montoTotal: number;
  estadoPago: SaleStatus;
  metodoPago: string;
  fechaVenta: string;
}
