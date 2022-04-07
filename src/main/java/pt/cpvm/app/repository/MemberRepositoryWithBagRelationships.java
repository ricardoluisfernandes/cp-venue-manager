package pt.cpvm.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import pt.cpvm.app.domain.Member;

public interface MemberRepositoryWithBagRelationships {
    Optional<Member> fetchBagRelationships(Optional<Member> member);

    List<Member> fetchBagRelationships(List<Member> members);

    Page<Member> fetchBagRelationships(Page<Member> members);
}
