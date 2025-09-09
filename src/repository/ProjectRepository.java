package repository;

import configs.message.Ingredient;
import configs.project.TaskStatus;
import configs.project.TaskType;
import managers.ConverterManager;
import managers.ValidatorManager;
import model.project.Task;
import utils.LogRecorder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ProjectRepository implements Repository<Task, String> {
    private static final ProjectRepository instance = new ProjectRepository();
    public static ProjectRepository getInstance() {
        return instance;
    }
// projects
//    id          VARCHAR(10)   PRIMARY KEY,     -- 프로젝트 ID (예: p01)
//    name 	   	  VARCHAR(20) NOT NULL,
//    type        VARCHAR(30)   ,        -- 프로젝트 유형
//    status      VARCHAR(20)   ,        -- 상태 (진행중, 완료 등)
//    start_date  DATE NOT NULL DEFAULT (current_date),
//    end_date    DATE
    @Override
    public void save(Task entity) throws SQLException {
        String query = "insert into projects values (?, ?, ?, ?, ?, ?)";
        try(Connection connection = MakeConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setString(1,entity.getTid());
            pstmt.setString(2,entity.getName());
            pstmt.setString(3, ConverterManager.stringTaskType.convertFrom(entity.getType()));
            pstmt.setString(4, ConverterManager.stringTaskStatus.convertFrom(entity.getStatus()));
            pstmt.setDate(5,entity.getDueTo() == null ?
                    ConverterManager.stringDate.convertToDate(LocalDate.of(9999,12,31))
                    :ConverterManager.stringDate.convertToDate(entity.getDueTo()));
            pstmt.setDate(6,ConverterManager.stringDate.convertToDate(entity.getCreatedAt()));
            pstmt.executeUpdate();
        }
    }

    @Override
    public Task findById(String eid) throws SQLException {
        String query = "select * from projects where id = ?";
        try(Connection connection = MakeConnection.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(query);){
            pstmt.setString(1,eid);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                String tid = rs.getString(1);
                String name = rs.getString(2);
                TaskType type = ConverterManager.stringTaskType.convertTo(rs.getString(3));
                TaskStatus status = ConverterManager.stringTaskStatus.convertTo(rs.getString(4));
                LocalDate end_time = rs.getDate(5).toLocalDate();
                LocalDate start_time = rs.getDate(6).toLocalDate();
                return new Task(tid,name,type,status,end_time,start_time);
            }
        }
        return null;
    }

    @Override
    public void deleteById(String eid) throws SQLException {
        String query = "delete from projects where id = ?";
        try(Connection connection = MakeConnection.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setString(1,eid);
            pstmt.executeUpdate();
        };
    }

    @Override
    public void update(Task entity) throws SQLException {
        String query = "UPDATE projects SET name = ?, status = ?, due_to = ? WHERE id = ?";
        try(Connection connection = MakeConnection.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setString(1,entity.getName());
            pstmt.setString(2,ConverterManager.stringTaskStatus.convertFrom(entity.getStatus()));
            pstmt.setDate(3,ConverterManager.stringDate.convertToDate(entity.getDueTo()));
            pstmt.setString(4,entity.getTid());
            pstmt.executeUpdate();
        }
    }

    @Override
    public Collection<Task> findAll() throws SQLException {
        // id, name, type, status, start_date, end_date
        String  query = "select * from projects";
        Collection<Task> tasks = new ArrayList<>();
        try(Connection connection = MakeConnection.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery()){
            while(rs.next()) {
                String tid = rs.getString("id");
                String name = rs.getString("name");
                TaskType type = ConverterManager.stringTaskType.convertTo(rs.getString("type"));
                TaskStatus status = ConverterManager.stringTaskStatus.convertTo(rs.getString( "status"));
                LocalDate end_time = rs.getDate("end_date").toLocalDate();
                LocalDate start_time = rs.getDate("start_date").toLocalDate();
                Task task = new Task(tid,name,type,status,end_time,start_time);
                tasks.add(task);
            }
        }catch(SQLException e){
            LogRecorder.record(Ingredient.LOG_ERROR_SQL,"findAll()");
        }
        return tasks;
    }

    @Override
    public boolean existsById(String eid){
        String query = "select * from projects where id = ?";
        try(Connection connection = MakeConnection.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery()){
            pstmt.setString(1,eid);
            return rs.next();
        }catch(Exception e){
            LogRecorder.record(Ingredient.LOG_ERROR_SQL,"Project 존재 검사");
            return false;
        }

    }

    @Override
    public int count(){
        String query = "select count(*) from projects";
        try(Connection connection = MakeConnection.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery()){
            if(rs.next()){
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            LogRecorder.record(Ingredient.LOG_ERROR_SQL,"Projects 계수");
        }
        return 0;
    }
}
