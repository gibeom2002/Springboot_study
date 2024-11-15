package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;


//순수 JPA를 이용한 레포지토리 MemberRepository 인터페이스에 있는 기능들은 구현을 해야함. 하지만 기존 방식보다는 훨씬 간단한 편
public class JpaMemberRepository implements MemberRepository {
    private final EntityManager em; //이 엔티티 매니저가 연결 등 모든 관리를 다 해줌.

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member); //알아서 INSERT문 수행 후 ID등 알아서 다 넣어줌
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) { //이런 pk값을 기반으로 찾는 sql문은 em이 알아서 다 해줌
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) { //pk 기반으로 찾는게 아닌 다른 sql문들은 직접 sql문들을 짜줘야함
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class).setParameter("name", name).getResultList();
        return result.stream().findAny();

    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList(); //인라인 함수로 리팩터링 함
                                                                                        //멤버 객체를 매핑 할 필요없이 바로 m에 넣어버림 -> 훨씬 간단!
    }
}
