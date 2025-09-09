package configs.message;

// [ Ingredient 개요 ]
// - 재료 메세지 제작 단계에서 활용할 문자열 format들을 보관하는 enum입니다.
// - 호출할 때엔 "Ingredient.필드명.getFormat()"으로 format 문자열을 얻을 수 있습니다.

// [ 메모 ]
//   - 완성된 Log 메세지의 예시는 다음과 같슴다.
//   ex) "20240923 15:22  |  새로운 팀원 [홍길동] 님을 초대했습니다."

public enum Ingredient {
    OVERVIEW("""
            [업무상태] 완료 %s | 진행 %s | 대기 %s | 전체 %s
            [분업현황] %s ( ■ = 담당업무 보유 팀원 | □ = 담당업무 미보유 팀원 )
            """),
    TASK_LIST("""
            = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
             No.%s [ %s ]
              유형 : %s  |  상태 : %s  |  담당자 : %s
              기간 : %s ~ %s
            """),
    MEMBER_LIST("""
            = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
             No.%s [%s] %s | Tasks : %s (%s건)
            """),
    MEMBER_LIST_FAILED("조건에 부합하는 Member가 없습니다."),
    TASK_LIST_FAILED("\" 조건에 부합하는 Task가 존재하지 않습니다! \""),
    REMOVE_TASK_FAILED("제거할 업무를 찾지 못했습니다. ID를 확인해주세요."),
    REMOVE_TASK_SUCCESS("[ %s ] 업무 제거에 성공했습니다."),
    DISMISS_MEMBER_FAILED("해임할 팀원을 찾지 못했습니다. ID를 확인해주세요."),
    DISMISS_MEMBER_SUCCESS("[ %s ] 팀원 해임에 성공했습니다."),
    ADD_TASK_SUCCESS("[ %s ] 업무 생성에 성공했습니다. 업무ID는 [ %s ] 입니다."),
    UPDATE_TASK_INFO_SUCCESS("[ %s ] 업무 수정에 성공했습니다."),
    INVITE_MEMBER_SUCCESS("[ %s ] 팀원 초대에 성공했습니다.팀원ID는 [ %s ]입니다."),
    UPDATE_MEMBER_INFO_SUCCESS("[ %s ] 님의 정보 수정에 성공했습니다."),
    LOG_INVITE_MEMBER("%s  |  새로운 팀원 [ %s ] 님을 초대했습니다."),
    LOG_UPDATE_MEMBER_INFO("%s  |  [ %s ] 님의 정보를 수정했습니다."),
    LOG_DISMISS_MEMBER("%s  |  [ %s ] 님을 팀에서 해임했습니다."),
    LOG_ADD_TASK("%s  |  신규 업무 [ %s ]을(를) 등록했습니다."),
    LOG_UPDATE_TASK_INFO("%s  |  [ %s ] 업무 정보를 수정했습니다."),
    LOG_REMOVE_TASK("%s  |  [ %s ] 업무 정보를 제거했습니다."),
    LOG_ERROR_SQL("%s  |  [ %s ] 중 오류가 있었습니다.");

    private String format;

    private Ingredient(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
