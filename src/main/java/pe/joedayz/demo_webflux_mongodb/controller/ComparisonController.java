package pe.joedayz.demo_webflux_mongodb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.joedayz.demo_webflux_mongodb.dto.CreateExpenseRequest;
import pe.joedayz.demo_webflux_mongodb.dto.ExpenseResponse;
import pe.joedayz.demo_webflux_mongodb.model.Expense;
import pe.joedayz.demo_webflux_mongodb.service.ReactiveExpenseService;
import pe.joedayz.demo_webflux_mongodb.service.TraditionalExpenseService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/v1/comparison")
@CrossOrigin(origins = "*")
public class ComparisonController {
    
    private static final Logger logger = LoggerFactory.getLogger(ComparisonController.class);
    
    private final ReactiveExpenseService reactiveExpenseService;
    private final TraditionalExpenseService traditionalExpenseService;
    private final ExecutorService executorService;
    
    public ComparisonController(ReactiveExpenseService reactiveExpenseService,
                              TraditionalExpenseService traditionalExpenseService) {
        this.reactiveExpenseService = reactiveExpenseService;
        this.traditionalExpenseService = traditionalExpenseService;
        this.executorService = Executors.newFixedThreadPool(10);
    }
    
    @GetMapping("/performance-test")
    public ResponseEntity<String> runPerformanceTest() {
        logger.info("Iniciando prueba de rendimiento comparativa");
        
        StringBuilder result = new StringBuilder();
        result.append("=== PRUEBA DE RENDIMIENTO WEBFLUX vs SPRING WEB ===\n\n");
        
        // Prueba 1: Crear gastos concurrentemente
        result.append("1. PRUEBA DE CREACIÓN CONCURRENTE DE GASTOS:\n");
        result.append(runConcurrentExpenseCreationTest());
        result.append("\n");
        
        // Prueba 2: Consultas masivas
        result.append("2. PRUEBA DE CONSULTAS MASIVAS:\n");
        result.append(runMassiveQueryTest());
        result.append("\n");
        
        // Prueba 3: Procesamiento de métodos de pago
        result.append("3. PRUEBA DE PROCESAMIENTO DE MÉTODOS DE PAGO:\n");
        result.append(runPaymentMethodProcessingTest());
        result.append("\n");
        
        logger.info("Prueba de rendimiento completada");
        return ResponseEntity.ok(result.toString());
    }
    
    @GetMapping("/stress-test")
    public ResponseEntity<String> runStressTest() {
        logger.info("Iniciando prueba de estrés comparativa");
        
        StringBuilder result = new StringBuilder();
        result.append("=== PRUEBA DE ESTRÉS WEBFLUX vs SPRING WEB ===\n\n");
        
        // Simular carga alta
        int numberOfRequests = 100;
        
        result.append("Simulando ").append(numberOfRequests).append(" requests concurrentes...\n\n");
        
        // WebFlux - Reactivo
        Instant reactiveStart = Instant.now();
        runReactiveStressTest(numberOfRequests);
        Duration reactiveDuration = Duration.between(reactiveStart, Instant.now());
        
        // Spring Web - Tradicional
        Instant traditionalStart = Instant.now();
        runTraditionalStressTest(numberOfRequests);
        Duration traditionalDuration = Duration.between(traditionalStart, Instant.now());
        
        result.append("RESULTADOS:\n");
        result.append("WebFlux (Reactivo): ").append(reactiveDuration.toMillis()).append(" ms\n");
        result.append("Spring Web (Tradicional): ").append(traditionalDuration.toMillis()).append(" ms\n");
        result.append("Diferencia: ").append(traditionalDuration.toMillis() - reactiveDuration.toMillis()).append(" ms\n");
        result.append("WebFlux es ").append(String.format("%.2f", (double) traditionalDuration.toMillis() / reactiveDuration.toMillis())).append("x más rápido\n");
        
        logger.info("Prueba de estrés completada");
        return ResponseEntity.ok(result.toString());
    }
    
