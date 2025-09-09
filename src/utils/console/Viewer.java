package utils.console;

public class Viewer {

    public static void print(String msg) {
        System.out.println(msg);
    }

    public static void clear() {
        try {
            // 파워쉘에서도 작동하는 방법
            if (System.getProperty("").toLowerCase().contains("windows")) {
                // 윈도우에서는 ProcessBuilder 사용
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // 다른 OS에서는 ANSI 이스케이프 코드
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // 실패하면 단순히 줄바꿈으로 대체
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}

// [ Viewer 클래스 설명 ]
// - Viewer는 콘솔 화면을 코드상에서 조작하기 위한 클래스임다.
// - print() 메서드는 문자열을 받아 println으로 콘솔화면에 출력합니다.
// - clear() 메서드는 100회의 줄바꿈을 통해 보여지는 콘솔화면을 정돈합니다.