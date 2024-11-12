package hello.hello_spring.repository;
import hello.hello_spring.domain.Member;
import java.util.Optional;
import java.util.List;
public interface MemberRepository { //구현을 위한 인터페이스 선언
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();
}
