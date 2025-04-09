package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;



@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        // given
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);

        // when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        System.out.println(orderId);

        //them
        Order getOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        Assertions.assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        Assertions.assertEquals(10000*orderCount, getOrder.getTotalPrice(), "주문한 가격은 개당 가격 * 수량이다.");
        Assertions.assertEquals(8, book.getStockQuantity(),"주문 수량만큼 재고가 줄어야 한다.");
    }

    @Test
    public void  주문취소() throws Exception{
        // given
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        Assertions.assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주민 취소 상태는 CANCEL");
        Assertions.assertEquals(10, book.getStockQuantity(),"주문 취소 상품의 재고가 그대로야 한다.");

    }


    @Test
    public void 상품주문_재고수량초과() throws Exception{
        // given
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);

        // when
        int orderCount = 20;

        //then
        Assertions.assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), book.getId(), orderCount);
        });

    }

    private Book createBook(String jpa, int price, int stockQuantity) {
        // command + option + p로 인자 추출해서 매개변수로 만들어줌 ;;
        Book book = new Book();
        book.setName(jpa);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "동작구","123"));
        em.persist(member);
        return member;
    }


}