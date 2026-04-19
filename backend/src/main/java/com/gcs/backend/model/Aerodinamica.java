package com.gcs.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "aerodinamica")
@Data
@NoArgsConstructor
public class Aerodinamica {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pieza_id")
    @JsonIgnore
    private Pieza pieza;

    @Column(name = "coeficiente_arrastre")
    private BigDecimal coeficienteArrastre;
    
    private String material;
    private String zona;
}