    private String runConcurrentExpenseCreationTest() {
        StringBuilder result = new StringBuilder();
        
        // Crear gasto de prueba
        CreateExpenseRequest testRequest = new CreateExpenseRequest(
                "juan.perez", // Usuario existente
                "1", // Categoría existente
                BigDecimal.valueOf(50.00),
                "Gasto de prueba para comparación",
                Expense.PaymentMethod.CASH,
                java.time.LocalDate.now()
        );
        
        // WebFlux - Reactivo
        Instant reactiveStart = Instant.now();
        try {
            reactiveExpenseService.createExpense(testRequest).block();
            Duration reactiveDuration = Duration.between(reactiveStart, Instant.now());
            result.append("WebFlux: ").append(reactiveDuration.toMillis()).append(" ms\n");
        } catch (Exception e) {
            result.append("WebFlux: Error - ").append(e.getMessage()).append("\n");
        }
        
        // Spring Web - Tradicional
        Instant traditionalStart = Instant.now();
        try {
            traditionalExpenseService.createExpense(testRequest);
            Duration traditionalDuration = Duration.between(traditionalStart, Instant.now());
            result.append("Spring Web: ").append(traditionalDuration.toMillis()).append(" ms\n");
        } catch (Exception e) {
            result.append("Spring Web: Error - ").append(e.getMessage()).append("\n");
        }
        
        return result.toString();
    }
    
    private String runMassiveQueryTest() {
        StringBuilder result = new StringBuilder();
        
        // WebFlux - Reactivo
        Instant reactiveStart = Instant.now();
        try {
            reactiveExpenseService.getAllExpenses().collectList().block();
            Duration reactiveDuration = Duration.between(reactiveStart, Instant.now());
            result.append("WebFlux (consulta masiva): ").append(reactiveDuration.toMillis()).append(" ms\n");
        } catch (Exception e) {
            result.append("WebFlux: Error - ").append(e.getMessage()).append("\n");
        }
        
        // Spring Web - Tradicional
        Instant traditionalStart = Instant.now();
        try {
            traditionalExpenseService.getAllExpenses();
            Duration traditionalDuration = Duration.between(traditionalStart, Instant.now());
            result.append("Spring Web (consulta masiva): ").append(traditionalDuration.toMillis()).append(" ms\n");
        } catch (Exception e) {
            result.append("Spring Web: Error - ").append(e.getMessage()).append("\n");
        }
        
        return result.toString();
    }
    
    private String runPaymentMethodProcessingTest() {
        StringBuilder result = new StringBuilder();
        
        // WebFlux - Reactivo
        Instant reactiveStart = Instant.now();
        try {
            reactiveExpenseService.getExpensesByPaymentMethod(Expense.PaymentMethod.CREDIT_CARD).collectList().block();
            Duration reactiveDuration = Duration.between(reactiveStart, Instant.now());
            result.append("WebFlux (procesamiento de pago): ").append(reactiveDuration.toMillis()).append(" ms\n");
        } catch (Exception e) {
            result.append("WebFlux: Error - ").append(e.getMessage()).append("\n");
        }
        
        // Spring Web - Tradicional
        Instant traditionalStart = Instant.now();
        try {
            traditionalExpenseService.getExpensesByPaymentMethod(Expense.PaymentMethod.CREDIT_CARD);
            Duration traditionalDuration = Duration.between(traditionalStart, Instant.now());
            result.append("Spring Web (procesamiento de pago): ").append(traditionalDuration.toMillis()).append(" ms\n");
        } catch (Exception e) {
            result.append("Spring Web: Error - ").append(e.getMessage()).append("\n");
        }
        
        return result.toString();
    }
    
    private void runReactiveStressTest(int numberOfRequests) {
        // WebFlux maneja la concurrencia de forma nativa
        Flux.range(1, numberOfRequests)
                .flatMap(i -> reactiveExpenseService.getAllExpenses().collectList())
                .collectList()
                .block();
    }
    
    private void runTraditionalStressTest(int numberOfRequests) {
        // Spring Web necesita manejo manual de concurrencia
        List<CompletableFuture<Void>> futures = Flux.range(1, numberOfRequests)
                .map(i -> CompletableFuture.runAsync(() -> {
                    try {
                        traditionalExpenseService.getAllExpenses();
                    } catch (Exception e) {
                        logger.error("Error en request tradicional: {}", e.getMessage());
                    }
                }, executorService))
                .collectList()
                .block();
        
        // Esperar a que todos completen
        futures.forEach(CompletableFuture::join);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Comparison Service is running!");
    }
}
