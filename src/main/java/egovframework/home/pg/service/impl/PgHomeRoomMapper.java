package egovframework.home.pg.service.impl;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface PgHomeRoomMapper {
    List<EgovMap> getRoomList(HashMap<String, Object> param) throws DataAccessException;
    Double getRoomTotalCnt(HashMap<String, Object> param) throws DataAccessException;
    EgovMap getRoom(HashMap<String, Object> param) throws DataAccessException;
}
