package pt.cpvm.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pt.cpvm.app.domain.SetList;

/**
 * Spring Data SQL repository for the SetList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SetListRepository extends JpaRepository<SetList, Long> {}
