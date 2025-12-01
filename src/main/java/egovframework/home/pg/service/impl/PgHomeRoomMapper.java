package egovframework.home.pg.service.impl;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface PgHomeRoomMapper {
    /**
     * 사용가능한 회의실 조건 조회
     */
    List<EgovMap> getRoomList(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 사용가능한 회의실 전체 개수
     */
    Double getRoomTotalCnt(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 회의실 단일 조회
     */
    EgovMap getRoomById(HashMap<String, Object> param) throws DataAccessException;
}
