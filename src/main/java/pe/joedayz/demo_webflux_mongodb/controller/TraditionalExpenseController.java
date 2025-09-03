package pe.joedayz.demo_webflux_mongodb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.joedayz.demo_webflux_mongodb.dto.CreateExpenseRequest;
import pe.joedayz.demo_webflux_mongodb.dto.ExpenseResponse;
import pe.joedayz.demo_webflux_mongodb.model.Expense;
import pe.joedayz.demo_webflux_mongodb.service.TraditionalExpenseService;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/traditional/expenses")
@CrossOrigin(origins = "*")
public class TraditionalExpenseController {
    
    private static final Logger logger = LoggerFactory.getLogger(TraditionalExpenseController.class);
    
    private final TraditionalExpenseService expenseService;
    
    public TraditionalExpenseController(TraditionalExpenseService expenseService) {
        this.expenseService = expenseService;
    }
    
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody CreateExpenseRequest request) {
        logger.info("Endpoint tradicional: Creando gasto para usuario: {}", request.getUserId());
        
        try {
            ExpenseResponse expense = expenseService.createExpense(request);
            logger.info("Gasto tradicional creado exitosamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(expense);
        } catch (Exception e) {
            logger.error("Error al crear gasto tradicional: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        logger.info("Endpoint tradicional: Obteniendo todos los gastos");
        
        try {
            List<ExpenseResponse> expenses = expenseService.getAllExpenses();
            logger.info("Todos los gastos tradicionales obtenidos exitosamente");
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            logger.error("Error al obtener gastos tradicionales: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable String id) {
        logger.info("Endpoint tradicional: Obteniendo gasto por ID: {}", id);
        
        try {
            ExpenseResponse expense = expenseService.getExpenseById(id);
            logger.info("Gasto tradicional encontrado con ID: {}", id);
            return ResponseEntity.ok(expense);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                logger.warn("Gasto tradicional no encontrado con ID: {}", id);
                return ResponseEntity.notFound().build();
            } else {
                logger.error("Error al obtener gasto tradicional con ID {}: {}", id, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        } catch (Exception e) {
            logger.error("Error inesperado al obtener gasto tradicional con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUser(@PathVariable String userId) {
        logger.info("Endpoint tradicional: Obteniendo gastos del usuario: {}", userId);
        
        try {
            List<ExpenseResponse> expenses = expenseService.getExpensesByUser(userId);
            logger.info("Gastos tradicionales del usuario {} obtenidos exitosamente", userId);
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            logger.error("Error al obtener gastos tradicionales del usuario {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    @GetMapping("/payment-method/{paymentMethod}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByPaymentMethod(@PathVariable Expense.PaymentMethod paymentMethod) {
        logger.info("Endpoint tradicional: Obteniendo gastos por método de pago: {}", paymentMethod);
        
        try {
            List<ExpenseResponse> expenses = expenseService.getExpensesByPaymentMethod(paymentMethod);
            logger.info("Gastos tradicionales con método de pago {} obtenidos exitosamente", paymentMethod);
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            logger.error("Error al obtener gastos tradicionales con método de pago {}: {}", paymentMethod, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    @GetMapping("/user/{userId}/total")
    public ResponseEntity<BigDecimal> getTotalExpensesByUser(@PathVariable String userId) {
        logger.info("Endpoint tradicional: Calculando total de gastos del usuario: {}", userId);
        
        try {
            BigDecimal total = expenseService.getTotalExpensesByUser(userId);
            logger.info("Total de gastos tradicionales del usuario {}: {}", userId, total);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            logger.error("Error al calcular total de gastos tradicionales del usuario {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    @GetMapping("/payment-method/{paymentMethod}/count")
    public ResponseEntity<Long> getExpenseCountByPaymentMethod(@PathVariable Expense.PaymentMethod paymentMethod) {
        logger.info("Endpoint tradicional: Contando gastos por método de pago: {}", paymentMethod);
        
        try {
            long count = expenseService.getExpenseCountByPaymentMethod(paymentMethod);
            logger.info("Total de gastos tradicionales con método de pago {}: {}", paymentMethod, count);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Error al contar gastos tradicionales con método de pago {}: {}", paymentMethod, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        logger.info("Endpoint tradicional: Verificando salud del servicio");
        
        try {
            String message = "Traditional Expense Service is running!";
            logger.info("Servicio tradicional de gastos funcionando correctamente");
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            logger.error("Error en el servicio tradicional: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Service Error");
        }
    }
}
