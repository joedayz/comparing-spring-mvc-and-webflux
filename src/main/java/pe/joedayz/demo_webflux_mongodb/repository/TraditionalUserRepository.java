package pe.joedayz.demo_webflux_mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pe.joedayz.demo_webflux_mongodb.model.User;

import java.util.Optional;

@Repository
public interface TraditionalUserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
}
