package utils;

import configs.project.TaskStatus;
import configs.team.Authority;
import controller.controllers.ProjectController;
import controller.controllers.TeamController;
import managers.ConverterManager;
import managers.ValidatorManager;
import model.project.Project;
import model.project.Task;
import model.team.Member;
import model.team.Team;
import utils.console.Viewer;

import java.util.Random;

// [ FuncTest 클래스 설명 ]
// - FuncTest는 코드가 의도대로 작동하는지 확인하기 위한 클래스임다.
// - addDummy 메서드들은 각각 더미 Task와 Member 인스턴스를 생성할 수 있습니다.
// - dummyCount 필드를 통해 생성될 인스턴스의 개수를 조절할 수 있습니다.
// - show 메서드들은 각각 전체 프로젝트와 전체 팀의 데이터를 확인할 수 있습니다.

public class FuncTest {
    private static final int dummyCount = 15;

    /* Project에 더미 데이터 추가 */
    public static void addDummyTasks() {
        ProjectController pc = Project.getInstance().controller;

        for (int i = 1; i <= dummyCount; i++) {
            String dummy = "%s/%s/%s/%s";
            String name = i < 10 ? "프로젝트0" + i : "프로젝트" + i;
            String type = "" + (new Random().nextInt(4) + 1);
            String teammates = "@";
            String status = "" + (new Random().nextInt(3));
            String end_date = "@";
            dummy = String.format(dummy, name, type, teammates, end_date);
            Pair<Boolean,String> pair = ValidatorManager.addTask.check(dummy);
            pc.add(pair.getValue().split("/"));
        }
    }

    /* Project 현황 확인 */
    public static void showProject() {
        String tasks = Project.getInstance().controller.getAll().stream().map(Task::toString).reduce((acc, cur) -> acc + cur).orElseGet(() -> "!! project test 생성 실패 !!");

        Viewer.print(String.format("""
                [Task 목록]
                %s
                [현재 Task의 개수]
                %d
                """, tasks, Project.getInstance().controller.getAll().size()));
    }

    /* Team에 더미 데이터 추가 */
    public static void addDummyMembers() {
        TeamController tc = Team.getInstance().controller;

        for (int i = 1; i <= dummyCount; i++) {
            String dummy_add = "%s/%s";
            String name = i < 10 ? "팀원0" + i : "팀원" + i;
            String auth = new Random().nextInt(2) + 1 + "";
            Pair<Boolean, String> pair = ValidatorManager.inviteMember.check(String.format(dummy_add,name,auth));
            tc.add(pair.getValue().split("/"));

//            // [메모] 업무가 없는 더미도 만들기 위해 10번까지만 Task 할당
//            if (i < 10) {
//                dummy.addTask(Project.getInstance().controller.get("t0" + i));
//            }
        }
    }

    /* Team 현황 확인 */
    public static void showTeam() {
        // [메모] Member 클래스에서 toString 메서드를 구현해야 작동합니다!
        String members = Team.getInstance().controller.getAll().stream().map(Member::toString).reduce((acc, cur) -> acc + cur).orElseGet(() -> "!! team test 문자열 생성 실패 !!");

        Viewer.print(String.format("""
                [Member 목록]
                %s
                [현재 Member의 개수]
                %d
                """, members, Team.getInstance().controller.getAll().size()));
    }
}
