package managers.conversion;

import configs.project.TaskType;
import model.team.Team;

// [ StringTaskTypeConverter 클래스 설명 ]
// - StringTaskTypeConverter는 String과 TaskType 타입 간의 변환용 클래스입니다.

public class StringTaskTypeConverter extends Converter<String, TaskType>{
    String[] type = {"기획","개발","디버그","기타"};

    @Override
    public TaskType convertTo(String target) {
        // [메모] 입력은 1234로 받기 때문에, 인덱스로는 1을 감산해줘야 함
        int i = 1;
        for(String s : type){
            if(s.equals(target)){
                target = i+"";
                break;
            }
            i++;
        }
        return TaskType.values()[Integer.parseInt(target)-1];

    }

    @Override
    public String convertFrom(TaskType target) {
        return type[target.ordinal()];
    }
}
