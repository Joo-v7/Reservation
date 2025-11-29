package egovframework.home.pg.service.impl;

import egovframework.home.pg.service.PgHomeRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PgHomeRoomServiceImpl implements PgHomeRoomService {

    private final PgHomeRoomMapper pgHomeRoomMapper;

    @Override
    public List<EgovMap> getRoomList(HashMap<String, Object> param) throws DataAccessException {
        return pgHomeRoomMapper.getRoomList(param);
    }

    @Override
    public EgovMap getRoom(HashMap<String, Object> param) throws DataAccessException {
        return pgHomeRoomMapper.getRoom(param);
    }

    @Override
    public Double getRoomTotalCnt(HashMap<String, Object> param) throws DataAccessException {
        return pgHomeRoomMapper.getRoomTotalCnt(param);
    }
}
