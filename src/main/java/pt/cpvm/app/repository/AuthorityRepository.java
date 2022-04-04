package pt.cpvm.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.cpvm.app.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
