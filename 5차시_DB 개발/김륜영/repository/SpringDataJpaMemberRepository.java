package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
    //스프링 JPA를 이용한 레포지토리. 구현이 딱히 필요 없는게 장점. 사용하기 간단함. 하지만 순수 JPA를 잘 알지 못하면 안됨.
    @Override
    Optional<Member> findByName(String name);
}
