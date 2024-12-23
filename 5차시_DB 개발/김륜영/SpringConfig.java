package hello.hello_spring;

import hello.hello_spring.repository.*;
import hello.hello_spring.sevice.MemberService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

//    private DataSource dataSource;
//
//    @Autowired
//    public SpringConfig(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

//    EntityManager em;
//
//    @Autowired
//    public SpringConfig(EntityManager em) {
//        this.em = em;
//    }

    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean //여기에 바로 멤버리포지토리 넣어주면 됨
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }
    //이 부분에는 사용한 레포지토리에맞게 Bean을 생성하면 됨. 단 스프링 jpa는 빈을 생성 할 필요가 없음
//    @Bean
//    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//        return new JdbcMemberRepository(dataSource);
//        return new JdbcTemplateMemberRepository(dataSource );
//        return new JpaMemberRepository(em);
        
    //}

}
