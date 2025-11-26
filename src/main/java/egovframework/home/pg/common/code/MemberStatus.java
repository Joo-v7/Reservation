package egovframework.home.pg.common.code;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum MemberStatus {
    ACTIVE("활성화"),
    INACTIVE("비활성화"),
    DELETED("삭제");

    private final String kor;

    private MemberStatus(String kor) {
        this.kor = kor;
    }

    public String getKor() {
        return kor;
    }

    public static String toKor(String status) {
        if (StringUtils.isEmpty(status)) {
            return null;
        }

        try {
            return MemberStatus.valueOf(status.toUpperCase()).getKor();
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 회원 상태입니다." + status);
        }


    }

}
