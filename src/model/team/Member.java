package model.team;

import configs.message.Ingredient;
import configs.team.Authority;
import controller.controllers.TeamController;
import model.project.Task;
import repository.MemberRepository;
import repository.ProjectTeamRepository;
import utils.LogRecorder;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Member {
    private LocalDate startDate = LocalDate.now();
    private String mid;
    private String name;
    private Authority auth;
    private Set<Task> tasks; // [메모] 멤버를 통해 접근할 Tasks는 각각에 접근할 필요가 없기 때문에 Set으로 지정

    public Member(String mid, String name, Authority auth) {
        this.mid = mid;
        this.name = name;
        this.auth = auth;
        this.tasks = new HashSet<>();
    }
    public Member(String mid, String name, Authority auth, LocalDate startDate) {
        this.mid = mid;
        this.name = name;
        this.auth = auth;
        this.startDate = startDate;
    }

    public String getMid() {
        return mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Authority getAuth() {
        return auth;
    }

    public void setAuth(Authority auth) {
        this.auth = auth;
    }

    public Set<Task> getTasks() {
        try{
            return ProjectTeamRepository.getInstance().findProjectbyMember(mid);}
        catch(Exception e){
            LogRecorder.record(Ingredient.LOG_ERROR_SQL,"Member getTasks() 실행");
            return null;
        }
    }

    public void addTask(Task task){
        try{
            ProjectTeamRepository.getInstance().addMemberToProject(task.getTid(),mid);
        }catch(Exception e){
            LogRecorder.record(Ingredient.LOG_ERROR_SQL,"addTask 처리");
            e.printStackTrace();
        }
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return "Member{" +
                "startDate=" + startDate +
                ", mid='" + mid + '\'' +
                ", name='" + name + '\'' +
                ", auth=" + auth +
                ", tasks=" + tasks +
                '}';
    }
}
