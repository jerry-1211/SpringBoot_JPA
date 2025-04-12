package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	@Bean
	// hibernate5Modul는 지연 로딩 무시 (LAZY)
	Hibernate5JakartaModule hibernate5Module() {
		Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();


		/**
		 * 이렇게 옵션을 줘서 Lazy 로딩되는 부분을 바로 로딩 시킬 수 있다.
		 * 하지만 이렇게 하이버네이트 모듈 자체를 Lazy 로딩 때문에 등록하는거는 좋지 않음*/
		// hibernate5JakartaModule.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, true);


		return hibernate5JakartaModule;
	}
}
