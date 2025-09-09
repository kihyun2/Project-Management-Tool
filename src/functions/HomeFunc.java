package functions;

import configs.message.Ingredient;
import configs.message.SystemMessage;
import configs.message.UIMessage;
import functions.project.ProjectFuncs;
import functions.team.TeamFuncs;
import managers.MessageBuilderManager;
import managers.messageBuild.MessageBuilder;
import managers.messageBuild.SystemMessageBuilder;
import managers.messageBuild.UIMessageBuilder;
import managers.messageBuild.ingredient.OverviewMessageBuilder;
import model.project.Project;
import model.team.Team;
import repository.MakeConnection;
import repository.MemberRepository;
import repository.ProjectRepository;
import utils.FuncTest;
import utils.LogRecorder;
import utils.Graph;
import utils.Pair;
import utils.console.InputReader;
import utils.console.Viewer;

import java.sql.Connection;
import java.util.List;

// [ HomeFuncs 개요 ]
// - "홈 화면" 단계
// - UI : 프로젝트 현황 (overview), 최근 활동 (recentLogs)
// - System : 기능 선택 안내

public class HomeFunc {
    /* [ 프로그램 실행 내내 호출이 유지될 메서드 ] */
    public static void start() {
        // [Test] 기능 테스트용 더미 데이터 추가
        FuncTest.addDummyTasks();
        FuncTest.addDummyMembers();

        // [1] 프로그램 실행 동안 유지될 반복문 시작
        while (true) {
            // [Test] 기능 테스트용 콘솔
            // FuncTest.showProject(); (비활성화)

            // [Loop-1] 콘솔창 정돈 후, 필요한 MessageBuilder들 준비
            Viewer.clear();
            OverviewMessageBuilder overviewBuilder = MessageBuilderManager.overview;
            UIMessageBuilder uiBuilder = MessageBuilderManager.ui;
            SystemMessageBuilder sysBuilder = MessageBuilderManager.system;

            // [Loop-2] overview 제작에 필요한 정보 모으고 메세지 재료 제작
            List<String> overviewInfos = Project.getInstance().controller.countTasksByStatus();
            Pair<Integer,Integer> assignment = Team.getInstance().controller.countAssignment();
            overviewInfos.add(Graph.draw(assignment.getKey(), assignment.getValue()));

            // [Loop-3] 재료 정돈 (overview 메세지, recentLogs 메세지)
            String overviewMsg = overviewBuilder.build(Ingredient.OVERVIEW.getFormat(),overviewInfos);
            String recentLogsMsg = LogRecorder.getRecentLogs();

            // [Loop-4] 최종 메세지 제작해 출력 (UIMessage + SystemMessage)
            String uiMsg = uiBuilder.build(UIMessage.HOME.getMsg(), MessageBuilder.pack(overviewMsg,recentLogsMsg));
            String sysMsg = sysBuilder.build(SystemMessage.HOME.getMsg());

            Viewer.print(MessageBuilder.integrate(uiMsg, sysMsg));

            // [Loop-5] 사용자의 입력값에 따라 알맞은 기능 함수 호출
            switch (InputReader.read()) {
                case "1" -> TeamFuncs.inviteMember();
                case "2" -> TeamFuncs.updateMemberInfo();
                case "3" -> TeamFuncs.browseMembers();
                case "4" -> ProjectFuncs.addTask();
                case "5" -> ProjectFuncs.updateTaskInfo();
                case "6" -> ProjectFuncs.browseTasks();
                case "0" -> System.exit(0);
                default -> {
                    // [메모] 어차피 반복문의 최하단이어서 continue 없이도 동일하게 작동
                }
            }

        }
    }
}
