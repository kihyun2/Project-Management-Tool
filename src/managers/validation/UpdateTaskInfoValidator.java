package managers.validation;

import configs.validation.FailureReason;
import configs.validation.RegEx;
import utils.Pair;

import java.util.regex.Pattern;

// [ UpdateTaskInfoValidator 클래스 설명 ]
// - UpdateTaskInfoValidator는 "업무정보수정"의 입력값에 대한 유효성 검사용 클래스입니다.

public class UpdateTaskInfoValidator extends Validator {
    @Override
    public Pair<Boolean, String> check(String target) {
        // 업무ID / 업무명 / 상태 / 담당자명 / 마감일

        // [1] 일차적으로 입력값의 구조를 점검 ("문자열1/문자열2/문자열3/문자열4")
        boolean isRightStructure = Pattern.matches(RegEx.UPDATE_TASK_INFO_STRUCTURE.getPattern(), target);
        if (!isRightStructure) {
            return new Pair<>(false, FailureReason.UPDATE_TASK_INFO_STRUCTURE.getReason());
        }

        // [2] 항목별 유효성 검사 진행
        String[] fields = target.split("/");

        boolean isRightId = Pattern.matches(RegEx.UPDATE_TASK_INFO_TID.getPattern(), fields[0]);
        if (!isRightId) {
            return new Pair<>(false, FailureReason.UPDATE_TASK_INFO_TID.getReason());
        }
        boolean isRightName = Pattern.matches(RegEx.UPDATE_TASK_INFO_NAME.getPattern(), fields[1]);
        if (!isRightName) {
            return new Pair<>(false, FailureReason.UPDATE_TASK_INFO_NAME.getReason());
        }
        boolean isRightStatus = Pattern.matches(RegEx.UPDATE_TASK_INFO_STATUS.getPattern(), fields[2]);
        if (!isRightStatus) {
            return new Pair<>(false, FailureReason.UPDATE_TASK_INFO_STATUS.getReason());
        }
        String[] mids = fields[3].split(",");
        for(String mid : mids){
            boolean isRightMember = Pattern.matches(RegEx.UPDATE_TASK_INFO_ASSIGNEE.getPattern(), mid);
            if (!isRightMember) {
                return new Pair<>(false, FailureReason.UPDATE_TASK_INFO_ASSIGNEE.getReason());
            }

        }
        boolean isRightDue = Pattern.matches(RegEx.UPDATE_TASK_INFO_DUE.getPattern(), fields[4]);
        if (!isRightDue) {
            return new Pair<>(false, FailureReason.UPDATE_TASK_INFO_DUE.getReason());
        }

        // [3] 검증된 입력값을 Pair 클래스로 감싸 반환한다.
        return new Pair<>(true, target);
    }
}