package egovframework.home.pg.service.impl;

import egovframework.home.pg.exception.ConflictException;
import egovframework.home.pg.service.PgHomeReservationService;
import egovframework.home.pg.service.PgHomeRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PgHomeReservationServiceImpl implements PgHomeReservationService {

    private final PgHomeReservationMapper pgHomeReservationMapper;

    /**
     * 예약 - 예약 전체 조회 (status가 'APPROVED', 'PENDING' 인 예약만)
     * @param param
     * @return 예약 데이터 리스트
     * @throws DataAccessException
     */
    @Override
    public List<EgovMap> getReservationList(HashMap<String, Object> param) throws DataAccessException {
        return pgHomeReservationMapper.getReservationList(param);
    }

    /**
     * 예약 - 예약 단일 조회 by reservation_id (PK)
     * @param param
     * @return 예약 단일 데이터
     * @throws DataAccessException
     */
    @Override
    public EgovMap getReservation(HashMap<String, Object> param) throws DataAccessException {
        return pgHomeReservationMapper.getReservation(param);
    }

    /**
     * 예약 - 예약 merge
     * @param param
     * @return 성공 여부
     * @throws DataAccessException
     */
    @Transactional
    @Override
    public boolean setReservationMerge(HashMap<String, Object> param) throws DataAccessException {
        if(pgHomeReservationMapper.isDuplicatedReservation(param)) {
            throw new ConflictException("중복된 예약입니다.");
        }

        int result = pgHomeReservationMapper.setReservationMerge(param);

        return result > 0;
    }

    /**
     * 예약 - 예약 전체 개수 조회
     * @param param
     * @return 예약 전체 개수
     * @throws DataAccessException
     */
    @Override
    public double getReservationTotalCnt(HashMap<String, Object> param) throws DataAccessException {
        return pgHomeReservationMapper.getReservationTotalCnt(param);
    }

    /**
     * 예약 - 내 예약 데이터 리스트
     * @param param
     * @return 내 예약 데이터 리스트
     * @throws DataAccessException
     */
    @Override
    public List<EgovMap> getReservationListForMember(HashMap<String, Object> param) throws DataAccessException {
        return pgHomeReservationMapper.getReservationListForMember(param);
    }

    /**
     * ============== 관리자 영역 ==============
     */

    /**
     * 관리자 - 예약 전체 조회
     * @param param
     * @return 예약 데이터 리스트
     * @throws DataAccessException
     */
    @Override
    public List<EgovMap> getReservationListForAdmin(HashMap<String, Object> param) throws DataAccessException {
        return pgHomeReservationMapper.getReservationListForAdmin(param);
    }

    /**
     * 관리자 - 예약 상태 업데이트
     * @param param
     * @return 상태 업데이트 결과 여부
     * @throws DataAccessException
     */
    @Transactional
    @Override
    public boolean setUpdateReservationStatus(HashMap<String, Object> param) throws DataAccessException {
        int result = pgHomeReservationMapper.setUpdateReservationStatus(param);

        return result > 0;
    }
}
