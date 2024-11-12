package hello.hello_spring.controller;

import hello.hello_spring.domain.Member;
import hello.hello_spring.sevice.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
//서비스를 통해 외부로부터 요청을 받고, 서비스를 로직을 만들고, 리포지토리를 통해 데이터를 저장하는 이 방식은 정형화 된 방식임.

//이 방식은 컴포넌트 자동 인식 방식임. Controller, Serice, Repository를 까보면 안에는 Component로 이루어져 있음.
//또한 인식 범위는 해당 프로젝트의 어플리케이션이 속해있는 패키지의 하위 패키지들을 탐색함. (이번 프로젝트에서는 hello.hello_spring 보다 상위에 있으면 인식을 못함)
//하지만 어떤 설정을 하면 인식시킬 수는 있는데 잘 안씀.


@Controller //스프링 컨테이너에 빈을 생성  이런 것들이 없으면 스프링은 인식하지 못함.
public class MemberController {

//    private final MemberService memberService = new MemberService(); //사용되지 않는 방식. 스프링 컨테이너에 등록되지 않음. 여기저기서 새로 생성 될 수 있음
    private final MemberService memberService;
    @Autowired //MemberController 객체가 생성될 떄 스프링 컨테이너에 있는 MemberService를 불러와서 사용함. dependency injection (의존관계 주입)
    public MemberController(MemberService memberService) { //스프링 컨테이너에 있는 MemberService를 불러와서 사용함
        this.memberService = memberService;
    }

    @GetMapping("/members/new") //get방식으로 해당 주소로 접근하면
    public String createForm() {
        return "/members/createMemberForm"; //templates에서 members/createMemberForm 이라는 html을 그냥 띄워줌
    }
    @PostMapping("/members/new") //등록 버튼을 누르면 post 방식으로 들어왔으니 아래 함수를 실행 (데이터를 form 같은 곳에 넣어 전달할 때 POST 사용)
    public String create(MemberForm form) {
        Member member = new Member(); //새로운 회원의 정보를 담을 Member 객체 선언
        member.setName(form.getName()); //setter를 통해 form으로 부터 받은 name을 member에 저장

        System.out.println("member = " + member.getName()); //디버깅용

        memberService.join(member); //멤버 가입

        return "redirect:/"; //완료 후 루트페이지로 리다이렉트
    }
    @GetMapping("/members")
    public String list(Model model) { //Model은 key, value로 이루어진 HashMap
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members); //key:members  value:members리스트
        return "members/memberList";
    }

}
