package pe.joedayz.demo_webflux_mongodb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.joedayz.demo_webflux_mongodb.dto.CreateExpenseRequest;
import pe.joedayz.demo_webflux_mongodb.dto.ExpenseResponse;
import pe.joedayz.demo_webflux_mongodb.model.Expense;
import pe.joedayz.demo_webflux_mongodb.service.ReactiveExpenseService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/reactive/expenses")
@CrossOrigin(origins = "*")
public class ReactiveExpenseController {
    
    private static final Logger logger = LoggerFactory.getLogger(ReactiveExpenseController.class);
    
    private final ReactiveExpenseService expenseService;
    
    public ReactiveExpenseController(ReactiveExpenseService expenseService) {
        this.expenseService = expenseService;
    }
    
    @PostMapping
    public Mono<ResponseEntity<ExpenseResponse>> createExpense(@Valid @RequestBody CreateExpenseRequest request) {
        logger.info("Endpoint reactivo: Creando gasto para usuario: {}", request.getUserId());
        
        return expenseService.createExpense(request)
                .map(expense -> ResponseEntity.status(HttpStatus.CREATED).body(expense))
                .doOnSuccess(response -> logger.info("Gasto reactivo creado exitosamente"))
                .doOnError(error -> logger.error("Error al crear gasto reactivo: {}", error.getMessage()));
    }
    
    @GetMapping
    public Flux<ExpenseResponse> getAllExpenses() {
        logger.info("Endpoint reactivo: Obteniendo todos los gastos");
        
        return expenseService.getAllExpenses()
                .doOnComplete(() -> logger.info("Todos los gastos reactivos obtenidos exitosamente"))
                .doOnError(error -> logger.error("Error al obtener gastos reactivos: {}", error.getMessage()));
    }
    
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ExpenseResponse>> getExpenseById(@PathVariable String id) {
        logger.info("Endpoint reactivo: Obteniendo gasto por ID: {}", id);
        
        return expenseService.getExpenseById(id)
                .map(expense -> ResponseEntity.ok(expense))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .doOnSuccess(response -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        logger.info("Gasto reactivo encontrado con ID: {}", id);
                    } else {
                        logger.warn("Gasto reactivo no encontrado con ID: {}", id);
                    }
                })
                .doOnError(error -> logger.error("Error al obtener gasto reactivo con ID {}: {}", id, error.getMessage()));
    }
    
    @GetMapping("/user/{userId}")
    public Flux<ExpenseResponse> getExpensesByUser(@PathVariable String userId) {
        logger.info("Endpoint reactivo: Obteniendo gastos del usuario: {}", userId);
        
        return expenseService.getExpensesByUser(userId)
                .doOnComplete(() -> logger.info("Gastos reactivos del usuario {} obtenidos exitosamente", userId))
                .doOnError(error -> logger.error("Error al obtener gastos reactivos del usuario {}: {}", userId, error.getMessage()));
    }
    
    @GetMapping("/payment-method/{paymentMethod}")
    public Flux<ExpenseResponse> getExpensesByPaymentMethod(@PathVariable Expense.PaymentMethod paymentMethod) {
        logger.info("Endpoint reactivo: Obteniendo gastos por método de pago: {}", paymentMethod);
        
        return expenseService.getExpensesByPaymentMethod(paymentMethod)
                .doOnComplete(() -> logger.info("Gastos reactivos con método de pago {} obtenidos exitosamente", paymentMethod))
                .doOnError(error -> logger.error("Error al obtener gastos reactivos con método de pago {}: {}", paymentMethod, error.getMessage()));
    }
    
    @GetMapping("/user/{userId}/total")
    public Mono<ResponseEntity<BigDecimal>> getTotalExpensesByUser(@PathVariable String userId) {
        logger.info("Endpoint reactivo: Calculando total de gastos del usuario: {}", userId);
        
        return expenseService.getTotalExpensesByUser(userId)
                .map(total -> ResponseEntity.ok(total))
                .doOnSuccess(response -> logger.info("Total de gastos reactivos del usuario {}: {}", userId, response.getBody()))
                .doOnError(error -> logger.error("Error al calcular total de gastos reactivos del usuario {}: {}", userId, error.getMessage()));
    }
    
    @GetMapping("/payment-method/{paymentMethod}/count")
    public Mono<ResponseEntity<Long>> getExpenseCountByPaymentMethod(@PathVariable Expense.PaymentMethod paymentMethod) {
        logger.info("Endpoint reactivo: Contando gastos por método de pago: {}", paymentMethod);
        
        return expenseService.getExpenseCountByPaymentMethod(paymentMethod)
                .map(count -> ResponseEntity.ok(count))
                .doOnSuccess(response -> logger.info("Total de gastos reactivos con método de pago {}: {}", paymentMethod, response.getBody()))
                .doOnError(error -> logger.error("Error al contar gastos reactivos con método de pago {}: {}", paymentMethod, error.getMessage()));
    }
    
    @GetMapping("/health")
    public Mono<ResponseEntity<String>> health() {
        logger.info("Endpoint reactivo: Verificando salud del servicio");
        
        return Mono.just("Reactive Expense Service is running!")
                .map(message -> ResponseEntity.ok(message))
                .doOnSuccess(response -> logger.info("Servicio reactivo de gastos funcionando correctamente"));
    }
}
