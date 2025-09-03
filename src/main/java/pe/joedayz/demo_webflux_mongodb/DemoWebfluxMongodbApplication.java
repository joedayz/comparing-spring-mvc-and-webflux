package pe.joedayz.demo_webflux_mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación principal que demuestra WebFlux vs Spring Web tradicional
 * Esta aplicación incluye ambos enfoques para comparación
 * 
 * La configuración de WebFlux y Spring Web MVC se maneja en WebConfig.java
 * para evitar conflictos de beans
 */
@SpringBootApplication
public class DemoWebfluxMongodbApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoWebfluxMongodbApplication.class, args);
	}
}
