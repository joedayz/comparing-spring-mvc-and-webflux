package pe.joedayz.demo_webflux_mongodb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.joedayz.demo_webflux_mongodb.dto.CreateExpenseRequest;
import pe.joedayz.demo_webflux_mongodb.dto.ExpenseResponse;
import pe.joedayz.demo_webflux_mongodb.model.Category;
import pe.joedayz.demo_webflux_mongodb.model.Expense;
import pe.joedayz.demo_webflux_mongodb.model.User;
import pe.joedayz.demo_webflux_mongodb.repository.ReactiveCategoryRepository;
import pe.joedayz.demo_webflux_mongodb.repository.ReactiveExpenseRepository;
import pe.joedayz.demo_webflux_mongodb.repository.ReactiveUserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Service
public class ReactiveExpenseService {
    
    private static final Logger logger = LoggerFactory.getLogger(ReactiveExpenseService.class);
    
    private final ReactiveExpenseRepository expenseRepository;
    private final ReactiveUserRepository userRepository;
    private final ReactiveCategoryRepository categoryRepository;
    
    public ReactiveExpenseService(ReactiveExpenseRepository expenseRepository,
                                ReactiveUserRepository userRepository,
                                ReactiveCategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }
    
    public Mono<ExpenseResponse> createExpense(CreateExpenseRequest request) {
        logger.info("Creando gasto reactivo para usuario: {}", request.getUserId());
        
        return Mono.defer(() -> {
            // Simular procesamiento complejo de validación
            return validateExpenseRequest(request)
                    .then(processPaymentMethod(request))
                    .then(createExpenseEntity(request))
                    .flatMap(expense -> {
                        // Simular operaciones adicionales como notificaciones
                        return sendNotifications(expense)
                                .then(updateUserBalance(expense))
                                .then(Mono.just(expense));
                    })
                    .flatMap(this::enrichExpenseResponse);
        }).subscribeOn(Schedulers.boundedElastic());
    }
    
    public Flux<ExpenseResponse> getAllExpenses() {
        logger.info("Obteniendo todos los gastos de forma reactiva");
        
        return expenseRepository.findAll()
                .flatMap(this::enrichExpenseResponse)
                .delayElements(Duration.ofMillis(10)) // Simular procesamiento
                .doOnNext(expense -> logger.debug("Procesando gasto: {}", expense.getId()));
    }
    
    public Flux<ExpenseResponse> getExpensesByUser(String userId) {
        logger.info("Obteniendo gastos del usuario: {} de forma reactiva", userId);
        
        return expenseRepository.findByUserId(userId)
                .flatMap(this::enrichExpenseResponse)
                .doOnNext(expense -> logger.debug("Procesando gasto del usuario: {}", expense.getId()));
    }
    
    public Flux<ExpenseResponse> getExpensesByPaymentMethod(Expense.PaymentMethod paymentMethod) {
        logger.info("Obteniendo gastos por método de pago: {} de forma reactiva", paymentMethod);
        
        return expenseRepository.findByPaymentMethod(paymentMethod)
                .flatMap(this::enrichExpenseResponse)
                .doOnNext(expense -> logger.debug("Procesando gasto con método: {}", expense.getPaymentMethod()));
    }
    
    public Mono<ExpenseResponse> getExpenseById(String id) {
        logger.info("Obteniendo gasto por ID: {} de forma reactiva", id);
        
        return expenseRepository.findById(id)
                .flatMap(this::enrichExpenseResponse)
                .doOnNext(expense -> logger.debug("Gasto encontrado: {}", expense.getId()));
    }
    
    public Mono<BigDecimal> getTotalExpensesByUser(String userId) {
        logger.info("Calculando total de gastos del usuario: {} de forma reactiva", userId);
        
        return expenseRepository.findByUserId(userId)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doOnNext(total -> logger.info("Total de gastos del usuario {}: {}", userId, total));
    }
    
