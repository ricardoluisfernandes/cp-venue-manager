package pt.cpvm.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pt.cpvm.app.domain.Contractor;

/**
 * Spring Data SQL repository for the Contractor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractorRepository extends JpaRepository<Contractor, Long> {}
