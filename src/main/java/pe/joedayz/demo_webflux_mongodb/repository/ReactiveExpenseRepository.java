package pe.joedayz.demo_webflux_mongodb.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pe.joedayz.demo_webflux_mongodb.model.Expense;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveExpenseRepository extends ReactiveMongoRepository<Expense, String> {
    
    Flux<Expense> findByUserId(String userId);
    
    Flux<Expense> findByPaymentMethod(Expense.PaymentMethod paymentMethod);
    
    Flux<Expense> findByCategoryId(String categoryId);
    
    Flux<Expense> findByUserIdAndPaymentMethod(String userId, Expense.PaymentMethod paymentMethod);
    
    Mono<Long> countByUserId(String userId);
    
    Mono<Long> countByPaymentMethod(Expense.PaymentMethod paymentMethod);
}
