package com.gcs.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "motores")
@Data
@NoArgsConstructor
public class Motor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pieza_id")
    @JsonIgnore
    private Pieza pieza;

    @Column(name = "potencia_cv")
    private Integer potenciaCv;
    
    private String combustible;
    private BigDecimal cilindrada;
    private String fabricante;
    private String tipo;
}
