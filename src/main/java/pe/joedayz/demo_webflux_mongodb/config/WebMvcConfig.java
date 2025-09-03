package pe.joedayz.demo_webflux_mongodb.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración específica para Spring Web MVC
 * Solo se activa cuando las clases de Servlet están disponibles
 * Funciona en modo compatible con WebFlux
 */
@Configuration
@ConditionalOnClass(name = "javax.servlet.Servlet")
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    
    // Configuración mínima para Spring Web MVC
    // Los controladores tradicionales funcionarán en modo compatible
}
