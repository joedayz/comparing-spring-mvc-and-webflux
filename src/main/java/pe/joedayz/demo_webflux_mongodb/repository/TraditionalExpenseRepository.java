package pe.joedayz.demo_webflux_mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pe.joedayz.demo_webflux_mongodb.model.Expense;

import java.util.List;

@Repository
public interface TraditionalExpenseRepository extends MongoRepository<Expense, String> {
    
    List<Expense> findByUserId(String userId);
    
    List<Expense> findByPaymentMethod(Expense.PaymentMethod paymentMethod);
    
    List<Expense> findByCategoryId(String categoryId);
    
    List<Expense> findByUserIdAndPaymentMethod(String userId, Expense.PaymentMethod paymentMethod);
    
    long countByUserId(String userId);
    
    long countByPaymentMethod(Expense.PaymentMethod paymentMethod);
}
