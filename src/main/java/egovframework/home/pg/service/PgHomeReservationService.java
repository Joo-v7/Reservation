package egovframework.home.pg.service;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import java.util.HashMap;
import java.util.List;

public interface PgHomeReservationService {
    List<EgovMap> getReservationList(HashMap<String, Object> param) throws Exception;
    EgovMap getReservationById(Long reservationId) throws Exception;
    boolean setReservationMergeOnce(HashMap<String, Object> param) throws Exception;
    boolean setReservationMergeRegular(HashMap<String, Object> param) throws Exception;
    List<EgovMap> getReservationListForAdmin(HashMap<String, Object> param) throws Exception;
    double getReservationTotalCnt(HashMap<String, Object> param) throws Exception;
    boolean setUpdateReservationStatus(HashMap<String, Object> param) throws Exception;
}
