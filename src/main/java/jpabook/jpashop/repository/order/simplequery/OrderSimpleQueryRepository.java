package jpabook.jpashop.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    /**
     * repository는 가급적이면 순수한 엔티티 조회하는데만 쓰인다.
     * 조회, 검색, 필요하면 성능 최적화를 위한 fetch join 정도까지만
     * 아래의 복잡한 쿼리 작성하는 것도 꽤나 어려운 부분이다.
     * */

    public List<OrderSimpleQueryDto> findOrderDto() {
        return  em.createQuery("select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id,m.name,o.orderDate,o.status,d.address)"+
                        " from Order  o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }

}
