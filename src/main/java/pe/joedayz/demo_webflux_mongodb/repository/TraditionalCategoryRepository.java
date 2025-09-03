package pe.joedayz.demo_webflux_mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pe.joedayz.demo_webflux_mongodb.model.Category;

import java.util.Optional;

@Repository
public interface TraditionalCategoryRepository extends MongoRepository<Category, String> {
    
    Optional<Category> findByName(String name);
}
