package main;

import functions.HomeFunc;
import repository.MakeConnection;
import repository.MemberRepository;
import repository.ProjectRepository;




// [ Application 개요 ]
// - 프로그램 실행부, 딸깍

// [ 프로그램의 핵심 흐름 ]
// - Application.main 실행 -> HomeFunc.start 호출 -> 사용자의 기능 선택 -> 선택된 기능의 Func 메서드 호출

// [ Func 메서드들의 공통 흐름 ]
// - Message 제작 및 출력 -> 사용자의 입력 -> 입력값 검증 -> 관련 controller들 호출해 CRUD 수행 -> return으로 HomeFunc 복귀

// [ 메모 ]
// - 각 파일별로 간단한 설명을 작성해놓았습니다!
// - 예시를 위해 "업무 등록" 기능을 간단하게 구현해놓았읍니다! Application.java를 실행해 체험해볼 수 있습니다!
//   HomeFunc는 0번과 4번 선택지 제외하곤 정상 작동하지 않슴다...

public class Application {

    public static void main(String[] args) {
        HomeFunc.start();
    }
}