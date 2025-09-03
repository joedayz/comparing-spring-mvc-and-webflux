package pe.joedayz.demo_webflux_mongodb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Configuración de MongoDB para la aplicación
 * Habilita auditoría y configura repositorios tanto reactivos como tradicionales
 */
@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(
    basePackages = "pe.joedayz.demo_webflux_mongodb.repository",
    includeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
        org.springframework.stereotype.Repository.class
    )
)
public class MongoConfig {
    
    // La configuración por defecto de Spring Boot es suficiente
    // para manejar tanto MongoDB reactivo como tradicional
}
