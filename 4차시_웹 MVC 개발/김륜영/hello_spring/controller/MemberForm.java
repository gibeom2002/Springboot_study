package hello.hello_spring.controller;

public class MemberForm {

    private String name; //   /members/new 페이이지의 key : value   인 name: text 가 이곳의 name으로 들어옴

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
