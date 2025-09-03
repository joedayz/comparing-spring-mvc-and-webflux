package pe.joedayz.demo_webflux_mongodb.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración específica para métricas y monitoreo
 * Compatible con Spring Boot 3.x y Micrometer
 */
@Configuration
public class MetricsConfig {

    /**
     * Habilita la anotación @Timed para medir métodos específicos
     */
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
