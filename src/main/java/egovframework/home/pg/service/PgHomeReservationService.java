package egovframework.home.pg.service;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

public interface PgHomeReservationService {
    List<EgovMap> getReservationList(HashMap<String, Object> param) throws Exception;
    EgovMap getReservationById(Long reservationId) throws Exception;
    boolean setReservationMerge(HashMap<String, Object> param) throws Exception;
    List<EgovMap> getReservationListForAdmin(HashMap<String, Object> param) throws Exception;
    double getReservationTotalCnt(HashMap<String, Object> param) throws Exception;
    boolean setUpdateReservationStatus(HashMap<String, Object> param) throws Exception;
    /**
     * 마이페이지 - 내 예약 데이터
     */
    List<EgovMap> getReservationListForMember(HashMap<String, Object> param) throws DataAccessException;

}
