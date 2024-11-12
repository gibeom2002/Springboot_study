package hello.hello_spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SampleController {

    @PostMapping("/sendName")
    public ResponseEntity<List<Map<String, String>>> sendName(@RequestBody List<Map<String, String>> names) {
        // @RequestBody 는 requestbody(프론트에서 온 정보)를 자바 객체로 변환해주는 역할
        // List<Map<String, String>> names   는 Json으로 온 정보를 구조분해하여 names로 저장한다는 뜻
        // ResponseEntity<List<Map<String, String>>>  해당 자료형으로 반환한다는 뜻   (http response code와 함께 정보를 전달)
        List<Map<String, String>> nameList = new ArrayList<>(); //이름들을 담을 리스트. 리스트 내부 요소는 맵 객체

        for (Map<String, String> name : names) { //names맵에 들어있는 모든 객체들에 대해 반복
            String nameValue = name.get("name"); //key가 name인 value를 뽑아서 nameValue에 저장
            if (nameValue != null) { //nameValue가 존재 한다면
                Map<String, String> nameMap = new HashMap<>(); //반환해줄 list에 넣을 nameMap 선언
                nameMap.put("name", nameValue); //key:name  value:nameValue로 저장한다.
                nameList.add(nameMap); //반환할 list에 저장한다.
                System.out.println(nameValue); //디버깅용
            }
        }
        return ResponseEntity.ok(nameList); //http 200 status랑 같이 nameList를 JSON 형식으로 전송해준다.
    }
}
