package pe.joedayz.demo_webflux_mongodb.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pe.joedayz.demo_webflux_mongodb.model.User;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveUserRepository extends ReactiveMongoRepository<User, String> {
    
    Mono<User> findByUsername(String username);
    
    Mono<User> findByEmail(String email);
}
