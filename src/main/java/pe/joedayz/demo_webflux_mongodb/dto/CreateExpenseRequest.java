package pe.joedayz.demo_webflux_mongodb.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.joedayz.demo_webflux_mongodb.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateExpenseRequest {
    
    @NotBlank(message = "El userId es requerido")
    private String userId;
    
    @NotBlank(message = "El categoryId es requerido")
    private String categoryId;
    
    @NotNull(message = "El monto es requerido")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal amount;
    
    @NotBlank(message = "La descripción es requerida")
    private String description;
    
    @NotNull(message = "El método de pago es requerido")
    private Expense.PaymentMethod paymentMethod;
    
    @NotNull(message = "La fecha es requerida")
    private LocalDate date;
    
    // Constructores
    public CreateExpenseRequest() {}
    
    public CreateExpenseRequest(String userId, String categoryId, BigDecimal amount, 
                              String description, Expense.PaymentMethod paymentMethod, LocalDate date) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.paymentMethod = paymentMethod;
        this.date = date;
    }
    
    // Getters y Setters
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Expense.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(Expense.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
