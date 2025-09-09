package controller.controllers;

import configs.message.Ingredient;
import configs.project.TaskStatus;
import configs.project.TaskType;
import controller.*;
import managers.ConverterManager;
import model.project.Project;
import model.project.Task;
import model.team.Member;
import model.team.Team;
import repository.MemberRepository;
import repository.ProjectRepository;
import repository.ProjectTeamRepository;
import utils.LogRecorder;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// [ ProjectController 클래스 설명 ]
// - ProjectController는 Task 인스턴스들에 대한 CRUD 조작을 처리하기 위한 Controller 기반의 클래스임다.
// - Project 클래스를 통해서만 인스턴스를 생성하고, Project의 HashMap 필드인 tasks를 참조하는 종속성을 갖슴다.

// [ 메모 ]
// - add() 메서드 실행 마다 TID를 생성하고, index 필드의 값을 1씩 implement 해야 합니다.
// - Task 인스턴스 생성 시 입력값이 보류된 필드에 대한 처리 방법의 고민이 필요합니다.
//      ex) "비품구매/4/@/20270722" -> 보류된 assignee 필드에 null을 할당...? Optional.empty()...?
// - "업무조회" 기능 관련해, 보류된 필드에 대한 출력값 설정이 필요합니다.
//      ex) name=비품구매/type=기타/status=진행/assignee=null" -> 콘솔화면에서 assignee는 "미정"으로...?

public class ProjectController extends Controller implements Adder<Task>, Getter<Task>, Updater, Remover {
    private Map<String, Task> tasks;
    private long index = 1;

    public ProjectController(Map<String, Task> tasks) {
        this.tasks = tasks;
    }

    /* Create 담당 */
    @Override
    public Task add(String[] infos) {
        // infos = 업무명 / 유형 / 담당자ID / 마감일
        // 자료형 = String / TaskType / Member / LocalDate

        // [1] 항목별로 Task의 각 필드타입에 맞게 convert
        String tid = createId();
        String name = infos[0];
        TaskType type = ConverterManager.stringTaskType.convertTo(infos[1]);
        // [1-2] 해당 프로젝트에 담당 팀원 배정
        if(!infos[2].equals("@")) {
            String[] mids =  infos[2].split(",");
            changeProjectTeam(tid, mids);
        }
        // 방금 등록한 프로젝트라서 NOT_STARTED로 초기화
        TaskStatus status = TaskStatus.values()[0];
        LocalDate dueTo = infos[3].equals("@")
                ? null
                : ConverterManager.stringDate.convertTo(infos[3]);

        // [2] 신규 Task 인스턴스 생성
        Task task = new Task(tid, name, type, status, dueTo);

        // [3] Projects 테이블(DB)에 업무 저장
        try {
            ProjectRepository.getInstance().save(task);
        } catch (SQLException e) {
            LogRecorder.record(Ingredient.LOG_ERROR_SQL,"add-save()");
            e.printStackTrace();
        }
        return task;
    }

    /* Update 담당 */
    @Override
    public void update(String[] changes) {
        // changes = TID / 업무명 / 상태 / 담당자ID / 마감일
        // 자료형 = String / String / TaskStatus / Member / LocalDate

        // [1] TID로 해당 Task 인스턴스 찾아오기
        String tid = changes[0];
        if(!ProjectRepository.getInstance().existsById(tid)){return;}
        Task targetTask = get(tid);
        // 생략 기호("@") 확인 후 이전 값 혹은 변경값 선택
        // 업무명
        String name = changes[1].equals("@")? targetTask.getName():changes[1];
        // 업무 상태
        TaskStatus status = changes[2].equals("@")? targetTask.getStatus():ConverterManager.stringTaskStatus.convertTo(changes[2]);
        // 업무 마감일
        LocalDate dueTo = changes[4].equals("@")? targetTask.getDueTo():ConverterManager.stringDate.convertTo(changes[3]);
        // [1-2] 변경된 팀원들로 변경
        if(!changes[3].equals("@")) {
            String[] mids =  changes[3].split(",");
            changeProjectTeam(tid, mids);
        }

        // [2] 입력값 바탕으로 각 필드 수정
        targetTask.setName(name);
        targetTask.setStatus(status);
        targetTask.setDueTo(dueTo);
        // [2-1] 필드가 수정된 객체를 DB에 저장
        try {
            ProjectRepository.getInstance().update(targetTask);
        } catch (SQLException e) {
            LogRecorder.record(Ingredient.LOG_ERROR_SQL,"update-update()");
        }
        // [3] 해당 Task의 updatedAt 필드 최신화
        targetTask.updateTime();
    }

