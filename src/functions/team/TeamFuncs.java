package functions.team;

// [ TeamFuncs 개요 ]
// - "@@ 화면" 단계, @@은 각 기능의 한국 이름  ex) 업무등록 화면, 업무조회 화면, ...
// - UI : 기능 소개 + 입력양식 설명
// - System : (검증실패사유) + 정보입력 안내 + 홈화면 복귀 안내

// [ 메모 ]
// - "팀원정보수정" 에서 tasks 정보가 변경되거나, 멤버가 해임될 경우 관련 Task 인스턴스들의 assignee 필드도 최신화해주기!
//    이 때, Task 인스턴스의 updatedAt에 대한 갱신도 잊지 않기! (자동으로 갱신되는 Util 메서드를 만드...?)
// - 486을 입력해 홈 화면으로 복귀하는 기능은 구현되지 않았습니다!!

import configs.message.Ingredient;
import configs.message.SystemMessage;
import configs.message.UIMessage;
import managers.MessageBuilderManager;
import managers.ValidatorManager;
import managers.messageBuild.MessageBuilder;
import managers.messageBuild.SystemMessageBuilder;
import managers.messageBuild.UIMessageBuilder;
import managers.messageBuild.ingredient.MemberListMessageBuilder;
import model.project.Project;
import model.project.Task;
import model.team.Member;
import model.team.Team;
import utils.Pair;
import utils.console.InputReader;
import utils.console.Viewer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TeamFuncs {
    /* [ "팀원초대" 선택 시 실행될 실행될 실행될 메서드 ] */
    public static void inviteMember() {
        Pair<Boolean, String> alert = new Pair<>(true, "");  // [메모] System 메세지 갱신에 활용할 지역변수
        // [1] 업무등록 화면 유지할 반복문 시작
        while (true) {
            // [Loop-1] UI와 System 문자열 제작해 출력
            Viewer.clear();

            UIMessageBuilder uiBuilder = MessageBuilderManager.ui;
            SystemMessageBuilder sysBuilder = MessageBuilderManager.system;

            String uiMsg = uiBuilder.build(UIMessage.INVITE_MEMBER.getMsg());
            String sysMsg = alert.getKey() // [메모] alert의 key에는 유효성 검사 결과 bool 값이 담김, 이를 기준으로 다른 분기의 System 메세지 출력
                    ? sysBuilder.build(SystemMessage.INVITE_MEMBER.getMsg())
                    : sysBuilder.build(SystemMessage.INVITE_MEMBER_FAILED.getMsg(), MessageBuilder.pack(alert.getValue()));

            Viewer.print(MessageBuilder.integrate(uiMsg, sysMsg));

            // [Loop-2] 특정 문자가 입력되면 홈 화면으로 복귀
            String input = InputReader.read();
            if(input.equals("486")) return;

            // [Loop-3] 사용자의 입력에 대한 유효성 검사
            alert = ValidatorManager.inviteMember.check(input);

            // [Loop-4] 유효성 검사 통과시 데이터 저장
            if (alert.getKey()){
                Team.getInstance().controller.add(alert.getValue().split("/"));
            }

        }
    }

    /* [ "팀원정보수정" 선택 시 실행될 메서드 ] */
    public static void updateMemberInfo() {
        Pair<Boolean, String> alert = new Pair<>(true, "");  // [메모] System 메세지 갱신에 활용할 지역변수
        // [1] 업무등록 화면 유지할 반복문 시작
        while (true) {
            // [Loop-1] UI와 System 문자열 제작해 출력
            Viewer.clear();

            UIMessageBuilder uiBuilder = MessageBuilderManager.ui;
            SystemMessageBuilder sysBuilder = MessageBuilderManager.system;

            String uiMsg = uiBuilder.build(UIMessage.UPDATE_MEMBER_INFO.getMsg());
            String sysMsg = alert.getKey() // [메모] alert의 key에는 유효성 검사 결과 bool 값이 담김, 이를 기준으로 다른 분기의 System 메세지 출력
                    ? sysBuilder.build(SystemMessage.UPDATE_MEMBER_INFO.getMsg())
                    : sysBuilder.build(SystemMessage.UPDATE_MEMBER_INFO_FAILED.getMsg(), MessageBuilder.pack(alert.getValue()));

            Viewer.print(MessageBuilder.integrate(uiMsg, sysMsg));

            // [Loop-2] 특정 문자가 입력되면 홈 화면으로 복귀
            String input = InputReader.read();
            if(input.equals("486")) return;

            // [Loop-3] 사용자의 입력에 대한 유효성 검사
            alert = ValidatorManager.updateMemberInfo.check(input);

            // [Loop-4] 유효성 검사 통과시 데이터 저장
            if (alert.getKey()){
                Team.getInstance().controller.update(alert.getValue().split("/"));
            }

        }
    }

    /* [ "팀원조회" 선택 시 실행될 메서드 ] */
    public static void browseMembers() {
        Pair<Boolean, String> alert = new Pair<>(true, "");
        while (true) {
            // [Loop-1] UI와 System 문자열 제작해 출력
            Viewer.clear();

            UIMessageBuilder uiBuilder = MessageBuilderManager.ui;
            SystemMessageBuilder sysBuilder = MessageBuilderManager.system;
            String UiMsg = uiBuilder.build(UIMessage.BROWSE_MEMBERS.getMsg());
            String sysMsg = alert.getKey()
                    ? sysBuilder.build(SystemMessage.BROWSE_MEMBERS.getMsg())
                    : sysBuilder.build(SystemMessage.BROWSE_MEMBERS_FAILED.getMsg(), MessageBuilder.pack(alert.getValue()));
            Viewer.print(MessageBuilder.integrate(UiMsg, sysMsg));

            String input = InputReader.read();
            if (input.equals("486")) return;
            alert = ValidatorManager.browseMembers.check(input);
            if (alert.getKey()) {
                showFilteredMembers(alert.getValue().split(","));
            }

        }
    }
    /* "업무조회" 파생 화면 -> 업무 목록 출력 */
    private static void showFilteredMembers(String[] inputs) {
        // [1] 조건들로 값 추출 List<String>
        Stream<Member> browsing = Team.getInstance().controller.browse(inputs);
        List<String> filteredMembers = browsing.map(m ->{
            String tasks = m.getTasks().isEmpty() || m.getTasks() == null ?
                    ""
                    : m.getTasks().stream().map(Task::getName).collect(Collectors.joining(","));
            return String.format("%s/%s/%s/%s/%s",m.getMid(),m.getName(),m.getAuth(),tasks,m.getTasks().size()+"");
        }).toList();

        // [2] 값과 함께 재료 메시지 제작
        MemberListMessageBuilder memberListMsgBuilder = MessageBuilderManager.memberList;
        String messageIngredient = memberListMsgBuilder.build(Ingredient.MEMBER_LIST.getFormat(), filteredMembers);
        
        // [3] 최종 메시지 제작(UI+재료+system)
        UIMessageBuilder uiBuilder = MessageBuilderManager.ui;
        SystemMessageBuilder sysBuilder = MessageBuilderManager.system;

        String uiMsg = uiBuilder.build(UIMessage.BROWSE_MEMBERS_RESPOND.getMsg(), MessageBuilder.pack(messageIngredient));
        String sysMsg = sysBuilder.build(SystemMessage.BROWSE_MEMBERS_RESPOND.getMsg());
        // [4] 출력
        Viewer.clear();
        Viewer.print(MessageBuilder.integrate(uiMsg, sysMsg));

        // [5] 특정 문자가 입력되면 이전 화면으로 복귀
        String input = InputReader.read();
        if(input.equals("486")) return;
    }


    // [보류] public static void setLeader() {}
}
