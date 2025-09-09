package configs.message;

// [ SystemMessage 개요 ]
// - 기능별 System 메서지 부분의 format 문자열들을 보관할 Config 파일임다.
// - 호출할 때엔 "SystemMessage.필드명.getMsg()"으로 message format을 얻을 수 있습니다.

// [ 메모 ]
// - 필드의 작명 방식은 다음과 같슴다. 초기화면인 경우 "사용될기능", 특정상황인 경우 "사용될기능_상황설명"
//   ex) 초기화면 : BROWSE_TASKS() / 특정상황 : BROWSE_TASKS_FAILED(), BROWSE_TASKS_REMOVE()
// - getMsg()를 통해 자동으로 "<System> - - - -" 구분선이 작성되도록 구현해놓았음다.

public enum SystemMessage {
    HOME("""
            알맞은 숫자를 입력해 기능을 선택해주세요. 0 입력 시 프로그램을 종료합니다.
            1. 팀원초대 | 2. 팀원정보수정 | 3. 팀원조회 | 4. 업무등록 | 5. 업무정보수정 | 6. 업무조회
            """),
    ADD_TASK("""
            요구 양식에 맞춰 등록할 정보를 입력해주세요.
            기능 선택으로 돌아가시려면 486 을 입력해주세요.
            """),
    ADD_TASK_FAILED("""
            " %s "
            양식에 맞게 정보를 입력해주세요. 기능 선택으로 돌아가시려면 486 을 입력해주세요.
            """),
    UPDATE_TASK_INFO("""
            요구 양식에 맞춰 등록할 정보를 입력해주세요.
            기능 선택으로 돌아가시려면 486 을 입력해주세요.
            """),
    UPDATE_TASK_INFO_FAILED("""
            " %s "
            양식에 맞게 정보를 입력해주세요. 기능 선택으로 돌아가시려면 486 을 입력해주세요.
            """),
    BROWSE_TASKS("""
            요구 양식에 맞춰 등록할 정보를 입력해주세요.
            기능 선택으로 돌아가시려면 486 을 입력해주세요.
            """),
    BROWSE_TASKS_FAILED("""
            " %s "
            양식에 맞게 정보를 입력해주세요. 기능 선택으로 돌아가시려면 486 을 입력해주세요.
            """),
    BROWSE_TASKS_RESPOND("""
            다시 조회를 하시려면 아무 키나 입력해주세요.
            기능 선택으로 돌아가시려면 486 을 입력해주세요.
            """),
    INVITE_MEMBER("""
            요구 양식에 맞춰 등록할 정보를 입력해주세요.
            기능 선택으로 돌아가시려면 486 을 입력해주세요.
            """),
    INVITE_MEMBER_FAILED("""
            " %s "
            기능 선택으로 돌아가시려면 486 을 입력해주세요.
            """),
    UPDATE_MEMBER_INFO("""
            요구 양식에 맞춰 등록할 정보를 입력해주세요.
            기능 선택으로 돌아가시려면 486 을 입력해주세요.
            """),
    UPDATE_MEMBER_INFO_FAILED("""
            " %s "
            기능 선택으로 돌아가시려면 486 을 입력해주세요.
            """),
    BROWSE_MEMBERS("""
            요구 양식에 맞춰 등록할 정보를 입력해주세요.
            기능 선택으로 돌아가시려면 486 을 입력해주세요.
            """),
    BROWSE_MEMBERS_FAILED("""
            " %s "
            기능 선택으로 돌아가시려면 486 을 입력해주세요.
            """),
    BROWSE_MEMBERS_RESPOND("""
            다시 조회를 하시려면 아무 키나 입력해주세요.
            기능 선택으로 돌아가시려면 486 을 입력해주세요.
            """);

    private String msg;

    private SystemMessage(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return "< System > ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~\n\n" + msg
                + "\n~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~\n입력) ";
    }

}
