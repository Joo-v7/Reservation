package egovframework.home.pg.web;

import egovframework.home.pg.common.code.ReservationStatus;
import egovframework.home.pg.common.security.user.PrincipalDetails;
import egovframework.home.pg.exception.AccessDeniedException;
import egovframework.home.pg.exception.ArgumentNotValidException;
import egovframework.home.pg.service.PgHomeMemberService;
import egovframework.home.pg.service.PgHomeReservationService;
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
import org.springframework.security.core.Authentication;
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
public class PgHomeMemberController {

    private final PgHomeMemberService pgHomeMemberService;
    private final PgHomeReservationService pgHomeReservationService;

    /**
     * 회원 - 회원가입 페이지
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 회원가입 페이지
     * @throws Exception
     */
    @RequestMapping("/join.do")
    public String joinForm(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        return "home/pg/join";
    }

    /**
     * 회원 - 회원가입
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 회원가입 결과
     * @throws Exception
     */
    @RequestMapping("/setMemberMerge.do")
    public ResponseEntity<?> setMemberMerge(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();

        if (pgHomeMemberService.setMemberMerge(param)) {
            retMap.put("error", "N");
            retMap.put("successTitle", "Success");
            retMap.put("successMsg", "회원가입을 환영합니다.");
        } else {
            retMap.put("error", "Y");
            retMap.put("errorTitle", "회원가입");
            retMap.put("errorMsg", "데이터 처리 중 오류가 발생했습니다.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(retMap);
    }

    /**
     * 회원가입 - 아이디 중복 체크
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 아이디 중복 여부
     * @throws Exception
     */
    @RequestMapping("/duplicateId.do")
    public ResponseEntity<?> duplicateId(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();

        String username = StringUtils.stripToEmpty(req.getParameter("username"));

        if (!StringUtils.isEmpty(username)) {

            if (pgHomeMemberService.existsByUsername(username)) {
                retMap.put("error", "Y");
                retMap.put("errorTitle", "회원가입");
                retMap.put("errorMsg", "이미 존재하는 아이디입니다.");
            } else {
                retMap.put("error", "N");
                retMap.put("successTitle", "Success");
                retMap.put("successMsg", "사용가능한 아이디입니다.");
            }

        }

        return ResponseEntity.status(HttpStatus.OK).body(retMap);
    }

    /**
     * 마이페이지 - 계정 - 내 정보 페이지
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 내 정보 페이지
     * @throws Exception
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/myPage/myInfoList.do")
    public String myInfoList(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        return "home/pg/myInfoList";
    }

    /**
     * 마이페이지 - 계정 - 내 정보 데이터
     * @param req
     * @param res
     * @param model
     * @param param
     * @param authentication
     * @return 내 정보 데이터
     * @throws Exception
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/myPage/getMyInfoList.do")
    public ResponseEntity<?> getMyInfoList(
            HttpServletRequest req,
            HttpServletResponse res,
            ModelMap model,
            @RequestParam HashMap<String, Object> param,
            Authentication authentication
    ) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long memberId = principalDetails.getId();

        EgovMap memberMap = pgHomeMemberService.getMemberById(memberId);

        retMap.put("error", "N");
        retMap.put("dataMap", memberMap);

        return ResponseEntity.status(HttpStatus.OK).body(retMap);
    }

    /**
     * 마이페이지 - 계정 - 내 정보 수정
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 내 정보 수정 결과
     * @throws Exception
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/myPage/setMyInfoUpdate.do")
    public ResponseEntity<?> setMyInfoUpdate(
            HttpServletRequest req,
            HttpServletResponse res,
            ModelMap model,
            @RequestParam HashMap<String, Object> param,
            Authentication authentication
    ) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long memberId = principalDetails.getId();

        param.put("memberId", memberId);

        if (pgHomeMemberService.setMemberUpdate(param)) {
            retMap.put("error", "N");
            retMap.put("successTitle", "Success");
            retMap.put("successMsg", "수정되었습니다.");
        } else {
            retMap.put("error", "Y");
            retMap.put("errorTitle", "회원 정보 수정");
            retMap.put("errorMsg", "데이터 처리 중 오류가 발생했습니다.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(retMap);
    }

    /**
     * 마이페이지 - 계정 - 비밀번호 변경 페이지
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 비밀번호 변경 페이지
     * @throws Exception
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/myPage/passwordUpdate.do")
    public String passwordUpdate(
            HttpServletRequest req,
            HttpServletResponse res,
            ModelMap model,
            @RequestParam HashMap<String, Object> param,
            Authentication authentication
    ) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long memberId = principalDetails.getId();
        param.put("memberId", memberId);

        EgovMap memberMap = pgHomeMemberService.getMemberById(memberId);

        String isOauth = StringUtils.stripToEmpty((String) memberMap.get("isOauth"));

        // oauth 사용자는 비밀번호 변경 페이지 접근 불가
        if (isOauth.equals("Y")) {
            String errorMsg = "접근 권한이 없습니다.";
            req.getSession().setAttribute("errorMsg", errorMsg);
            return "redirect:/";
        }

        return "home/pg/passwordUpdate";
    }

    /**
     * 마이페이지 - 계정 - 비밀번호 변경 요청
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 비밀번호 변경 결과 여부
     * @throws Exception
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/myPage/setPasswordUpdate.do")
    public ResponseEntity<?> setPasswordUpdate(
            HttpServletRequest req,
            HttpServletResponse res,
            ModelMap model,
            @RequestParam HashMap<String, Object> param,
            Authentication authentication
    ) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();

        try {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            Long memberId = principalDetails.getId();
            param.put("memberId", memberId);

            if (pgHomeMemberService.setPasswordUpdate(param)) {
                retMap.put("error", "N");
                retMap.put("successTitle", "Success");
                retMap.put("successMsg", "비밀번호가 변경되었습니다. 다시 로그인 하세요.");
            } else {
                retMap.put("error", "Y");
                retMap.put("errorTitle", "회원 비밀번호 변경");
                retMap.put("errorMsg", "데이터 처리 중 오류가 발생했습니다.");
            }

        } catch (DataAccessException | MultipartException | NullPointerException | IllegalArgumentException | ArgumentNotValidException e) {
            log.error("PgHomeMemberController:setPasswordUpdate.do error={}", e.getMessage());
            throw e;
        }

        return ResponseEntity.status(HttpStatus.OK).body(retMap);
    }

    /**
     * 마이페이지 - 계정 - 회원탈퇴 페이지
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 회원탈퇴 페이지
     * @throws Exception
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/myPage/leave.do")
    public String leave(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        // TODO 마이페이지 - 계정 - 회원탈퇴 페이지
        return "home/pg/leave";
    }

    /**
     * 마이페이지 - 계정 - 회원탈퇴 결과
     * @param req
     * @param res
     * @param model
     * @param param
     * @return
     * @throws Exception
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/myPage/setLeave.do")
    public ResponseEntity<?> setLeave(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();

        // TODO 마이페이지 - 계정 - 회원탈퇴 결과

        return ResponseEntity.status(HttpStatus.OK).body(retMap);
    }

    /**
     * 마이페이지 - 예약 - 내 예약 페이지
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 내 예약 데이터
     * @throws Exception
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/myPage/myReservationList.do")
    public String myReservationList(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        List<HashMap<String, String>> statusList = new ArrayList<>();

        // 예약 상태
        for (ReservationStatus status : ReservationStatus.values()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("code", status.name());       // 'PENDING',  'APPROVED', 'REJECTED', ‘CANCELLED'
            map.put("name", status.getKor());  // 승인대기, 승인완료, 반려, 취소
            statusList.add(map);
        }

        model.put("statusList", statusList);

        return "home/pg/myReservationList";
    }

    /**
     * 마이페이지 - 예약 - 내 예약 데이터
     * @param req
     * @param res
     * @param model
     * @param param
     * @param authentication
     * @return 내 예약 데이터
     * @throws Exception
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/myPage/getMyReservationList.do")
    public ResponseEntity<?> getMyReservationList(
            HttpServletRequest req,
            HttpServletResponse res,
            ModelMap model,
            @RequestParam HashMap<String, Object> param,
            Authentication authentication
            ) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();
        HashMap<String, Object> listMap = new HashMap<>();

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long memberId = principalDetails.getId();
        param.put("memberId", memberId);

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
            List<EgovMap> reservationList = pgHomeReservationService.getReservationListForMember(param);

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
     * 마이페이지 - 나의 게시글 - 내 게시글 페이지
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 내 게시글 페이지
     * @throws Exception
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/myPage/myBoardList.do")
    public String myBoardList(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        return "home/pg/myBoardList";
    }


}
