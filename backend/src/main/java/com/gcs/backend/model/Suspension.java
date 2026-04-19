package com.gcs.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "suspensiones")
@Data
@NoArgsConstructor
public class Suspension {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pieza_id")
    @JsonIgnore
    private Pieza pieza;

    private String tipo;
    private String material;
    private BigDecimal dureza;
    private String eje;
}
