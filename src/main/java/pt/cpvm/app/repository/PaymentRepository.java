package pt.cpvm.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pt.cpvm.app.domain.Payment;

/**
 * Spring Data SQL repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {}