    public Mono<Long> getExpenseCountByPaymentMethod(Expense.PaymentMethod paymentMethod) {
        logger.info("Contando gastos por método de pago: {} de forma reactiva", paymentMethod);
        
        return expenseRepository.countByPaymentMethod(paymentMethod)
                .doOnNext(count -> logger.info("Total de gastos con {}: {}", paymentMethod, count));
    }
    
    // Métodos privados con lógica de negocio compleja
    
    private Mono<Void> validateExpenseRequest(CreateExpenseRequest request) {
        return Mono.defer(() -> {
            logger.debug("Validando solicitud de gasto para usuario: {}", request.getUserId());
            
            // Simular validaciones complejas
            if (request.getAmount().compareTo(BigDecimal.valueOf(10000)) > 0) {
                return Mono.error(new RuntimeException("Monto excede el límite permitido"));
            }
            
            // Simular validación de usuario
            return userRepository.findById(request.getUserId())
                    .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")))
                    .then();
        });
    }
    
    private Mono<Void> processPaymentMethod(CreateExpenseRequest request) {
        return Mono.defer(() -> {
            logger.debug("Procesando método de pago: {}", request.getPaymentMethod());
            
            // Simular lógica de procesamiento según el método de pago
            switch (request.getPaymentMethod()) {
                case CREDIT_CARD:
                    return simulateCreditCardProcessing(request.getAmount());
                case DEBIT_CARD:
                    return simulateDebitCardProcessing(request.getAmount());
                case CASH:
                    return simulateCashProcessing(request.getAmount());
                default:
                    return Mono.error(new RuntimeException("Método de pago no válido"));
            }
        });
    }
    
    private Mono<Void> simulateCreditCardProcessing(BigDecimal amount) {
        return Mono.delay(Duration.ofMillis(200)) // Simular procesamiento de tarjeta de crédito
                .then(Mono.defer(() -> {
                    logger.debug("Procesando tarjeta de crédito por monto: {}", amount);
                    return Mono.empty();
                }));
    }
    
    private Mono<Void> simulateDebitCardProcessing(BigDecimal amount) {
        return Mono.delay(Duration.ofMillis(150)) // Simular procesamiento de tarjeta de débito
                .then(Mono.defer(() -> {
                    logger.debug("Procesando tarjeta de débito por monto: {}", amount);
                    return Mono.empty();
                }));
    }
    
    private Mono<Void> simulateCashProcessing(BigDecimal amount) {
        return Mono.delay(Duration.ofMillis(50)) // Simular procesamiento en efectivo
                .then(Mono.defer(() -> {
                    logger.debug("Procesando pago en efectivo por monto: {}", amount);
                    return Mono.empty();
                }));
    }
    
    private Mono<Expense> createExpenseEntity(CreateExpenseRequest request) {
        Expense expense = new Expense(
                request.getUserId(),
                request.getCategoryId(),
                request.getAmount(),
                request.getDescription(),
                request.getPaymentMethod(),
                request.getDate()
        );
        
        return expenseRepository.save(expense)
                .doOnNext(savedExpense -> logger.info("Gasto creado con ID: {}", savedExpense.getId()));
    }
    
    private Mono<Void> sendNotifications(Expense expense) {
        return Mono.defer(() -> {
            logger.debug("Enviando notificaciones para gasto: {}", expense.getId());
            
            // Simular envío de notificaciones
            return Mono.delay(Duration.ofMillis(100))
                    .then(Mono.empty());
        });
    }
    
    private Mono<Void> updateUserBalance(Expense expense) {
        return userRepository.findById(expense.getUserId())
                .flatMap(user -> {
                    BigDecimal newBalance = user.getBalance().subtract(expense.getAmount());
                    user.setBalance(newBalance);
                    return userRepository.save(user);
                })
                .then();
    }
    
    private Mono<ExpenseResponse> enrichExpenseResponse(Expense expense) {
        return Mono.zip(
                userRepository.findById(expense.getUserId()),
                categoryRepository.findById(expense.getCategoryId())
        ).map(tuple -> {
            User user = tuple.getT1();
            Category category = tuple.getT2();
            return new ExpenseResponse(expense, user.getFullName(), category.getName());
        });
    }
}
