package egovframework.home.pg.service.impl;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface PgHomeReservationMapper {
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
    int setReservationMerge(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 예약 - 예약 전체 개수 조회
     */
    double getReservationTotalCnt(HashMap<String, Object> param) throws DataAccessException;


    /**
     * 예약 - 예약 중복
     */
    boolean isDuplicatedReservation(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 관리자 - 예약 전체 조회
     */
    List<EgovMap> getReservationListForAdmin(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 관리자 = 상태(status) 업데이트
     */
    int setUpdateReservationStatus(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 마이페이지 - 내 예약 데이터
     */
    List<EgovMap> getReservationListForMember(HashMap<String, Object> param) throws DataAccessException;

}
