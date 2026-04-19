package com.gcs.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "pinturas")
@Data
@NoArgsConstructor
public class Pintura {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pieza_id")
    @JsonIgnore
    private Pieza pieza;

    @Column(name = "color_hex")
    private String colorHex;
    
    private String acabado;
    
    @Column(name = "livery_oficial")
    private Boolean liveryOficial;
}
