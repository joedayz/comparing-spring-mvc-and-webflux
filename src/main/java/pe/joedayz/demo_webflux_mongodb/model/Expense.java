package pe.joedayz.demo_webflux_mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "expenses")
public class Expense {
    
    @Id
    private String id;
    
    @Field("userId")
    private String userId;
    
    @Field("categoryId")
    private String categoryId;
    
    @Field("amount")
    private BigDecimal amount;
    
    @Field("description")
    private String description;
    
    @Field("paymentMethod")
    private PaymentMethod paymentMethod;
    
    @Field("date")
    private LocalDate date;
    
    @Field("createdAt")
    private LocalDateTime createdAt;
    
    @Field("status")
    private ExpenseStatus status;
    
    // Enums
    public enum PaymentMethod {
        CASH, CREDIT_CARD, DEBIT_CARD
    }
    
    public enum ExpenseStatus {
        PENDING, COMPLETED, CANCELLED
    }
    
    // Constructores
    public Expense() {}
    
    public Expense(String userId, String categoryId, BigDecimal amount, 
                  String description, PaymentMethod paymentMethod, LocalDate date) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.paymentMethod = paymentMethod;
        this.date = date;
        this.createdAt = LocalDateTime.now();
        this.status = ExpenseStatus.PENDING;
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
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
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
    
    public ExpenseStatus getStatus() {
        return status;
    }
    
    public void setStatus(ExpenseStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Expense{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", paymentMethod=" + paymentMethod +
                ", date=" + date +
                ", createdAt=" + createdAt +
                ", status=" + status +
                '}';
    }
}
