package managers.conversion;

import configs.team.Authority;

// [StringAuthorityConverter 클래스]
// - String을 Authorization으로 변환
public class StringAuthorityConverter extends Converter<String, Authority> {

    @Override
    public Authority convertTo(String target) {
        return Authority.values()[Integer.parseInt(target)-1];
    }

    @Override
    public String convertFrom(Authority target) {
        return Integer.toString(target.ordinal()+1);
    }
}
