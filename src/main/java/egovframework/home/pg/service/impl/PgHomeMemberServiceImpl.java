package egovframework.home.pg.service.impl;

import egovframework.home.pg.common.utils.AES256Util;
import egovframework.home.pg.service.PgHomeMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        return pgHomeMemberMapper.getMemberList(param);

//        List<EgovMap> list = pgHomeMemberMapper.getMemberList(param);
//
//        for (EgovMap member : list) {
//            try {
//                if (member.get("phone") != null) {
//                    member.put("phone", AES256Util.decrypt((String) member.get("phone")));
//                }
//                if (member.get("email") != null) {
//                    member.put("email", AES256Util.decrypt((String) member.get("email")));
//                }
//            } catch (Exception e) {
//                throw new RuntimeException("복호화 중 오류 발생", e);
//            }
//        }
//
//        return list;
    }

    @Override
    public EgovMap getMember(HashMap<String, Object> param) throws Exception {

        EgovMap memberMap = pgHomeMemberMapper.getMember(param);

        // 복호화
        if (memberMap != null) {
            if (memberMap.get("phone") != null) {
                memberMap.put("phone", AES256Util.decrypt((String) memberMap.get("phone")));
            }

            if (memberMap.get("email") != null) {
                memberMap.put("email", AES256Util.decrypt((String) memberMap.get("email")));
            }
        }

        return memberMap;
    }

    @Override
    public EgovMap getMemberByUsername(HashMap<String, Object> param) throws DataAccessException {
        return pgHomeMemberMapper.getMemberByUsername(param);
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
            // ROLE 하드코딩
            List<Integer> roleIds = new ArrayList<>();
            roleIds.add(1); // 1: ROLE_USER, 2: ROLE_ADMIN

            param.put("roleIds", roleIds);

            if (pgHomeMemberRoleMapper.setMemberRoleInsert(param) > 0) {
                result = true;
            }
        }

        return result;

    }

    @Override
    public String getMemberStatus(HashMap<String, Object> param) throws DataAccessException {
        return pgHomeMemberMapper.getMemberStatus(param);
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
    public boolean existsByUsername(HashMap<String, Object> param) throws DataAccessException {
        return pgHomeMemberMapper.existsByUsername(param);
    }

}
