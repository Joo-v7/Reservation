package egovframework.home.pg.web.admin;

import egovframework.home.pg.common.code.MemberStatus;
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
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class PgHomeRoomAdminController {

    private final PgHomeRoomService pgHomeRoomService;

    /**
     * 회의실 관리 - 회의실 목록 페이지
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 회의실 목록 페이지
     * @throws Exception
     */
    @RequestMapping("/roomList.do")
    public String roomListForm(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        return "/home/pg/admin/roomList";
    }

    /**
     * 회의실 관리 - 회의실 데이터 리스트
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 회의실 데이터 리스트
     * @throws Exception
     */
    @RequestMapping("/getRoomList.do")
    public ResponseEntity<?> getRoomList(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
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
            double totalCnt = pgHomeRoomService.getRoomTotalCntForAdmin(param);

            // 전체 페이지수
            double pageCnt = Math.ceil(totalCnt / recordCnt);
            int totalPage = (int)(pageCnt > 0 ? pageCnt : 1);

            // 페이지 유효성 체크
            if (movePage > totalPage) {
                movePage = 1;
                param.put("movePage", movePage);
                param.put("limitStart", (movePage - 1) * (int)recordCnt);
            }

            // 회의실 리스트
            List<EgovMap> roomList = pgHomeRoomService.getRoomListForAdmin(param);

            listMap.put("list", roomList);
            listMap.put("page", movePage);
            listMap.put("pageCnt", pageCnt);
            listMap.put("totalCnt", totalCnt);

            retMap.put("error", "N");
            retMap.put("dataMap", listMap);

        } catch (DataAccessException | MultipartException | NullPointerException | IllegalArgumentException e) {
            log.error("== ADMIN == PgHomeMemberAdminController:getMemberList.do error={}", e.getMessage());
            throw e;
        }
        return ResponseEntity.status(HttpStatus.OK).body(retMap);

    }

    /**
     * 회의실 관리 - 회의실 단일 조회
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 회의실 단일 데이터
     * @throws Exception
     */
    @RequestMapping("/getRoom.do")
    public ResponseEntity<?> getRoom(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();

        try {
            EgovMap room = pgHomeRoomService.getRoomById(param);
            retMap.put("error", "N");
            retMap.put("dataMap", room);
        } catch (DataAccessException | MultipartException | NullPointerException | IllegalArgumentException e) {
            log.error("PgHomeReservationController:getRoom.do error={}", e.getMessage());
            throw e;
        }

        return ResponseEntity.status(HttpStatus.OK).body(retMap);
    }

    /**
     * 회의실 관리 - 회의실 Merge
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 회의실 Merge 결과
     * @throws Exception
     */
    @RequestMapping("/setRoomMerge.do")
    public ResponseEntity<?> setRoomMerge(
            HttpServletRequest req,
            HttpServletResponse res,
            ModelMap model,
            @RequestParam HashMap<String, Object> param,
            @RequestParam(value = "imageUrl", required = false) MultipartFile imageUrl
            ) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();

        try {
            // image
            if (imageUrl != null && !imageUrl.isEmpty()) {
                String projectPath = req.getServletContext().getRealPath("/attachment/image");
                log.info("=== 이미지 upload path => {}", projectPath);

                String originalFileName = imageUrl.getOriginalFilename();
                String savedName = UUID.randomUUID().toString();

                File uploadDir = new File(projectPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                File savedFile = new File(uploadDir, savedName);
                imageUrl.transferTo(savedFile);
                log.info("=== 이미지 upload check | saved file exists => {}", savedFile.exists());

                param.put("imageName", originalFileName);
                param.put("imageUrl", savedName);
            }

            if (pgHomeRoomService.setMergeRoom(param)) {
                retMap.put("error", "N");
                retMap.put("successTitle", "Success");
                retMap.put("successMsg", "성공적으로 저장되었습니다.");
            } else {
                retMap.put("error", "Y");
                retMap.put("errorTitle", "예약 저장");
                retMap.put("errorMsg", "데이터 처리 중 오류가 발생했습니다.");
            }
        } catch (DataAccessException | MultipartException | NullPointerException | IllegalArgumentException e) {
            log.error("== ADMIN == PgHomeRoomController:setMergeRoom.do error={}", e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(retMap);
    }

    /**
     * 회의실 관리 - 회의실 사용여부 업데이트
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 회의실 사용여부 업데이트 결과
     * @throws Exception
     */
    @RequestMapping("/setRoomUseYnUpdate.do")
    public ResponseEntity<?> setRoomUseYnUpdate(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();

        try {
            if (pgHomeRoomService.setRoomUseYnUpdate(param)) {
                retMap.put("error", "N");
                retMap.put("successTitle", "Success");
                retMap.put("successMsg", "성공적으로 저장되었습니다.");
            } else {
                retMap.put("error", "Y");
                retMap.put("errorTitle", "예약 저장");
                retMap.put("errorMsg", "데이터 처리 중 오류가 발생했습니다.");
            }
        } catch (DataAccessException | MultipartException | NullPointerException | IllegalArgumentException e) {
            log.error("== ADMIN == PgHomeRoomController:setRoomUseYnUpdate.do error={}", e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(retMap);
    }

    /**
     * 이미지 다운로드
     * @param req
     * @param res
     * @param param
     * @throws Exception
     */
    @RequestMapping("/room/image.do")
    public void downloadImage(
            HttpServletRequest req,
            HttpServletResponse res,
            @RequestParam HashMap<String, Object> param
    ) throws Exception {

        String imageUrl = StringUtils.stripToEmpty((String) param.get("imageUrl"));
        String imageName = StringUtils.stripToEmpty((String) param.get("imageName"));

        // 내부 실제 저장 폴더
        String path = req.getServletContext().getRealPath("/attachment/image");
        log.info("=== image real path => {}", path);

        File file = new File(path, imageUrl);

        if (!file.exists()) {
            String errorMsg = "이미지가 존재하지 않습니다";

            req.getSession().setAttribute("errorMsg", errorMsg);

            String backUrl = req.getHeader("Referer");
            if (backUrl != null) {
                res.sendRedirect(backUrl);
            }

            // 관리자면 어디에??

            return;

        }

        // 다운로드 헤더 세팅
        String encodedName = URLEncoder.encode(imageName, "UTF-8").replace("+", "%20");
        String cd = "attachment; filename=\"" + encodedName + "\"; filename*=UTF-8''" + encodedName;

        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", cd);
        res.setHeader("Content-Length", String.valueOf(file.length()));

        try (OutputStream os = res.getOutputStream()) {
            Files.copy(file.toPath(), os);
            os.flush();
        }
    }


}
