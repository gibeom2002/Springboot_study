package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
//순수JDBC로 이루어진 레포지토리로 과거에는 모두 이렇게 구현하였음. 요즘은 이걸로 안쓰고 대부분 JPA를 사용한다고 함.
public class JdbcMemberRepository implements MemberRepository {
    private final DataSource dataSource;
    public JdbcMemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public Member save(Member member) {
        String sql = "insert into member(name) values(?)"; //sql 문 생성

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null; //결과
        try { //이 자체가 예외처리가 많이 발생하기 때문에 try catch문을 잘 사용해야함!
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); //RETURN_GENERATED_KEYS는 DB에 insert할 떄 생성된 index값
            pstmt.setString(1, member.getName());
            pstmt.executeUpdate(); //DB에 실제 쿼리 날림
            rs = pstmt.getGeneratedKeys(); //생성 된 ID(INDEX)를 받음
            if (rs.next()) { //값이 있으면 값을 꺼냄
                member.setId(rs.getLong(1));
            } else { //값을 꺼내지 못함
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally { //자원들을 잘 release 해줘야함 (나는 사용하는 그 순간만 연결을 하고 그 외에는 연결을 꼭 끊어야함! 자원을 자꾸 선점하고 있으면 장애 발생)
            close(conn, pstmt, rs);
        }
    }
    @Override
    public Optional<Member> findById(Long id) { //조회
        String sql = "select * from member where id = ?"; //사용 할 sql문 선언
        //연결을 위한 변수들 선언
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery(); //조회하는 함수
            if(rs.next()) { //값이 있으면 꺼냄
                Member member = new Member(); //반환 할 멤버객체 생성
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member); //멤버 객체 반환 (Optional에 담아서)
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    @Override
    public List<Member> findAll() { //모든 멤버들을 다 찾아서 반환해줄 함수
        String sql = "select * from member"; //사용 할 sql문
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<Member> members = new ArrayList<>();
            while(rs.next()) { //꺼낼 값이 있다면 꺼냄
                Member member = new Member(); //반환 할 객체 선언
                member.setId(rs.getLong("id")); //id를 얻어와서 설정
                member.setName(rs.getString("name")); //name을 얻어와서 설정
                members.add(member);
            }
            return members;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    @Override
    public Optional<Member> findByName(String name) { //이름을 가지고 회원을 찾는 함수
        String sql = "select * from member where name = ?"; //sql문 선언
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Member member = new Member(); //반환 할 멤버 객체
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}