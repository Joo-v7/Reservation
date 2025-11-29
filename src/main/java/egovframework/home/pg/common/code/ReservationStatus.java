package egovframework.home.pg.common.code;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    PENDING("승인대기"),
    APPROVED("승인완료"),
    REJECTED("반려"),
    CANCELLED("취소");

    private final String kor;

    ReservationStatus(String kor) {
        this.kor = kor;
    }

    public String getKor() {
        return kor;
    }

}
