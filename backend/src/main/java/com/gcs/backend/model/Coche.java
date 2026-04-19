package com.gcs.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "coches")
@Data
@NoArgsConstructor
public class Coche {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nomenclatura;
    
    @Column(name = "equipo_f1")
    private String equipoF1;
    
    private String temporada;
    private String descripcion;
    
    @Column(name = "precio_base")
    private BigDecimal precioBase;
    
    @Column(name = "imagen_url")
    private String imagenUrl;
    
    @Column(name = "es_base")
    private Boolean esBase;
    
    @Column(name = "precio_total")
    private BigDecimal precioTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @OneToMany(mappedBy = "coche", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CochePieza> cochePiezas;

    @OneToOne(mappedBy = "coche", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Venta venta;

    @OneToMany(mappedBy = "coche", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SolicitudPieza> solicitudes;

    @OneToMany(mappedBy = "coche", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Resenia> resenias;
}
