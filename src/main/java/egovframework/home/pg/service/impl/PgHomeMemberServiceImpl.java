package egovframework.home.pg.service.impl;

import egovframework.home.pg.common.code.MemberStatus;
import egovframework.home.pg.common.utils.AES256Util;
import egovframework.home.pg.service.PgHomeMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PgHomeMemberServiceImpl extends EgovAbstractServiceImpl implements PgHomeMemberService {

    private final PgHomeMemberMapper pgHomeMemberMapper;
    private final PgHomeMemberRoleMapper pgHomeMemberRoleMapper;

    @Override
    public double getMemberTotalCnt(HashMap<String, Object> param) throws DataAccessException {
        return pgHomeMemberMapper.getMemberTotalCnt(param);
    }

    @Override
    public List<EgovMap> getMemberList(HashMap<String, Object> param) throws DataAccessException {

        List<EgovMap> list = pgHomeMemberMapper.getMemberList(param);

        // 전화번호/이메일 복호화
        for (EgovMap member : list) {
            try {
                if (member.get("phone") != null) {
                    member.put("phone", AES256Util.decrypt((String) member.get("phone")));
                }
                if (member.get("email") != null) {
                    member.put("email", AES256Util.decrypt((String) member.get("email")));
                }
            } catch (Exception e) {
                throw new RuntimeException("복호화 중 오류 발생", e);
            }
        }

        return list;
    }

    @Override
    public EgovMap getMemberById(Long memberId) throws Exception {

        EgovMap memberMap = pgHomeMemberMapper.getMemberById(memberId);

        // 복호화
        if (memberMap != null) {
            if (memberMap.get("phone") != null) {
                memberMap.put("phone", AES256Util.decrypt((String) memberMap.get("phone")));
            }

            if (memberMap.get("email") != null) {
                memberMap.put("email", AES256Util.decrypt((String) memberMap.get("email")));
            }
        }

        // 상태
        String statusKor = MemberStatus.toKor((String) memberMap.get("status"));
        memberMap.put("status", statusKor);


        return memberMap;
    }

    @Override
    public EgovMap getMemberByUsername(String username) throws DataAccessException {
        return pgHomeMemberMapper.getMemberByUsername(username);
    }

    /**
     * 회원가입
     */
    @Transactional
    @Override
    public boolean setMemberMerge(HashMap<String, Object> param) throws Exception {
        boolean result = false;

        // password 해시 암호화
        String hashedPassword = BCrypt.hashpw((String)param.get("password"), BCrypt.gensalt());
        param.put("password", hashedPassword);

        // phone, email AES256 암호화
        String encryptedPhone = AES256Util.encrypt((String)param.get("phone"));
        String encryptedEmail = AES256Util.encrypt((String)param.get("email"));

        param.put("phone", encryptedPhone);
        param.put("email", encryptedEmail);

        if (pgHomeMemberMapper.setMemberMerge(param) > 0) {
            String roleId = StringUtils.stripToEmpty((String)param.get("roleId"));

            List<Integer> roleList = new ArrayList<>();
            if (StringUtils.isEmpty(roleId)) {
                // 일반 회원 ROLE 하드 코딩
                roleList.add(2); // 1: ROLE_ADMIN, 2:ROLE_USER, 3: ROLE_OAUTH
                param.put("roleIds", roleList);
            } else {
                roleList.add(Integer.parseInt(roleId));
                param.put("roleIds", roleList);
            }

            if (pgHomeMemberRoleMapper.setMemberRoleInsert(param) > 0) {
                result = true;
            }
        }

        return result;

    }

    @Override
    public String getMemberStatusByUsername(String username) throws DataAccessException {
        return pgHomeMemberMapper.getMemberStatusByUsername(username);
    }

    @Transactional
    @Override
    public boolean setMemberStatus(HashMap<String, Object> param) throws DataAccessException {
        boolean result = false;

        if(pgHomeMemberMapper.setMemberStatus(param) > 0) {
            result = true;
        }

        return result;
    }

    @Override
    public boolean existsByUsername(String username) throws DataAccessException {
        return pgHomeMemberMapper.existsByUsername(username);
    }

    @Transactional
    @Override
    public void setUpdateLastLoginAtByUsername(String username) throws DataAccessException {
        pgHomeMemberMapper.setUpdateLastLoginAtByUsername(username);
    }


}
