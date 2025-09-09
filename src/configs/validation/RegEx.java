package configs.validation;

// [ RegEx 개요 ]
// - 검증에 활용할 정규식 패턴들을 보관하기 위한 Config 파일임다.
// - 호출할 때엔 "RegEx.필드명.getPattern()"으로 정규식 String을 얻을 수 있습니다.

// [ 메모 ]
// - 필드의 작명 방식은 다음과 같슴다. "사용될기능_검증항목"
//   ex) ADD_TASK_NAME("^[가-힣a-zA-Z0-9]$") -> "업무등록" 입력값 중 "업무명"을 검증하는 정규식
//   지금은 테스트를 위한 일괄 검증용 필드가 작성돼있슴다...

public enum RegEx {
    ADD_TASK_STRUCTURE("^[^/]+/[^/]+/[^/]+/[^/]+$"),
    ADD_TASK_NAME("^[가-힣a-zA-Z0-9]{1,15}$"),
    ADD_TASK_TYPE("^[1-4]$"),
    ADD_TASK_ASSIGNEE("^m(0[1-9]|[1-9][0-9])|@$"),
    ADD_TASK_DUE("^((20[0-9]{2})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])|@)$"),
    UPDATE_TASK_INFO_STRUCTURE("^[^/]+/[^/]+/[^/]+/[^/]+/[^/]+$"),
    UPDATE_TASK_INFO_TID("^t(0[1-9]|[1-9][0-9])$"),
    UPDATE_TASK_INFO_NAME("^[가-힣a-zA-Z0-9]{1,15}|@$"),
    UPDATE_TASK_INFO_STATUS("^[1-3]|@$"),
    UPDATE_TASK_INFO_ASSIGNEE("^m(0[1-9]|[1-9][0-9])|@$"),
    UPDATE_TASK_INFO_DUE("^((20[0-9]{2})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])|@)$"),
    BROWSE_TASKS_STRUCTURE("^\\s*\\d+,[^,/]+(\\s*/\\s*\\d+,[^,/]+){0,2}\\s*$"),
    BROWSE_TASKS_CRITERIA("^[1-3]$"),
    BROWSE_TASKS_CONDITION_TYPE("^[1-4]$"),
    BROWSE_TASKS_CONDITION_STATUS("^[1-3]$"),
    BROWSE_TASKS_CONDITION_MID("^m(0[1-9]|[1-9][0-9])|@$"),
    MEMBER_INVITE_STRUCTURE("^[^/]+/[^/]+$"),
    MEMBER_UPDATE_STRUCTURE("^[^/]+/[^/]+/[^/]+/[^/]+$"),
    MEMBER_BROWSE_CONDITION("^(@|([1-5](,[1-5]){0,4}))$"),
    MEMBER_NAME("^[가-힣a-zA-Z0-9]{1,15}$"),
    MEMBER_AUTH("^[1-3]$"),
    MEMBER_MID("^m(0[1-9]|[1-9][0-9])|@$"),
    TASK_TID("^t(0[1-9]|[1-9][0-9])$");

    private String regEx;

    private RegEx(String regEx) {
        this.regEx = regEx;
    }

    public String getPattern() {
        return regEx;
    }
}
