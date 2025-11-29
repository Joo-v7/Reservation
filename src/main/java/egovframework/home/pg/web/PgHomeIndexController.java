package egovframework.home.pg.web;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PgHomeIndexController {

    private final PgHomeRoomService pgHomeRoomService;

    /**
     * 홈 - 페이지
     * @return
     */
    @GetMapping("/index.do")
    public String index() {
        return "home/pg/home";
    }

    /**
     * 홈 - 데이터
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 홈페이지 구성에 필요한 데이터 (roomList)
     * @throws Exception
     */
    @RequestMapping("/getIndex.do")
    public ResponseEntity<?> getIndex(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
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
            double totalCnt = pgHomeRoomService.getRoomTotalCnt(param);

            // 전체 페이지수
            double pageCnt = Math.ceil(totalCnt / recordCnt);
            int totalPage = (int)(pageCnt > 0 ? pageCnt : 1);

            // 페이지 유효성 체크
            if (movePage > totalPage) {
                movePage = 1;
                param.put("movePage", movePage);
                param.put("limitStart", (movePage - 1) * (int)recordCnt);
            }

            List<EgovMap> roomList = pgHomeRoomService.getRoomList(param);

            listMap.put("list", roomList);
            listMap.put("page", movePage);
            listMap.put("pageCnt", pageCnt);
            listMap.put("totalCnt", totalCnt);

            retMap.put("error", "N");
            retMap.put("dataMap", listMap);

            // 전역 에러
        } catch (DataAccessException | NullPointerException e) {
            log.error("PgHomeBoardController:getBoardList.do error={}", e.getMessage());
            throw e;
        }

        return ResponseEntity.status(HttpStatus.OK).body(retMap);
    }

    /**
     * 관리자 - 홈 페이지
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/admin/index.do")
    public String adminHome() {
        return "home/pg/admin/index";
    }

}
