package com.gcs.backend.config;

import com.gcs.backend.model.Coche;
import com.gcs.backend.repository.CocheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Order(2)
public class CocheDataInitializer implements CommandLineRunner {

    @Autowired
    private CocheRepository cocheRepository;

    @Override
    public void run(String... args) {
        if (!cocheRepository.findByEsBaseTrue().isEmpty()) {
            return;
        }

        List<Coche> coches = List.of(
            baseCar(
                "SF-23",
                "Ferrari",
                "2023",
                "Monoplaza de Ferrari para la temporada 2023. Motor V6 hibrido de 1.6L con unidad de potencia Ferrari 066/10. Aerodinamica rediseñada con sidepods revolucionarios.",
                BigDecimal.valueOf(285000),
                "https://placehold.co/800x450/1a0000/e10600?text=Ferrari+SF-23"
            ),
            baseCar(
                "RB19",
                "Red Bull Racing",
                "2023",
                "El monoplaza dominante de la temporada 2023. Motor Honda RBPTH001 de bajo consumo. Chasis con mayor carga aerodinámica de la historia de Red Bull.",
                BigDecimal.valueOf(310000),
                "https://placehold.co/800x450/001a3a/3b82f6?text=Red+Bull+RB19"
            ),
            baseCar(
                "W14",
                "Mercedes-AMG",
                "2023",
                "Monoplaza de recuperacion de Mercedes tras los problemas de 2022. Sidepods evolucionados y fondo plano mejorado para maximizar la carga en curva.",
                BigDecimal.valueOf(295000),
                "https://placehold.co/800x450/001a15/29dec9?text=Mercedes+W14"
            ),
            baseCar(
                "MCL60",
                "McLaren",
                "2023",
                "El MCL60 celebra los 60 años de McLaren en F1. Motor Mercedes M14 e de alta potencia. Paquete aerodinámico renovado que recupero la competitividad del equipo.",
                BigDecimal.valueOf(265000),
                "https://placehold.co/800x450/3a1800/ff6b00?text=McLaren+MCL60"
            ),
            baseCar(
                "AMR23",
                "Aston Martin",
                "2023",
                "El coche mas rapido del primer sector de la temporada 2023. Inspirado en el RB19, el AMR23 supero todas las expectativas con Fernando Alonso al volante.",
                BigDecimal.valueOf(275000),
                "https://placehold.co/800x450/001a0d/10b981?text=Aston+Martin+AMR23"
            ),
            baseCar(
                "A523",
                "Alpine",
                "2023",
                "Monoplaza frances con motor Renault E-Tech RE23. Diseño aerodinámico propio con filosofia diferenciada respecto a los equipos de referencia de la parrilla.",
                BigDecimal.valueOf(250000),
                "https://placehold.co/800x450/001a3a/60a5fa?text=Alpine+A523"
            )
        );

        cocheRepository.saveAll(coches);
        System.out.println("[GCS] Coches base F1 inicializados: " + coches.size() + " coches.");
    }

    private Coche baseCar(String nomenclatura, String equipo, String temporada,
                          String descripcion, BigDecimal precio, String imagen) {
        Coche c = new Coche();
        c.setNomenclatura(nomenclatura);
        c.setEquipoF1(equipo);
        c.setTemporada(temporada);
        c.setDescripcion(descripcion);
        c.setPrecioBase(precio);
        c.setPrecioTotal(precio);
        c.setImagenUrl(imagen);
        c.setEsBase(true);
        return c;
    }
}
