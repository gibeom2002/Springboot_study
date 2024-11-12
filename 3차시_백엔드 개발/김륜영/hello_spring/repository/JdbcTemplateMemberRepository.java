package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcTemplateMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateMemberRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) { //새로운 멤버를 저장하는 함수
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id"); //table명이랑 PK 주면 알아서 insertsql문을 만들어줌

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        member.setId(key.longValue());
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) { //순수 JDBC는 정말 긴 코드를 작성해야 하지만 템플릿을 사용하면 정말 짧아짐
        List<Member> result = jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id);
        return result.stream().findAny(); //List 자료형인 reslt를 Spring으로 바꿔서 반환
    }

    @Override
    public Optional<Member> findByName(String name) { //이름으로 멤머 찾기
        List<Member> result = jdbcTemplate.query("select * from member where name = ?", memberRowMapper(), name); //결과를 rowMapper를 통해 매핑
        return result.stream().findAny(); //List 자료형인 reslt를 Spring으로 바꿔서 반
    }

    @Override
    public List<Member> findAll() { //모든 멤버 찾기
        return jdbcTemplate.query("select * from member", memberRowMapper());
    }
    private RowMapper<Member> memberRowMapper() { //멤버 객체 생성에 대한 함수   멤버 객체로 매핑
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));
            return member;
        };
    }
}
