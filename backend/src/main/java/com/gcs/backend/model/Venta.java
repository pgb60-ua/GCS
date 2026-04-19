package com.gcs.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coche_id")
    @JsonIgnore
    private Coche coche;

    @Column(name = "monto_total")
    private BigDecimal montoTotal;
    
    @Column(name = "estado_pago")
    private String estadoPago;
    
    @Column(name = "metodo_pago")
    private String metodoPago;
    
    @Column(name = "fecha_venta")
    private Timestamp fechaVenta;
}
