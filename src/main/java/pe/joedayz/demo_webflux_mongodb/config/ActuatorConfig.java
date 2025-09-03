package pe.joedayz.demo_webflux_mongodb.config;

import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.info.InfoEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración específica para Actuator en WebFlux
 * Habilita explícitamente los endpoints de Actuator
 */
@Configuration
@EnableAutoConfiguration(exclude = {
    // Excluir configuraciones que puedan causar conflictos
})
public class ActuatorConfig {
    
    // La configuración se maneja automáticamente por Spring Boot
    // Los endpoints se configuran en application.properties
}
