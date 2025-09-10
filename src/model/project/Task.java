package model.project;

import configs.message.Ingredient;
import configs.project.TaskStatus;
import configs.project.TaskType;
import managers.ConverterManager;
import model.team.Member;
import repository.ProjectTeamRepository;
import utils.LogRecorder;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class Task {
    private LocalDate createdAt = LocalDate.now();
    private String tid;
    private String name;
    private TaskType type;
    private TaskStatus status = TaskStatus.NOT_STARTED;
    private Set<Member> members;
    private LocalDateTime updatedAt = LocalDateTime.now();
    private LocalDate dueTo;

    public Task(String tid, String name, TaskType type, TaskStatus status, LocalDate dueTo) {
        this.tid = tid;
        this.name = name;
        this.type = type;
        this.status = status;
        this.dueTo = dueTo;
    }
    public Task(String tid, String name, TaskType type, TaskStatus status, LocalDate dueTo, LocalDate createdAt) {
        this.tid = tid;
        this.name = name;
        this.type = type;
        this.status = status;
        this.dueTo = dueTo;
        this.createdAt = createdAt;
    }

    public String getTid() {
        return tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskType getType() {
        return type;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Set<Member> getMembers() {

        try {
            return ProjectTeamRepository.getInstance().findMemberbyProject(tid);
        } catch (SQLException e) {
            LogRecorder.record(Ingredient.LOG_ERROR_SQL,"Task.getMembers");
            return null;
        }

    }

    public void setAssignee(Set<Member> assignee) {
        this.members = assignee;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDate getDueTo() {
        return dueTo;
    }

    public void setDueTo(LocalDate dueTo) {
        this.dueTo = dueTo;
    }

    public void updateTime() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        String createAtString = ConverterManager.stringDate.convertFrom(createdAt);
        String end_date = (dueTo != null ? ConverterManager.stringDate.convertFrom(dueTo) : "미정");
        String members = getMembers().stream().map(Member::getName).collect(Collectors.joining(","));
        return String.format("%s/%s/%s/%s/%s/%s/%s",tid,name,type,status,members,createAtString,end_date);
    }
}
