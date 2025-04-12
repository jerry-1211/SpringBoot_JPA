package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        /**
         * repository가 Controller에 의존하면 절대 안된다.
         * findOrderDto에서 OrderSimpleApiController 컨트롤러 내부의 DTO를 의존하려한다.
         * 따라서 SimpleOrderQueryDto는 관련 DTO를 따로 Repository 계층에 넣어준 클래스이다.
         * */

        public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
            this.orderId = orderId;
            this.name = name;
            this.orderDate = orderDate;
            this.orderStatus = orderStatus;
            this.address = address;
        }



}
