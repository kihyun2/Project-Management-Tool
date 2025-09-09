package model.project;

import configs.project.TaskStatus;
import configs.project.TaskType;
import managers.ConverterManager;
import model.team.Member;

import java.sql.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {
    private LocalDate createdAt = LocalDate.now();
    private String tid;
    private String name;
    private TaskType type;
    private TaskStatus status = TaskStatus.NOT_STARTED;
    private Member assignee;
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

    public Member getAssignee() {
        return assignee;
    }

    public void setAssignee(Member assignee) {
        this.assignee = assignee;
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
        return tid + "/" + name + "/" + type + "/" + status + "/" + (assignee != null ? assignee.getName() : "미정") + "/" + ConverterManager.stringDate.convertFrom(createdAt) + "/" + (dueTo != null ? ConverterManager.stringDate.convertFrom(dueTo) : "미정");
    }
}
