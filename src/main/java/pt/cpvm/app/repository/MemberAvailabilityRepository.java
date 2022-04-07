package pt.cpvm.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pt.cpvm.app.domain.MemberAvailability;

/**
 * Spring Data SQL repository for the MemberAvailability entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberAvailabilityRepository extends JpaRepository<MemberAvailability, Long> {}
