package egovframework.home.pg.web;

import egovframework.home.pg.common.security.user.PrincipalDetails;
import egovframework.home.pg.service.PgHomeMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PgHomeMemberController {

    private final PgHomeMemberService pgHomeMemberService;

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

    // 마이페이지 - 계정 - 내 정보 페이지
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/myPage/myInfo.do")
    public String myInfo(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        return "home/pg/myInfo";
    }

    // 마이페이지 - 계정 - 내 정보 데이터
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/myPage/getMyInfo.do")
    public ResponseEntity<?> getMyInfo(
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


}
