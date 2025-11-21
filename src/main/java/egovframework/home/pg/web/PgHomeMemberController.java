package egovframework.home.pg.web;

import egovframework.home.pg.service.PgHomeMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // TODO: 회원 전체 조회 ADMIN

    // TODO: 회원 단일 조회 USER

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


    // TODO: 회원 상태 변경(ACTIVE, INACTIVE, DELETED)
    // @PreAuthorize("hasRole('ADMIN')") 붙이기


}
