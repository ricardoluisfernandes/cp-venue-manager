package pt.cpvm.app.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import pt.cpvm.app.domain.Member;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class MemberRepositoryWithBagRelationshipsImpl implements MemberRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Member> fetchBagRelationships(Optional<Member> member) {
        return member.map(this::fetchVenues);
    }

    @Override
    public Page<Member> fetchBagRelationships(Page<Member> members) {
        return new PageImpl<>(fetchBagRelationships(members.getContent()), members.getPageable(), members.getTotalElements());
    }

    @Override
    public List<Member> fetchBagRelationships(List<Member> members) {
        return Optional.of(members).map(this::fetchVenues).orElse(Collections.emptyList());
    }

    Member fetchVenues(Member result) {
        return entityManager
            .createQuery("select member from Member member left join fetch member.venues where member is :member", Member.class)
            .setParameter("member", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Member> fetchVenues(List<Member> members) {
        return entityManager
            .createQuery("select distinct member from Member member left join fetch member.venues where member in :members", Member.class)
            .setParameter("members", members)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
