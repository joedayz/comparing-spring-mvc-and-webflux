package pe.joedayz.demo_webflux_mongodb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pe.joedayz.demo_webflux_mongodb.model.User;
import pe.joedayz.demo_webflux_mongodb.repository.ReactiveUserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/reactive/users")
@CrossOrigin(origins = "*")
public class ReactiveUserController {
    
    private static final Logger logger = LoggerFactory.getLogger(ReactiveUserController.class);
    
    private final ReactiveUserRepository userRepository;
    
    public ReactiveUserController(ReactiveUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping
    public Flux<User> getAllUsers() {
        logger.info("Endpoint reactivo: Obteniendo todos los usuarios");
        return userRepository.findAll()
                .doOnComplete(() -> logger.info("Todos los usuarios obtenidos exitosamente"))
                .doOnError(error -> logger.error("Error al obtener usuarios: {}", error.getMessage()));
    }
    
    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable String id) {
        logger.info("Endpoint reactivo: Obteniendo usuario por ID: {}", id);
        return userRepository.findById(id)
                .doOnSuccess(user -> logger.info("Usuario encontrado con ID: {}", id))
                .doOnError(error -> logger.error("Error al obtener usuario con ID {}: {}", id, error.getMessage()));
    }
    
    @GetMapping("/username/{username}")
    public Mono<User> getUserByUsername(@PathVariable String username) {
        logger.info("Endpoint reactivo: Obteniendo usuario por username: {}", username);
        return userRepository.findByUsername(username)
                .doOnSuccess(user -> logger.info("Usuario encontrado con username: {}", username))
                .doOnError(error -> logger.error("Error al obtener usuario con username {}: {}", username, error.getMessage()));
    }
}
