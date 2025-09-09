package repository;

import configs.message.Ingredient;
import model.project.Task;
import utils.LogRecorder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ProjectTeamRepository {
    private static final ProjectTeamRepository instance = new ProjectTeamRepository();
    private ProjectTeamRepository() {}
    public static ProjectTeamRepository getInstance() { return instance; }

    public void addMemberToProject(String projectId, String memberId) throws SQLException {
        String sql = "INSERT INTO project_team (pid, mid) VALUES (?, ?)";
        try (Connection conn = MakeConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, projectId);
            pstmt.setString(2, memberId);
            pstmt.executeUpdate();
        }
    }

    public void removeMemberFromProject(String projectId, String memberId) throws SQLException {
        String sql = "DELETE FROM project_team WHERE pid = ? AND mid = ?";
        try (Connection conn = MakeConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, projectId);
            pstmt.setString(2, memberId);
            pstmt.executeUpdate();
        }
    }
    public Set<Task> findProjectbyMember(String memberId) throws SQLException {
        String sql = "SELECT * FROM project_team WHERE mid = ?";
        Set<Task> tasks = new HashSet<>();
        try (Connection conn = MakeConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String taskID  = rs.getString("pid");
                Task task = ProjectRepository.getInstance().findById(taskID);
                tasks.add(task);
            }
        }
        return tasks;
    }

    public boolean exists(String projectId, String memberId) {
        String sql = "SELECT * FROM project_team WHERE pid = ? AND mid = ?";
        try (Connection conn = MakeConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            pstmt.setString(1, projectId);
            pstmt.setString(2, memberId);
            return rs.next();

        } catch (SQLException e) {
            LogRecorder.record(Ingredient.LOG_ERROR_SQL,"존재 검사");
            return false;
        }
    }

}

