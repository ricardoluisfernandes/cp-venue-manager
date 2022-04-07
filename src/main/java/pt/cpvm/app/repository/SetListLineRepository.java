package pt.cpvm.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pt.cpvm.app.domain.SetListLine;

/**
 * Spring Data SQL repository for the SetListLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SetListLineRepository extends JpaRepository<SetListLine, Long> {}
