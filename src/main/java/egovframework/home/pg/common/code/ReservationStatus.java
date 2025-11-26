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

    public static ReservationStatus from(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }

        // 영문 매칭
        for (ReservationStatus status : values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }

        // 한글 매칭
        for (ReservationStatus status : ReservationStatus.values()) {
            if (status.getKor().equals(s)) {
                return status;
            }
        }

        throw new IllegalArgumentException("유효하지 않은 예약 상태입니다." + s);
    }

}