    private void changeProjectTeam(String tid, String[] mids) {
        for (String mid : mids){
            Member member = null;
            try{
                member = MemberRepository.getInstance().findById(mid);
            } catch (SQLException e) {
                LogRecorder.record(Ingredient.LOG_ERROR_SQL,"changeProjectTeam-findById()");
            }
            if(member != null){
                if(!ProjectTeamRepository.getInstance().exists(tid,mid)){
                    try{ProjectTeamRepository.getInstance().addMemberToProject(tid,mid);}catch (SQLException e){
                        LogRecorder.record(Ingredient.LOG_ERROR_SQL,"changeProjectTeam-addMemberToProject()");
                    }
                }
            }
        }
    }

    /* Read 담당 */
    @Override
    public Task get(String tid) {
        try{
            return ProjectRepository.getInstance().findById(tid);
        }catch (SQLException e){
            LogRecorder.record(Ingredient.LOG_ERROR_SQL,"get-findById()");
            e.printStackTrace();
            return null;
        }
    }

    /* Delete 담당 */
    @Override
    public void remove(String tid) {
        try {
            ProjectRepository.getInstance().deleteById(tid);
        } catch (SQLException e) {
            LogRecorder.record(Ingredient.LOG_ERROR_SQL,"remove-findById()");
        }
    }

    /* 조건에 부합하는 Task의 정보를 추출하는 메서드 */
    public Stream<Task> browse(String[] inputs) {
        // [1] tasks에 대한 스트림 시작
        Stream<Task> filtering = this.getAll().stream();

        // [2] 입력받은 기준들을 순회하며 필터링 반복
        for (String input : inputs) {
            // [Loop-1] 입력값을 통해 조회기준과 조건을 추출
            String[] field = input.split(",");
            String criteria = field[0];
            String condition = field[1];

            // [Loop-2] 기준별로 조건에 부합하지 않는 Task 걸러내기
            switch (criteria) {
                case "1": // 업무유형 비교
                    filtering = filtering.filter(a -> a.getType() == ConverterManager.stringTaskType.convertTo(condition));
                    break;
                case "2": // 업무상태 비교
                    filtering = filtering.filter(a -> a.getStatus() == ConverterManager.stringTaskStatus.convertTo(condition));
                    break;
                case "3": // 담당자 비교
                    filtering = filtering.filter(a -> {
                        Member assignee = a.getAssignee(); // [메모] null 체크를 하지 않으면 무조건 에러 발생
                        return assignee != null && assignee.getMid().equals(condition);
                    });
                    break;
                default:
                    // [메모] 여기로 올 일이 없어서 우째야 하나 고민
                    // throw new Exception();
            }
        }

        // [3] 필터링 마친 Task들을 스트림 형태로 반환
        return filtering;
    }

    /*  현존 Task들의 유형별 개수 세기 (홈화면 overview에 활용) */
    public List<String> countTasksByStatus() {
        // [1] 유형별로 셀 수 있는 변수 준비
        int[] count = {0, 0, 0, ProjectRepository.getInstance().count()};
        // [2] task들 순회하면서 유형별로 count (출력 순서는 완료-진행-대기라서 인덱스를 뒤집어줘야 함)
        // [수정예정] 상수 2에서 빼는 방식 말고, reverse든 뭐든 방법을 찾아서 뒤집기
        this.getAll().forEach(task -> count[2-task.getStatus().ordinal()] += 1);
        // [3] 각 갯수 모은 int 배열을 String List로 변환해 반환
        // [메모] 바로 toList() 하면 그 List는 Immutable이라서, Collectors.toList()를 해야 add할 수 있음
        return Arrays.stream(count).mapToObj(String::valueOf).collect(Collectors.toList());
    }

    public Collection<Task> getAll() {
        try {
            return ProjectRepository.getInstance().findAll();
        } catch (SQLException e) {
            LogRecorder.record(Ingredient.LOG_ERROR_SQL,"getAll-findAll()");
            return null;
        }
    }

    private String createId() {
        return index < 10 ? "t0" + index++ : "t" + index++;
    }
}
