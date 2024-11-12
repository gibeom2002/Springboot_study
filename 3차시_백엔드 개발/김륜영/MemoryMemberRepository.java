package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository //스프링 컨테이너에 빈을 생성  이런 것들이 없으면 스프링은 인식하지 못함.
public class MemoryMemberRepository implements MemberRepository { //인터페이스에 있는 함수들을 구현 (Override)

    private static Map<Long, Member> store = new HashMap<>(); //메모리에 member 정보를 임시로 저장 할 맵 선언.
    private static long sequence = 0L;

    @Override
    public Member save(Member member) { //새로운 멤버를 저장하는 함수
        member.setId(++sequence); //sequencd(회원 순서)를 우선 1 증가한 후 새로운 멤버의 Id로 설정한다.
        store.put(member.getId(), member); //메모리에 임시로 만든 저장공간인 store(HashMap)에 멤버Id와 멤버 객체를 추가한다. key: memberId, value: member객체
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) { //현재 멤버들을 id값을 이용해 존재하는지 찾는다.
        return Optional.ofNullable(store.get(id)); //store에서 id를 기반으로 멤버를 찾는다. Optional을 사용했으니 만약 멤버가 존재하지 않는다면 자동으로 null을 반환한다.
    }

    @Override
    public Optional<Member> findByName(String name) { //멤버를 이름으로 찾는 기능이다.
        return store.values().stream() //store맴에 있는 모든 Member 객체를 Collection<Member> 형태로 불러온다. 불러온 Collection들을 스트림 변환하여 연속처리가 가능하게 만듬.
                .filter(member -> member.getName().equals(name)) //각 Member 객체에 대해 getName() 함수를 실행하여 파라미터에 있는 name과 일치하는 객체가 있는지 찾는다.
                .findAny(); //name이 일치하는 객체가 있다면 Optional<Member>에 감싸서 반환한다. 존재하지 않으면 Optional.empty()를 반환한다.

    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); //store 맵에 존재하는 모든 value 들을 어레이리스트 형태로 반환한다.
    }

    public void clearStore() { //store 맵을 초기화 하는 함수
        store.clear();
    }
}
