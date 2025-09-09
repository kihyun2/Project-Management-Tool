package managers.validation;

import configs.validation.FailureReason;
import configs.validation.RegEx;
import utils.Pair;

import java.util.regex.Pattern;

public class RemoveMembersValidator extends Validator {

    @Override
    public Pair<Boolean, String> check(String target) {
        // target = Mid
        // [1] 입력값 검사
        boolean isMidValid =  Pattern.matches(RegEx.MEMBER_MID.getPattern(),  target);
        if (!isMidValid) {
            return new Pair<>(false, FailureReason.MEMBER_MID.getReason());
        }
        // [2] 통과되면 true, target 쌍 반환
        return new Pair<>(true, target);
    }
}
