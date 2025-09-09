package model;

public class ProjectTeam {
    String taskID;
    String memberID;
    public ProjectTeam(String taskID, String memberID) {
        this.taskID = taskID;
        this.memberID = memberID;
    }

    public String getMemberID() {
        return memberID;
    }
    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }
    public String getTaskID() {
        return taskID;
    }
    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }
}
