package pe.joedayz.demo_webflux_mongodb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.joedayz.demo_webflux_mongodb.dto.CreateExpenseRequest;
import pe.joedayz.demo_webflux_mongodb.dto.ExpenseResponse;
import pe.joedayz.demo_webflux_mongodb.model.Category;
import pe.joedayz.demo_webflux_mongodb.model.Expense;
import pe.joedayz.demo_webflux_mongodb.model.User;
import pe.joedayz.demo_webflux_mongodb.repository.TraditionalCategoryRepository;
import pe.joedayz.demo_webflux_mongodb.repository.TraditionalExpenseRepository;
import pe.joedayz.demo_webflux_mongodb.repository.TraditionalUserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TraditionalExpenseService {
    
    private static final Logger logger = LoggerFactory.getLogger(TraditionalExpenseService.class);
    
    private final TraditionalExpenseRepository expenseRepository;
    private final TraditionalUserRepository userRepository;
    private final TraditionalCategoryRepository categoryRepository;
    
    public TraditionalExpenseService(TraditionalExpenseRepository expenseRepository,
                                   TraditionalUserRepository userRepository,
                                   TraditionalCategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }
    
    public ExpenseResponse createExpense(CreateExpenseRequest request) {
        logger.info("Creando gasto tradicional para usuario: {}", request.getUserId());
        
        try {
            // Simular procesamiento complejo de validación
            validateExpenseRequest(request);
            processPaymentMethod(request);
            
            Expense expense = createExpenseEntity(request);
            
            // Simular operaciones adicionales como notificaciones
            sendNotifications(expense);
            updateUserBalance(expense);
            
            return enrichExpenseResponse(expense);
            
        } catch (Exception e) {
            logger.error("Error al crear gasto tradicional: {}", e.getMessage());
            throw new RuntimeException("Error al crear gasto: " + e.getMessage());
        }
    }
    
    public List<ExpenseResponse> getAllExpenses() {
        logger.info("Obteniendo todos los gastos de forma tradicional");
        
        try {
            List<Expense> expenses = expenseRepository.findAll();
            
            // Simular procesamiento secuencial
            return expenses.stream()
                    .map(this::enrichExpenseResponse)
                    .peek(expense -> {
                        try {
                            Thread.sleep(10); // Simular procesamiento
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    })
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            logger.error("Error al obtener gastos tradicionales: {}", e.getMessage());
            throw new RuntimeException("Error al obtener gastos: " + e.getMessage());
        }
    }
    
    public List<ExpenseResponse> getExpensesByUser(String userId) {
        logger.info("Obteniendo gastos del usuario: {} de forma tradicional", userId);
        
        try {
            List<Expense> expenses = expenseRepository.findByUserId(userId);
            return expenses.stream()
                    .map(this::enrichExpenseResponse)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            logger.error("Error al obtener gastos del usuario {}: {}", userId, e.getMessage());
            throw new RuntimeException("Error al obtener gastos del usuario: " + e.getMessage());
        }
    }
    
    public List<ExpenseResponse> getExpensesByPaymentMethod(Expense.PaymentMethod paymentMethod) {
        logger.info("Obteniendo gastos por método de pago: {} de forma tradicional", paymentMethod);
        
        try {
            List<Expense> expenses = expenseRepository.findByPaymentMethod(paymentMethod);
            return expenses.stream()
                    .map(this::enrichExpenseResponse)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            logger.error("Error al obtener gastos por método de pago {}: {}", paymentMethod, e.getMessage());
            throw new RuntimeException("Error al obtener gastos por método de pago: " + e.getMessage());
        }
    }
    
    public ExpenseResponse getExpenseById(String id) {
        logger.info("Obteniendo gasto por ID: {} de forma tradicional", id);
        
        try {
            Optional<Expense> expenseOpt = expenseRepository.findById(id);
            if (expenseOpt.isPresent()) {
                return enrichExpenseResponse(expenseOpt.get());
            } else {
                throw new RuntimeException("Gasto no encontrado con ID: " + id);
            }
        } catch (Exception e) {
            logger.error("Error al obtener gasto con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al obtener gasto: " + e.getMessage());
        }
    }
    
    public BigDecimal getTotalExpensesByUser(String userId) {
        logger.info("Calculando total de gastos del usuario: {} de forma tradicional", userId);
        
        try {
            List<Expense> expenses = expenseRepository.findByUserId(userId);
            BigDecimal total = expenses.stream()
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            logger.info("Total de gastos del usuario {}: {}", userId, total);
            return total;
            
        } catch (Exception e) {
            logger.error("Error al calcular total de gastos del usuario {}: {}", userId, e.getMessage());
            throw new RuntimeException("Error al calcular total de gastos: " + e.getMessage());
        }
    }
    
    public long getExpenseCountByPaymentMethod(Expense.PaymentMethod paymentMethod) {
        logger.info("Contando gastos por método de pago: {} de forma tradicional", paymentMethod);
        
        try {
            long count = expenseRepository.countByPaymentMethod(paymentMethod);
            logger.info("Total de gastos con {}: {}", paymentMethod, count);
            return count;
            
        } catch (Exception e) {
            logger.error("Error al contar gastos por método de pago {}: {}", paymentMethod, e.getMessage());
            throw new RuntimeException("Error al contar gastos por método de pago: " + e.getMessage());
        }
    }
    
    // Métodos privados con lógica de negocio compleja
    
    private void validateExpenseRequest(CreateExpenseRequest request) {
        logger.debug("Validando solicitud de gasto para usuario: {}", request.getUserId());
        
        // Simular validaciones complejas
        if (request.getAmount().compareTo(BigDecimal.valueOf(10000)) > 0) {
            throw new RuntimeException("Monto excede el límite permitido");
        }
        
        // Simular validación de usuario
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
    }
    
    private void processPaymentMethod(CreateExpenseRequest request) {
        logger.debug("Procesando método de pago: {}", request.getPaymentMethod());
        
        // Simular lógica de procesamiento según el método de pago
        switch (request.getPaymentMethod()) {
            case CREDIT_CARD:
                simulateCreditCardProcessing(request.getAmount());
                break;
            case DEBIT_CARD:
                simulateDebitCardProcessing(request.getAmount());
                break;
            case CASH:
                simulateCashProcessing(request.getAmount());
                break;
            default:
                throw new RuntimeException("Método de pago no válido");
        }
    }
    
    private void simulateCreditCardProcessing(BigDecimal amount) {
        try {
            Thread.sleep(200); // Simular procesamiento de tarjeta de crédito
            logger.debug("Procesando tarjeta de crédito por monto: {}", amount);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error en procesamiento de tarjeta de crédito");
        }
    }
    
    private void simulateDebitCardProcessing(BigDecimal amount) {
        try {
            Thread.sleep(150); // Simular procesamiento de tarjeta de débito
            logger.debug("Procesando tarjeta de débito por monto: {}", amount);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error en procesamiento de tarjeta de débito");
        }
    }
    
    private void simulateCashProcessing(BigDecimal amount) {
        try {
            Thread.sleep(50); // Simular procesamiento en efectivo
            logger.debug("Procesando pago en efectivo por monto: {}", amount);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error en procesamiento en efectivo");
        }
    }
    
    private Expense createExpenseEntity(CreateExpenseRequest request) {
        Expense expense = new Expense(
                request.getUserId(),
                request.getCategoryId(),
                request.getAmount(),
                request.getDescription(),
                request.getPaymentMethod(),
                request.getDate()
        );
        
        Expense savedExpense = expenseRepository.save(expense);
        logger.info("Gasto creado con ID: {}", savedExpense.getId());
        return savedExpense;
    }
    
    private void sendNotifications(Expense expense) {
        logger.debug("Enviando notificaciones para gasto: {}", expense.getId());
        
        try {
            Thread.sleep(100); // Simular envío de notificaciones
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Interrupción en envío de notificaciones");
        }
    }
    
    private void updateUserBalance(Expense expense) {
        Optional<User> userOpt = userRepository.findById(expense.getUserId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            BigDecimal newBalance = user.getBalance().subtract(expense.getAmount());
            user.setBalance(newBalance);
            userRepository.save(user);
        }
    }
    
    private ExpenseResponse enrichExpenseResponse(Expense expense) {
        Optional<User> userOpt = userRepository.findById(expense.getUserId());
        Optional<Category> categoryOpt = categoryRepository.findById(expense.getCategoryId());
        
        if (userOpt.isPresent() && categoryOpt.isPresent()) {
            User user = userOpt.get();
            Category category = categoryOpt.get();
            return new ExpenseResponse(expense, user.getFullName(), category.getName());
        } else {
            throw new RuntimeException("Error al enriquecer respuesta del gasto");
        }
    }
}
