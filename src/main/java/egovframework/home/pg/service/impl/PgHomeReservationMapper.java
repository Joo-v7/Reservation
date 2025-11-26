package egovframework.home.pg.service.impl;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface PgHomeReservationMapper {
    List<EgovMap> getReservationList(HashMap<String, Object> param) throws Exception;

    int setReservationMerge(HashMap<String, Object> param) throws Exception;

    boolean isDuplicatedReservation(HashMap<String, Object> param) throws Exception;

    EgovMap getReservationById(Long reservationId) throws Exception;

    List<EgovMap> getReservationListForAdmin(HashMap<String, Object> param) throws Exception;

    double getReservationTotalCnt(HashMap<String, Object> param) throws Exception;

    int setUpdateReservationStatus(HashMap<String, Object> param) throws Exception;
}
