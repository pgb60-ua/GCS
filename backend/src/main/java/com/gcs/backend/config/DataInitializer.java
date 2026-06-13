package com.gcs.backend.config;

import com.gcs.backend.model.*;
import com.gcs.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    @Order(1)
    CommandLineRunner initData(
            UsuarioRepository usuarioRepo,
            CocheRepository cocheRepo,
            PiezaRepository piezaRepo,
            MotorRepository motorRepo,
            PinturaRepository pinturaRepo,
            SuspensionRepository suspensionRepo,
            AerodinamicaRepository aerodinamicaRepo,
            ReseniaRepository reseniaRepo,
            SolicitudPiezaRepository solicitudRepo
    ) {
        return args -> {
            // Usuarios
            if (usuarioRepo.count() == 0) {
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setEmail("admin@gcs.com");
                admin.setPassword("1234");
                admin.setRol("ADMIN");
                usuarioRepo.save(admin);

                Usuario cliente = new Usuario();
                cliente.setNombre("Cliente Demo");
                cliente.setEmail("cliente@gcs.com");
                cliente.setPassword("1234");
                cliente.setRol("CLIENTE");
                usuarioRepo.save(cliente);
            }

            // Coches base
            if (cocheRepo.count() == 0) {
                cocheRepo.save(createCoche("RB20", "Red Bull Racing", "2024", "Monoplaza campeon del mundo 2024 con aerodinamica eficiente.", new BigDecimal("250000.00"), "https://upload.wikimedia.org/wikipedia/commons/b/b4/2024_Red_Bull_RB20.jpg", true));
                cocheRepo.save(createCoche("W15", "Mercedes-AMG F1", "2024", "Evolucion del concepto zero-pod con mejoras en suspension.", new BigDecimal("240000.00"), "https://upload.wikimedia.org/wikipedia/commons/2/22/Mercedes_W15_on_display_-_2024_Chinese_GP.jpg", true));
                cocheRepo.save(createCoche("SF-24", "Scuderia Ferrari", "2024", "Diseño agresivo con motor V6 hibrido de alta potencia.", new BigDecimal("245000.00"), "https://upload.wikimedia.org/wikipedia/commons/9/99/SF-24_at_the_Japanese_GP.jpg", true));
                cocheRepo.save(createCoche("MCL38", "McLaren F1", "2024", "Gran equilibrio entre velocidad punta y estabilidad.", new BigDecimal("230000.00"), "https://upload.wikimedia.org/wikipedia/commons/4/4e/McLaren_MCL38.jpg", true));
                cocheRepo.save(createCoche("AMR24", "Aston Martin", "2024", "Concepto verde con mejoras en eficiencia aerodinamica.", new BigDecimal("220000.00"), "https://upload.wikimedia.org/wikipedia/commons/d/d4/2024_Aston_Martin_AMR24.jpg", true));
                cocheRepo.save(createCoche("VCARB 01", "Visa Cash App RB", "2024", "Evolucion del equipo sister con componentes compartidos.", new BigDecimal("210000.00"), "https://upload.wikimedia.org/wikipedia/commons/thumb/1/11/Yuki_Tsunoda_Chinese_GP_2024.jpg/1280px-Yuki_Tsunoda_Chinese_GP_2024.jpg", true));
            }

            // Piezas de catalogo
            if (piezaRepo.count() == 0) {
                // Motores
                Pieza m1 = createPieza("Honda RBPT V6", "MOTOR", "Motor hibrido de 2024 con sistema ERS optimizado.", new BigDecimal("85000.00"), true, true);
                piezaRepo.save(m1);
                motorRepo.save(createMotor(m1, 1050, "Hibrido sintetico", new BigDecimal("1.6"), "Honda RBPT", "V6 Turbo"));

                Pieza m2 = createPieza("Mercedes-AMG M14", "MOTOR", "Motor de alto rendimiento con fiabilidad probada.", new BigDecimal("90000.00"), true, true);
                piezaRepo.save(m2);
                motorRepo.save(createMotor(m2, 1040, "Hibrido sintetico", new BigDecimal("1.6"), "Mercedes-AMG HPP", "V6 Turbo"));

                Pieza m3 = createPieza("Ferrari 066/10", "MOTOR", "Motor italiano con respuesta agresiva y sonido caracteristico.", new BigDecimal("88000.00"), true, true);
                piezaRepo.save(m3);
                motorRepo.save(createMotor(m3, 1030, "Hibrido sintetico", new BigDecimal("1.6"), "Ferrari", "V6 Turbo"));

                Pieza m4 = createPieza("Renault E-Tech RE24", "MOTOR", "Motor compacto con buena relacion peso-potencia.", new BigDecimal("82000.00"), true, true);
                piezaRepo.save(m4);
                motorRepo.save(createMotor(m4, 1010, "Hibrido sintetico", new BigDecimal("1.6"), "Renault", "V6 Turbo"));

                // Pinturas
                Pieza p1 = createPieza("Livery Red Bull", "PINTURA", "Livery oficial Red Bull Racing con acabado mate.", new BigDecimal("5000.00"), true, true);
                piezaRepo.save(p1);
                pinturaRepo.save(createPintura(p1, "#1E41FF", "Mate", true));

                Pieza p2 = createPieza("Livery Mercedes", "PINTURA", "Livery Mercedes AMG con acabado metalizado.", new BigDecimal("5000.00"), true, true);
                piezaRepo.save(p2);
                pinturaRepo.save(createPintura(p2, "#00D2BE", "Metalizado", true));

                Pieza p3 = createPieza("Livery Ferrari", "PINTURA", "Livery Ferrari Rosso Corsa con acabado brillante.", new BigDecimal("5500.00"), true, true);
                piezaRepo.save(p3);
                pinturaRepo.save(createPintura(p3, "#FF0000", "Brillante", true));

                Pieza p4 = createPieza("Livery McLaren", "PINTURA", "Livery McLaren papaya con detalles en azul.", new BigDecimal("5000.00"), true, true);
                piezaRepo.save(p4);
                pinturaRepo.save(createPintura(p4, "#FF8000", "Brillante", true));

                // Suspensiones
                Pieza s1 = createPieza("Pushrod Delantera", "SUSPENSION", "Suspension pushrod configurada para curvas rapidas.", new BigDecimal("12000.00"), true, true);
                piezaRepo.save(s1);
                suspensionRepo.save(createSuspension(s1, "Pushrod", "Carbono", new BigDecimal("8.5"), "Delantero"));

                Pieza s2 = createPieza("Pullrod Trasera", "SUSPENSION", "Suspension pullrod trasera para mejor traccion.", new BigDecimal("12000.00"), true, true);
                piezaRepo.save(s2);
                suspensionRepo.save(createSuspension(s2, "Pullrod", "Carbono", new BigDecimal("9.0"), "Trasero"));

                Pieza s3 = createPieza("Suspension Blanda", "SUSPENSION", "Setup blando para circuitos con muchos baches.", new BigDecimal("11000.00"), true, true);
                piezaRepo.save(s3);
                suspensionRepo.save(createSuspension(s3, "Hidráulica", "Titanio", new BigDecimal("6.5"), "Delantero"));

                Pieza s4 = createPieza("Suspension Dura", "SUSPENSION", "Setup duro para circuitos lisos y alta velocidad.", new BigDecimal("11000.00"), true, true);
                piezaRepo.save(s4);
                suspensionRepo.save(createSuspension(s4, "Hidráulica", "Titanio", new BigDecimal("10.5"), "Trasero"));

                // Aerodinamica
                Pieza a1 = createPieza("Aleron DRS Monza", "AERODINAMICA", "Aleron trasero con baja carga para circuitos rapidos.", new BigDecimal("15000.00"), true, true);
                piezaRepo.save(a1);
                aerodinamicaRepo.save(createAerodinamica(a1, new BigDecimal("0.22"), "Carbono", "Trasera"));

                Pieza a2 = createPieza("Aleron Mónaco", "AERODINAMICA", "Aleron trasero de alta carga para circuitos lentos.", new BigDecimal("16000.00"), true, true);
                piezaRepo.save(a2);
                aerodinamicaRepo.save(createAerodinamica(a2, new BigDecimal("0.35"), "Carbono", "Trasera"));

                Pieza a3 = createPieza("Front Wing 2024", "AERODINAMICA", "Ala delantera con configuracion optimizada para Y2024.", new BigDecimal("14000.00"), true, true);
                piezaRepo.save(a3);
                aerodinamicaRepo.save(createAerodinamica(a3, new BigDecimal("0.28"), "Carbono", "Delantera"));

                Pieza a4 = createPieza("Difusor Extremo", "AERODINAMICA", "Difusor con extraccion maxima de vacio.", new BigDecimal("18000.00"), true, true);
                piezaRepo.save(a4);
                aerodinamicaRepo.save(createAerodinamica(a4, new BigDecimal("0.18"), "Carbono", "Inferior"));
            }

            // Reseñas iniciales
            if (reseniaRepo.count() == 0) {
                Usuario admin = usuarioRepo.findByEmail("admin@gcs.com").orElse(null);
                Coche rb20 = cocheRepo.findAll().stream().filter(c -> "RB20".equals(c.getNomenclatura())).findFirst().orElse(null);
                if (admin != null && rb20 != null) {
                    Resenia r = new Resenia();
                    r.setUsuario(admin);
                    r.setCoche(rb20);
                    r.setPuntuacion(5);
                    r.setComentario("Excelente configurador, el RB20 es increible.");
                    reseniaRepo.save(r);
                }
            }

            // Solicitud inicial
            if (solicitudRepo.count() == 0) {
                Usuario cliente = usuarioRepo.findByEmail("cliente@gcs.com").orElse(null);
                Coche w15 = cocheRepo.findAll().stream().filter(c -> "W15".equals(c.getNomenclatura())).findFirst().orElse(null);
                if (cliente != null && w15 != null) {
                    SolicitudPieza s = new SolicitudPieza();
                    s.setUsuario(cliente);
                    s.setCoche(w15);
                    s.setDescripcion("Quiero un aleron especial inspirado en Monaco");
                    s.setEstado("PENDIENTE");
                    solicitudRepo.save(s);
                }
            }
        };
    }

    private Coche createCoche(String nomenclatura, String equipoF1, String temporada, String descripcion, BigDecimal precioBase, String imagenUrl, boolean esBase) {
        Coche c = new Coche();
        c.setNomenclatura(nomenclatura);
        c.setEquipoF1(equipoF1);
        c.setTemporada(temporada);
        c.setDescripcion(descripcion);
        c.setPrecioBase(precioBase);
        c.setPrecioTotal(precioBase);
        c.setImagenUrl(imagenUrl);
        c.setEsBase(esBase);
        return c;
    }

    private Pieza createPieza(String nombre, String tipoPieza, String descripcion, BigDecimal precio, boolean disponible, boolean esCatalogo) {
        Pieza p = new Pieza();
        p.setNombre(nombre);
        p.setTipoPieza(tipoPieza);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setDisponible(disponible);
        p.setEsCatalogo(esCatalogo);
        return p;
    }

    private Motor createMotor(Pieza pieza, int potenciaCv, String combustible, BigDecimal cilindrada, String fabricante, String tipo) {
        Motor m = new Motor();
        m.setPieza(pieza);
        m.setPotenciaCv(potenciaCv);
        m.setCombustible(combustible);
        m.setCilindrada(cilindrada);
        m.setFabricante(fabricante);
        m.setTipo(tipo);
        return m;
    }

    private Pintura createPintura(Pieza pieza, String colorHex, String acabado, boolean liveryOficial) {
        Pintura p = new Pintura();
        p.setPieza(pieza);
        p.setColorHex(colorHex);
        p.setAcabado(acabado);
        p.setLiveryOficial(liveryOficial);
        return p;
    }

    private Suspension createSuspension(Pieza pieza, String tipo, String material, BigDecimal dureza, String eje) {
        Suspension s = new Suspension();
        s.setPieza(pieza);
        s.setTipo(tipo);
        s.setMaterial(material);
        s.setDureza(dureza);
        s.setEje(eje);
        return s;
    }

    private Aerodinamica createAerodinamica(Pieza pieza, BigDecimal coeficienteArrastre, String material, String zona) {
        Aerodinamica a = new Aerodinamica();
        a.setPieza(pieza);
        a.setCoeficienteArrastre(coeficienteArrastre);
        a.setMaterial(material);
        a.setZona(zona);
        return a;
    }
}
