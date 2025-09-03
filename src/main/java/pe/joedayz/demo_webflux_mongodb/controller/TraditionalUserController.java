package pe.joedayz.demo_webflux_mongodb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pe.joedayz.demo_webflux_mongodb.model.User;
import pe.joedayz.demo_webflux_mongodb.repository.TraditionalUserRepository;
import java.util.List;

@RestController
@RequestMapping("/api/v1/traditional/users")
@CrossOrigin(origins = "*")
public class TraditionalUserController {
    
    private static final Logger logger = LoggerFactory.getLogger(TraditionalUserController.class);
    
    private final TraditionalUserRepository userRepository;
    
    public TraditionalUserController(TraditionalUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping
    public List<User> getAllUsers() {
        logger.info("Endpoint tradicional: Obteniendo todos los usuarios");
        List<User> users = userRepository.findAll();
        logger.info("Todos los usuarios obtenidos exitosamente: {}", users.size());
        return users;
    }
    
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        logger.info("Endpoint tradicional: Obteniendo usuario por ID: {}", id);
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            logger.info("Usuario encontrado con ID: {}", id);
        } else {
            logger.warn("Usuario no encontrado con ID: {}", id);
        }
        return user;
    }
    
    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        logger.info("Endpoint tradicional: Obteniendo usuario por username: {}", username);
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            logger.info("Usuario encontrado con username: {}", username);
        } else {
            logger.warn("Usuario no encontrado con username: {}", username);
        }
        return user;
    }
}
