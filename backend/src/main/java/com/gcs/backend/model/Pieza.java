package com.gcs.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "piezas")
@Data
@NoArgsConstructor
public class Pieza {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nombre;
    
    @Column(name = "tipo_pieza")
    private String tipoPieza;
    
    private String descripcion;
    private BigDecimal precio;
    private Boolean disponible;
    
    @Column(name = "es_catalogo")
    private Boolean esCatalogo;

    @OneToMany(mappedBy = "pieza", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CochePieza> cochePiezas;

    @OneToOne(mappedBy = "pieza", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Aerodinamica aerodinamica;

    @OneToOne(mappedBy = "pieza", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Motor motor;

    @OneToOne(mappedBy = "pieza", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Pintura pintura;

    @OneToOne(mappedBy = "pieza", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Suspension suspension;
}
