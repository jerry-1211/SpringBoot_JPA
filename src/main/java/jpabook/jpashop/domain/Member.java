package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

//    @NotEmpty  (이렇게 직접 엔티티를 건드리면 나중에 큰 문제 발생)
      /**
       * API와 분리하면 해당 값이 사라니거나 변경되어도 스펙이 변하지는 않음
       * 때문에, API는 절대 도메인을 건드리지 말고, DTO로 데이터 처리*/

    private String name;

    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
