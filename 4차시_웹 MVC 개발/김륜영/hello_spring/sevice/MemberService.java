package hello.hello_spring.sevice;


import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service //스프링 컨트롤러에 빈을 생성  이런 것들이 없으면 스프링은 인식하지 못함.
public class MemberService {
//    private final MemberRepository memberRepository = new
//            MemoryMemberRepository();
    private final MemberRepository memberRepository;

    @Autowired //MemberService를 생성할 떄 스프링 컨테이너에서 MemberRepository를 불러옴. (AutoWired를 이용해 자동 연결)
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    /**
     *
     * 회원가입
     */
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }
    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
