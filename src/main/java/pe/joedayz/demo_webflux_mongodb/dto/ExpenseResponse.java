package pe.joedayz.demo_webflux_mongodb.dto;

import pe.joedayz.demo_webflux_mongodb.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseResponse {
    
    private String id;
    private String userId;
    private String userName;
    private String categoryId;
    private String categoryName;
    private BigDecimal amount;
    private String description;
    private Expense.PaymentMethod paymentMethod;
    private LocalDate date;
    private LocalDateTime createdAt;
    private Expense.ExpenseStatus status;
    
    // Constructores
    public ExpenseResponse() {}
    
    public ExpenseResponse(Expense expense, String userName, String categoryName) {
        this.id = expense.getId();
        this.userId = expense.getUserId();
        this.userName = userName;
        this.categoryId = expense.getCategoryId();
        this.categoryName = categoryName;
        this.amount = expense.getAmount();
        this.description = expense.getDescription();
        this.paymentMethod = expense.getPaymentMethod();
        this.date = expense.getDate();
        this.createdAt = expense.getCreatedAt();
        this.status = expense.getStatus();
    }
    
    // Getters y Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Expense.ExpenseStatus getStatus() {
        return status;
    }
    
    public void setStatus(Expense.ExpenseStatus status) {
        this.status = status;
    }
}
