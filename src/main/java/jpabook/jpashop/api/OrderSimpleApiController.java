package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class  OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for(Order order: all){
            /**
             * order.getMember() 여기까지는 프록시 객체
             * .getName(); 이 부분 부처 Lazy 강제 초기화
             * */
            order.getMember().getName();


            /**
             * order.getDelivery() 여기까지는 프록시 객체
             * .getAddress(); 이 부분 부처 Lazy 강제 초기화
             * */
            order.getDelivery().getAddress();
        }
        return all;
    }


    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        /**
         * Order의 개수 조회 (Order 쿼리 1번 호출)
         * */
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());

        /**
         * Order의 개수만큼 반복
         * 반복하면서 내부의 Member, Delivery 쿼리 1번씩 호출
         * 이 부분에서 Order 개수 * (Member 쿼리 + Delivery 쿼라) 만큼 쿼리문 호출
         * 즉 , N+1 문제 발생  (1+N 이라고 하는게 좀 더 직관적)
         * */
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @Data
    static class SimpleOrderDto{
        /**
         * DTO가 이렇게 엔티티를 파라미터로 받는 것은 크게 문제되지 않는다.
         * 큰 영향력 없는 애가 영향력있는 애한테 의존하는 것은 큰 문제 되지 않음.
         * */
        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // Lazy 초기화 (Member 조회)
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //Lazy 초기화 (Delivey 조회)
        }
        
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
    }

}
