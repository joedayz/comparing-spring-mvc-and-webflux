package pe.joedayz.demo_webflux_mongodb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pe.joedayz.demo_webflux_mongodb.model.Category;
import pe.joedayz.demo_webflux_mongodb.repository.ReactiveCategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/reactive/categories")
@CrossOrigin(origins = "*")
public class ReactiveCategoryController {
    
    private static final Logger logger = LoggerFactory.getLogger(ReactiveCategoryController.class);
    
    private final ReactiveCategoryRepository categoryRepository;
    
    public ReactiveCategoryController(ReactiveCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    @GetMapping
    public Flux<Category> getAllCategories() {
        logger.info("Endpoint reactivo: Obteniendo todas las categorías");
        return categoryRepository.findAll()
                .doOnComplete(() -> logger.info("Todas las categorías obtenidas exitosamente"))
                .doOnError(error -> logger.error("Error al obtener categorías: {}", error.getMessage()));
    }
    
    @GetMapping("/{id}")
    public Mono<Category> getCategoryById(@PathVariable String id) {
        logger.info("Endpoint reactivo: Obteniendo categoría por ID: {}", id);
        return categoryRepository.findById(id)
                .doOnSuccess(category -> logger.info("Categoría encontrada con ID: {}", id))
                .doOnError(error -> logger.error("Error al obtener categoría con ID {}: {}", id, error.getMessage()));
    }
    
    @GetMapping("/name/{name}")
    public Mono<Category> getCategoryByName(@PathVariable String name) {
        logger.info("Endpoint reactivo: Obteniendo categoría por nombre: {}", name);
        return categoryRepository.findByName(name)
                .doOnSuccess(category -> logger.info("Categoría encontrada con nombre: {}", name))
                .doOnError(error -> logger.error("Error al obtener categoría con nombre {}: {}", name, error.getMessage()));
    }
}
