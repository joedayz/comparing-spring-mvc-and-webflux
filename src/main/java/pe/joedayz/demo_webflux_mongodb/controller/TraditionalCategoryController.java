package pe.joedayz.demo_webflux_mongodb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pe.joedayz.demo_webflux_mongodb.model.Category;
import pe.joedayz.demo_webflux_mongodb.repository.TraditionalCategoryRepository;
import java.util.List;

@RestController
@RequestMapping("/api/v1/traditional/categories")
@CrossOrigin(origins = "*")
public class TraditionalCategoryController {
    
    private static final Logger logger = LoggerFactory.getLogger(TraditionalCategoryController.class);
    
    private final TraditionalCategoryRepository categoryRepository;
    
    public TraditionalCategoryController(TraditionalCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    @GetMapping
    public List<Category> getAllCategories() {
        logger.info("Endpoint tradicional: Obteniendo todas las categorías");
        List<Category> categories = categoryRepository.findAll();
        logger.info("Todas las categorías obtenidas exitosamente: {}", categories.size());
        return categories;
    }
    
    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable String id) {
        logger.info("Endpoint tradicional: Obteniendo categoría por ID: {}", id);
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            logger.info("Categoría encontrada con ID: {}", id);
        } else {
            logger.warn("Categoría no encontrada con ID: {}", id);
        }
        return category;
    }
    
    @GetMapping("/name/{name}")
    public Category getCategoryByName(@PathVariable String name) {
        logger.info("Endpoint tradicional: Obteniendo categoría por nombre: {}", name);
        Category category = categoryRepository.findByName(name).orElse(null);
        if (category != null) {
            logger.info("Categoría encontrada con nombre: {}", name);
        } else {
            logger.warn("Categoría no encontrada con nombre: {}", name);
        }
        return category;
    }
}
