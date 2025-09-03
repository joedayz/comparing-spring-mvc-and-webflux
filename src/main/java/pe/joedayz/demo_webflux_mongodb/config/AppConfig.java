package pe.joedayz.demo_webflux_mongodb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * Configuración principal de la aplicación
 * WebFlux es el framework principal, Spring Web MVC funciona en modo compatible
 */
@Configuration
@EnableWebFlux
public class AppConfig {
    
    // La configuración por defecto de Spring Boot maneja la coexistencia
    // WebFlux tiene prioridad, Spring Web MVC funciona en modo compatible
}
