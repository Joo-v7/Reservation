package egovframework.home.pg.web;

import egovframework.home.pg.common.code.ReservationStatus;
import egovframework.home.pg.common.security.user.PrincipalDetails;
import egovframework.home.pg.service.PgHomeReservationService;
import egovframework.home.pg.service.PgHomeRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.faces.annotation.RequestMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PgHomeReservationController {

    private final PgHomeReservationService pgHomeReservationService;
    private final PgHomeRoomService pgHomeRoomService;

    @RequestMapping("/reservationList.do")
    public String reservationList(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        List<HashMap<String, String>> statusList = new ArrayList<>();

        // 예약 상태
        for (ReservationStatus status : ReservationStatus.values()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("code", status.name());       // 'PENDING',  'APPROVED', 'REJECTED', ‘CANCELLED'
            map.put("name", status.getKor());  // 승인대기, 승인완료, 반려, 취소
            statusList.add(map);
        }

        model.put("reservationStatusList", statusList);

        return "home/pg/reservationList";
    }

    @RequestMapping("/getReservationList.do")
    public ResponseEntity<?> getReservationList(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();

        try {

            List<EgovMap> reservationList = pgHomeReservationService.getReservationList(param);
            retMap.put("error", "N");
            retMap.put("dataMap", reservationList);

        } catch (DataAccessException | MultipartException | NullPointerException | IllegalArgumentException e) {
            log.error("PgHomeReservationController:getReservationList.do error={}", e.getMessage());
            throw e;
        }
        return ResponseEntity.status(HttpStatus.OK).body(retMap);
    }

    @RequestMapping("/getReservationById.do")
    public ResponseEntity<?> getReservationById(HttpServletRequest req, HttpServletResponse res, @RequestParam Long memberId) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();

        try {

            EgovMap reservation = pgHomeReservationService.getReservationById(memberId);
            retMap.put("error", "N");
            retMap.put("dataMap", reservation);

        } catch (DataAccessException | MultipartException | NullPointerException | IllegalArgumentException e) {
            log.error("PgHomeReservationController:getReservationById.do error={}", e.getMessage());
            throw e;
        }

        return ResponseEntity.status(HttpStatus.OK).body(retMap);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/reservation.do")
    public String reservationForm(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        List<EgovMap> roomList = pgHomeRoomService.getRoomList(param);
        model.addAttribute("roomList", roomList);

        return "home/pg/reservation";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/setReservationMerge.do")
    public ResponseEntity<?> setReservationMerge(
            HttpServletRequest req,
            HttpServletResponse res,
            ModelMap model,
            @RequestParam HashMap<String, Object> param,
            @RequestParam(value = "attachment", required = false) MultipartFile attachment,
            @RequestParam(value = "daysOfWeek", required = false) String[] daysOfWeeks,
            Authentication authentication
    ) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();

        try {
            // member_id
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            param.put("memberId", principalDetails.getId());

            // attachment
            if (attachment != null && !attachment.isEmpty()) {
                String projectPath = req.getServletContext().getRealPath("/attachment/reservation");
                log.info("=== 첨부파일 upload path => {}", projectPath);

                String originalFilename = attachment.getOriginalFilename();
                String savedName = UUID.randomUUID() + "_" + originalFilename;

                File uploadDir = new File(projectPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                File savedFile = new File(uploadDir, savedName);
                attachment.transferTo(savedFile);
                log.info("=== 첨부파일 upload check | saved file exists => {}", savedFile.exists());

                param.put("attachment", savedName);

            }

            // daysOfWeek
            if (daysOfWeeks != null && daysOfWeeks.length > 0) {
                String daysOfWeek = String.join(",", daysOfWeeks);
                param.put("daysOfWeek", daysOfWeek);
            }

            if (pgHomeReservationService.setReservationMerge(param)) {
                retMap.put("error", "N");
                retMap.put("successTitle", "Success");
                retMap.put("successMsg", "성공적으로 저장되었습니다.");
            } else {
                retMap.put("error", "Y");
                retMap.put("errorTitle", "예약 저장");
                retMap.put("errorMsg", "예약 저장 중 오류가 발생했습니다.");
            }

        } catch (DataAccessException | MultipartException | NullPointerException | IllegalArgumentException e) {
            log.error("PgHomeReservationController:setReservationMerge.do error={}", e.getMessage());
            throw e;
        }

        return ResponseEntity.status(HttpStatus.OK).body(retMap);

    }

    // 첨부파일 다운로드
    @RequestMapping("/reservation/attachment.do")
    public void downloadAttachment(
            HttpServletRequest req,
            HttpServletResponse res,
            @RequestParam("file") String fileName
    ) throws Exception {

        // webapp 내부 실제 저장 폴더
        String path = req.getServletContext().getRealPath("/attachment/reservation");
        log.info("=== attachment real path => {}", path);

        File file = new File(path, fileName);

        if (!file.exists()) {
            String errorMsg = "첨부파일이 존재하지 않습니다.";

            req.getSession().setAttribute("errorMsg", errorMsg);

            String backUrl = req.getHeader("Referer");
            if (backUrl != null) {
                res.sendRedirect(backUrl);
            } else {
                res.sendRedirect(req.getContextPath() + "/");

            }
            res.sendRedirect(req.getContextPath() + "/");

            return;
        }

        // 다운로드 헤더 세팅
        String realFileName = fileName.split("_")[1];
        String encodedName = URLEncoder.encode(realFileName, "UTF-8").replace("+", "%20");
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
