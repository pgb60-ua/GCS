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
                "assets/cars/ferrari.svg"
            ),
            baseCar(
                "RB19",
                "Red Bull Racing",
                "2023",
                "El monoplaza dominante de la temporada 2023. Motor Honda RBPTH001 de bajo consumo. Chasis con mayor carga aerodinámica de la historia de Red Bull.",
                BigDecimal.valueOf(310000),
                "assets/cars/redbull.svg"
            ),
            baseCar(
                "W14",
                "Mercedes-AMG",
                "2023",
                "Monoplaza de recuperacion de Mercedes tras los problemas de 2022. Sidepods evolucionados y fondo plano mejorado para maximizar la carga en curva.",
                BigDecimal.valueOf(295000),
                "assets/cars/mercedes.svg"
            ),
            baseCar(
                "MCL60",
                "McLaren",
                "2023",
                "El MCL60 celebra los 60 años de McLaren en F1. Motor Mercedes M14 e de alta potencia. Paquete aerodinámico renovado que recupero la competitividad del equipo.",
                BigDecimal.valueOf(265000),
                "assets/cars/mclaren.svg"
            ),
            baseCar(
                "AMR23",
                "Aston Martin",
                "2023",
                "El coche mas rapido del primer sector de la temporada 2023. Inspirado en el RB19, el AMR23 supero todas las expectativas con Fernando Alonso al volante.",
                BigDecimal.valueOf(275000),
                "assets/cars/astonmartin.svg"
            ),
            baseCar(
                "A523",
                "Alpine",
                "2023",
                "Monoplaza frances con motor Renault E-Tech RE23. Diseño aerodinámico propio con filosofia diferenciada respecto a los equipos de referencia de la parrilla.",
                BigDecimal.valueOf(250000),
                "assets/cars/alpine.svg"
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
