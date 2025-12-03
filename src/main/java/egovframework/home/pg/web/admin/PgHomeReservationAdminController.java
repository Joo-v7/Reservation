package egovframework.home.pg.web.admin;

import egovframework.home.pg.common.code.ReservationStatus;
import egovframework.home.pg.service.PgHomeReservationService;
import egovframework.home.pg.service.PgHomeRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class PgHomeReservationAdminController {

    private final PgHomeReservationService pgHomeReservationService;
    private final PgHomeRoomService pgHomeRoomService;

    /**
     * 예약관리 페이지
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 예약관리 페이지
     * @throws Exception
     */
    @RequestMapping("/reservationList.do")
    public String reservationList(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        List<HashMap<String, String>> statusList = new ArrayList<>();

        // 예약 상태
        for (ReservationStatus status : ReservationStatus.values()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("code", status.name()); // 'PENDING',  'APPROVED', 'REJECTED', ‘CANCELLED'
            map.put("name", status.getKor()); // 승인대기, 승인완료, 반려, 취소
            statusList.add(map);
        }

        model.put("statusList", statusList);

        return "home/pg/admin/reservationList";
    }

    /**
     * 예약 데이터 리스트
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 예약 데이터 리스트
     * @throws Exception
     */
    @RequestMapping("/getReservationList.do")
    public ResponseEntity<?> getReservationList(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();
        HashMap<String, Object> listMap = new HashMap<>();

        try {
            // 페이지 번호
            Object movePageObject = param.get("movePage");
            int movePage = ObjectUtils.isEmpty(movePageObject) ? 1 : NumberUtils.toInt(movePageObject.toString());
            param.put("movePage", movePage);

            // 한페이지 레코드 개수
            double recordCnt = NumberUtils.toDouble((String) param.get("recordCnt"),10);
            if (StringUtils.isNotEmpty((String) param.get("recordCnt"))) recordCnt = NumberUtils.toDouble((String) param.get("recordCnt"));
            param.put("recordCnt", (int)recordCnt);

            // limit 시작 개수 (Mariadb, mySql)
            param.put("limitStart", ((movePage - 1) * (int)recordCnt));

            // 전체 개수
            double totalCnt = pgHomeReservationService.getReservationTotalCnt(param);

            // 전체 페이지수
            double pageCnt = Math.ceil(totalCnt / recordCnt);
            int totalPage = (int)(pageCnt > 0 ? pageCnt : 1);

            // 페이지 유효성 체크
            if (movePage > totalPage) {
                movePage = 1;
                param.put("movePage", movePage);
                param.put("limitStart", (movePage - 1) * (int)recordCnt);
            }

            List<HashMap<String, String>> statusList = new ArrayList<>();

            // 예약 상태
            for (ReservationStatus status : ReservationStatus.values()) {
                HashMap<String, String> map = new HashMap<>();
                map.put("code", status.name());       // 'PENDING',  'APPROVED', 'REJECTED', ‘CANCELLED'
                map.put("name", status.getKor());  // 승인대기, 승인완료, 반려, 취소
                statusList.add(map);
            }

            // 예약 리스트
            List<EgovMap> reservationList = pgHomeReservationService.getReservationListForAdmin(param);

            listMap.put("list", reservationList);
            listMap.put("statusList", statusList);
            listMap.put("page", movePage);
            listMap.put("pageCnt", pageCnt);
            listMap.put("totalCnt", totalCnt);

            retMap.put("error", "N");
            retMap.put("dataMap", listMap);

        } catch (DataAccessException | MultipartException | NullPointerException | IllegalArgumentException e) {
            log.error("== ADMIN == PgHomeReservationAdminController:getReservationList.do error={}", e.getMessage());
            throw e;
        }
        return ResponseEntity.status(HttpStatus.OK).body(retMap);
    }

    /**
     * 예약 상태 업데이트
     * @param req
     * @param res
     * @param param
     * @return 예약 상태 업데이트 결과
     * @throws Exception
     */
    @RequestMapping("/setUpdateReservationStatus.do")
    public ResponseEntity<?> setUpdateReservationStatus(HttpServletRequest req, HttpServletResponse res, @RequestParam HashMap<String, Object> param) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();

        try {
            if (pgHomeReservationService.setUpdateReservationStatus(param)) {
                retMap.put("error", "N");
                retMap.put("successTitle", "Success");
                retMap.put("successMsg", "성공적으로 저장되었습니다.");
            } else {
                retMap.put("error", "Y");
                retMap.put("errorTitle", "예약 저장");
                retMap.put("errorMsg", "데이터 처리 중 오류가 발생했습니다.");
            }

        } catch (DataAccessException | MultipartException | NullPointerException | IllegalArgumentException e) {
            log.error("== ADMIN == PgHomeBoardController:setUpdateReservationStatus.do error={}", e.getMessage());
            throw e;
        }

        return ResponseEntity.status(HttpStatus.OK).body(retMap);
    }


}
