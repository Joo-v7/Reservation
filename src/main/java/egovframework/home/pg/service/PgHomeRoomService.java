package egovframework.home.pg.service;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

public interface PgHomeRoomService {
    List<EgovMap> getRoomList(HashMap<String, Object> param) throws DataAccessException;
    EgovMap getRoom(HashMap<String, Object> param) throws DataAccessException;
    Double getRoomTotalCnt(HashMap<String, Object> param) throws DataAccessException;
}
