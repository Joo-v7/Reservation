package egovframework.home.pg.service.impl;

import egovframework.home.pg.exception.ConflictException;
import egovframework.home.pg.service.PgHomeReservationService;
import egovframework.home.pg.service.PgHomeRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PgHomeReservationServiceImpl implements PgHomeReservationService {

    private final PgHomeReservationMapper pgHomeReservationMapper;
    private final PgHomeRoomService pgHomeRoomService;

    @Override
    public List<EgovMap> getReservationList(HashMap<String, Object> param) throws Exception {
        return pgHomeReservationMapper.getReservationList(param);
    }

    @Override
    public EgovMap getReservationById(Long reservationId) throws Exception {
        return pgHomeReservationMapper.getReservationById(reservationId);
    }

    @Transactional
    @Override
    public boolean setReservationMergeOnce(HashMap<String, Object> param) throws Exception {
        if(pgHomeReservationMapper.isDuplicatedReservation(param)) {
            throw new ConflictException("중복된 예약입니다.");
        }

        int result = pgHomeReservationMapper.setReservationMerge(param);

        return result > 0;
    }

    @Transactional
    @Override
    public boolean setReservationMergeRegular(HashMap<String, Object> param) throws Exception {
        String startDateStr = StringUtils.stripToEmpty((String) param.get("startDate"));
        String endDateStr = StringUtils.stripToEmpty((String) param.get("endDate"));
        String daysOfWeek = StringUtils.stripToEmpty((String) param.get("daysOfWeek"));

        // TODO 유효성 검사 + 참석인원이 회의실 용량 이하 인지확인

        if(pgHomeReservationMapper.isDuplicatedReservation(param)) {
            throw new ConflictException("중복된 예약입니다.");
        }

        int result = pgHomeReservationMapper.setReservationMerge(param);

        return result > 0;
    }

    /**
     * ============== 관리자 영역 ==============
     */

    @Override
    public List<EgovMap> getReservationListForAdmin(HashMap<String, Object> param) throws Exception {
        return pgHomeReservationMapper.getReservationListForAdmin(param);
    }

    @Override
    public double getReservationTotalCnt(HashMap<String, Object> param) throws Exception {
        return pgHomeReservationMapper.getReservationTotalCnt(param);
    }

    @Transactional
    @Override
    public boolean setUpdateReservationStatus(HashMap<String, Object> param) throws Exception {
        int result = pgHomeReservationMapper.setUpdateReservationStatus(param);

        return result > 0;
    }
}
