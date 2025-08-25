package managers.conversion;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// [ StringDateConverter 클래스 설명 ]
// - StringDateConverter는 String과 LocalDate 타입 간의 변환용 클래스입니다.

public class StringDateConverter extends Converter<String, LocalDate> {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public LocalDate convertTo(String target) { return LocalDate.parse(target, formatter); }
    @Override
    public String convertFrom(LocalDate target) {
        return formatter.format(target);
    }
}