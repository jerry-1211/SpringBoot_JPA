package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    // 동적 쿼리 문제 (추후에 Querydsl로 해결해야 함)
    public List<Order> findAll(OrderSearch orderSearch){
        return em.createQuery("select o from Order o join o.member m" +
                    " where o.status = :status" +
                    " and m.name like :name", Order.class)
                    .setParameter("status", orderSearch.getOrderStatus())
                    .setParameter("name", orderSearch.getMemberName())
                    .setMaxResults(1000)
                    .getResultList();
    }

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();
    }

    /**
     * fetch join 사용으로 쿼리 1번에 조인 가능해짐
     * findAllWithMemberDelivery는 Order 엔티티를 사용하는데,
     * 순수하게 조회하거나 fetch join을 위한정도 까지는 봐줘야 한다고 볼 수 있다.
     * 이정도는 엔티티의 순수성이 유지
     * */
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch  o.delivery d" , Order.class
            ).getResultList();
    }


    /**
     * fetch join 방식과 비교해서 장단점이 있다.
     * JPA에서 DTO로 바로 조회하는 경우 최적화가 잘 되어 있지만,
     * 한편으로는 재사용성이 많이 떨어진다.
     * 또한, API를 위한 로직이라 볼 수 있어서, 논리적으로 계층이 깨져있다고 볼 수 있다.
     * 때문에 아래의 로직은 repository에 별도로 관리한다.
     * */
//    public List<OrderSimpleQueryDto> findOrderDto() {
//        return  em.createQuery("select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id,m.name,o.orderDate,o.status,d.address)"+
//                        " from Order  o" +
//                        " join o.member m" +
//                        " join o.delivery d", OrderSimpleQueryDto.class)
//                .getResultList();
//    }

}
