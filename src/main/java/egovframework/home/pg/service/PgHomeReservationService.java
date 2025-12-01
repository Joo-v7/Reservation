package egovframework.home.pg.service;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

public interface PgHomeReservationService {
    /**
     * 예약 - 예약 전체 조회 (status가 'APPROVED', 'PENDING' 인 예약만)
     */
    List<EgovMap> getReservationList(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 예약 - 예약 단일 조회 by reservation_id (PK)
     */
    EgovMap getReservation(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 예약 - 예약 merge
     */
    boolean setReservationMerge(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 예약 - 예약 전체 개수 조회
     */
    double getReservationTotalCnt(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 관리자 - 예약 전체 조회
     */
    List<EgovMap> getReservationListForAdmin(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 관리자 = 상태(status) 업데이트
     */
    boolean setUpdateReservationStatus(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 마이페이지 - 내 예약 데이터
     */
    List<EgovMap> getReservationListForMember(HashMap<String, Object> param) throws DataAccessException;

}
