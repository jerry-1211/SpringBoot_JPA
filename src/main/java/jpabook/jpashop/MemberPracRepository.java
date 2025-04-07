package jpabook.jpashop;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberPracRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(MemberPrac member){
        em.persist(member);
        return  member.getId();
    }

    public MemberPrac find(Long id){
        return em.find(MemberPrac.class, id);
    }
}
