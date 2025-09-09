package repository;

import configs.message.Ingredient;
import configs.team.Authority;
import managers.ConverterManager;
import managers.conversion.StringAuthorityConverter;
import model.team.Member;
import utils.LogRecorder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemberRepository implements Repository<Member, String> {
    private static MemberRepository instance;

    private MemberRepository(){};
    public static MemberRepository getInstance(){
        if(instance == null){
            instance = new MemberRepository();
        }
        return instance;
    }

    public void save(Member member) throws SQLException{
        // mid, name, auth
        String query = "INSERT INTO members(id,name,auth) VALUES (?,?,?)";
        try(Connection connection = MakeConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(query)){
                pstmt.setString(1, member.getMid());
                pstmt.setString(2, member.getName());
                pstmt.setString(3, ConverterManager.stringAuthority.convertFrom(member.getAuth()));
                pstmt.executeUpdate();

        }
    }
    public Member findById(String id) throws SQLException {
        String query = "SELECT * FROM members WHERE id = ?";
        try(Connection connection = MakeConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()){
            pstmt.setString(1, id);
            if (rs.next()) {
                String mid = rs.getString("id");
                String name = rs.getString("name");
                Authority auth = ConverterManager.stringAuthority.convertTo(rs.getString("auth"));
                return new Member(mid, name, auth);
            } else{
                return null;
            }

        }
    }
    public Collection< Member> findAll() throws SQLException{
        String query = "SELECT * FROM members";
        Collection<Member> members = new ArrayList<>();
        try(Connection conn = MakeConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()){
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                Authority auth = ConverterManager.stringAuthority.convertTo(rs.getString("auth"));
                members.add(new Member(id, name, auth));
            }

        }
        return members;
    }
    public void update(Member member) throws SQLException {
        String query = "UPDATE members SET name = ?, auth = ? WHERE id = ?";
        try(Connection conn = MakeConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setString(1, member.getName());
            pstmt.setString(2, ConverterManager.stringAuthority.convertFrom(member.getAuth()));
            pstmt.setString(3, member.getMid());
            pstmt.executeUpdate();
        }
    }
    public void deleteById(String id) throws SQLException{
        String query = "DELETE FROM members WHERE id = ?";
        try (Connection connection = MakeConnection.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }
    public boolean existsById(String id){
        String query = "SELECT * FROM members WHERE id = ?";
        try(Connection connection = MakeConnection.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LogRecorder.record(Ingredient.LOG_ERROR_SQL,"Member 존재 검사");
            return false;
        }
    }

    @Override
    public int count() {
        String query = "SELECT COUNT(*) FROM members";
        try(Connection connection = MakeConnection.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery()){
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LogRecorder.record(Ingredient.LOG_ERROR_SQL,"멤버 계수");
        }
        return 0;
    }
}
