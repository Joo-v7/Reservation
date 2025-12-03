package egovframework.home.pg.web.admin;

import egovframework.home.pg.common.code.MemberStatus;
import egovframework.home.pg.common.code.ReservationStatus;
import egovframework.home.pg.service.PgHomeMemberRoleService;
import egovframework.home.pg.service.PgHomeMemberService;
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
@RequestMapping("/admin")
public class PgHomeMemberAdminController {

    private final PgHomeMemberService pgHomeMemberService;
    private final PgHomeMemberRoleService pgHomeMemberRoleService;

    /**
     * 회원 관리 - 회원 목록 페이지
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 회원 목록 페이지
     * @throws Exception
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/memberList.do")
    public String memberList(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
        List<HashMap<String, String>> statusList = new ArrayList<>();

        // 회원 상태
        for (MemberStatus status : MemberStatus.values()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("code", status.name()); // ACTIVE, INACTIVE, DELETED
            map.put("name", status.getKor()); // 활성화, 비활성화, 삭제
            statusList.add(map);
        }

        model.put("statusList", statusList);

        return "home/pg/admin/memberList";
    }

    /**
     * 회원 관리 - 회원 목록 데이터
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 회원 데이터 리스트
     * @throws Exception
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/getMemberList.do")
    public ResponseEntity<?> getMemberList(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam HashMap<String, Object> param) throws Exception {
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
            double totalCnt = pgHomeMemberService.getMemberTotalCnt(param);

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

            // 회원 상태
            for (MemberStatus status : MemberStatus.values()) {
                HashMap<String, String> map = new HashMap<>();
                map.put("code", status.name());       // 'PENDING',  'APPROVED', 'REJECTED', ‘CANCELLED'
                map.put("name", status.getKor());  // 승인대기, 승인완료, 반려, 취소
                statusList.add(map);
            }

            // 권한 리스트
            List<EgovMap> roleList = pgHomeMemberRoleService.getRoleList();

            // 회원 리스트
            List<EgovMap> memberList = pgHomeMemberService.getMemberList(param);

            listMap.put("list", memberList);
            listMap.put("statusList", statusList);
            listMap.put("roleList", roleList);
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
     * 회원 관리 - 회원 단일 데이터
     * @param req
     * @param res
     * @param model
     * @param memberId
     * @return 회원 단일 데이터
     * @throws Exception
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/getMember.do")
    public ResponseEntity<?> getMember(HttpServletRequest req, HttpServletResponse res, ModelMap model, @RequestParam Long memberId) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();

        try {
            List<HashMap<String, String>> statusList = new ArrayList<>();

            // 회원 상태
            for (MemberStatus status : MemberStatus.values()) {
                HashMap<String, String> map = new HashMap<>();
                map.put("code", status.name());       // 'PENDING',  'APPROVED', 'REJECTED', ‘CANCELLED'
                map.put("name", status.getKor());  // 승인대기, 승인완료, 반려, 취소
                statusList.add(map);
            }

            // 권한 리스트
            List<EgovMap> roleList = pgHomeMemberRoleService.getRoleList();

            // 회원 데이터
            EgovMap memberMap = pgHomeMemberService.getMemberById(memberId);

            retMap.put("error", "N");
            retMap.put("statusList", statusList);
            retMap.put("roleList", roleList);
            retMap.put("dataMap", memberMap);

        } catch (DataAccessException | MultipartException | NullPointerException | IllegalArgumentException e) {
            log.error("== ADMIN == PgHomeMemberAdminController:getMember.do error={}", e.getMessage());
            throw e;
        }

        return ResponseEntity.status(HttpStatus.OK).body(retMap);
    }

    /**
     * 회원 관리 - 회원 정보 수정 (상태/권한 수정 포함)
     * @param req
     * @param res
     * @param model
     * @param param
     * @return 회원 정보 수정 결과
     * @throws Exception
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/setMemberUpdate.do")
    public ResponseEntity<?> setMemberUpdate(
            HttpServletRequest req,
            HttpServletResponse res,
            ModelMap model,
            @RequestParam HashMap<String, Object> param,
            @RequestParam(value = "roleIds", required = false) Long[] roleIds
    ) throws Exception {
        HashMap<String, Object> retMap = new HashMap<>();
        boolean result = true;

        try {
            // 회원 정보 업데이트
            if (!pgHomeMemberService.setMemberUpdate(param)) {
                result = false;
            }

            // 회원 기존 권한 삭제
            String memberId = StringUtils.stripToEmpty((String) param.get("memberId"));
            if (!pgHomeMemberRoleService.setMemberRoleAllDeleteByMemberId(Long.parseLong(memberId))) {
                result = false;
            }

            // 회원 권한 등록
            param.put("roleIds", roleIds);
            if (!pgHomeMemberRoleService.setMemberRoleInsert(param)){
                result = false;
            }

            if (result) {
                retMap.put("error", "N");
                retMap.put("successTitle", "Success");
                retMap.put("successMsg", "성공적으로 저장되었습니다.");
            } else {
                retMap.put("error", "Y");
                retMap.put("errorTitle", "회원 정보 업데이트");
                retMap.put("errorMsg", "데이터 처리 중 오류가 발생했습니다.");
            }


        } catch (DataAccessException | MultipartException | NullPointerException | IllegalArgumentException e) {
            log.error("== ADMIN == PgHomeMemberAdminController:setMemberUpdate.do error={}", e.getMessage());
            throw e;
        }





        // TODO MEMBER ROLE 업데이트



        return ResponseEntity.status(HttpStatus.OK).body(retMap);
    }
}
