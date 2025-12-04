package egovframework.home.pg.service.impl;

import egovframework.home.pg.common.code.MemberStatus;
import egovframework.home.pg.common.utils.AES256Util;
import egovframework.home.pg.exception.ArgumentNotValidException;
import egovframework.home.pg.service.PgHomeMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 - 총 개수
     * @param param
     * @return 회원의 총 개수
     * @throws DataAccessException
     */
    @Override
    public double getMemberTotalCnt(HashMap<String, Object> param) throws DataAccessException {
        return pgHomeMemberMapper.getMemberTotalCnt(param);
    }

    /**
     * 회원 - 회원 리스트
     * @param param
     * @return 회원 데이터 리스트
     * @throws DataAccessException
     */
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

    /**
     * 회원 - 단일 조회 by ID (PK)
     * @param memberId
     * @return 회원 단일 데이터
     * @throws Exception
     */
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

    /**
     * 회원 - 단일 조회 by username (login ID)
     * @param username
     * @return 회원 단일 데이터
     * @throws DataAccessException
     */
    @Override
    public EgovMap getMemberByUsername(String username) throws DataAccessException {
        return pgHomeMemberMapper.getMemberByUsername(username);
    }

    /**
     * 회원가입
     * @param param
     * @return 회원가입 결과 boolean
     * @throws Exception
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
                roleList.add(2); // 1: ROLE_ADMIN, 2:ROLE_USER
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

    /**
     * 회원 - 상태 조회 by username (login ID)
     * @param username
     * @return 회원 상태
     * @throws DataAccessException
     */
    @Override
    public String getMemberStatusByUsername(String username) throws DataAccessException {
        return pgHomeMemberMapper.getMemberStatusByUsername(username);
    }


    /**
     * 회원 - username(로그인 아이디) 중복 체크
     * @param username
     * @return 아이디 중복 결과
     * @throws DataAccessException
     */
    @Override
    public boolean existsByUsername(String username) throws DataAccessException {
        return pgHomeMemberMapper.existsByUsername(username);
    }

    /**
     * 회원 - 최근 로그인 일시 업데이트
     * @param username
     * @throws DataAccessException
     */
    @Transactional
    @Override
    public void setUpdateLastLoginAtByUsername(String username) throws DataAccessException {
        pgHomeMemberMapper.setUpdateLastLoginAtByUsername(username);
    }

    /**
     * 회원 - 회원 정보 업데이트
     * @param param
     * @return 회원 정보 업데이트 결과 boolean
     * @throws DataAccessException
     */
    @Transactional
    @Override
    public boolean setMemberUpdate(HashMap<String, Object> param) throws Exception {
        boolean result = false;

        String phone = StringUtils.stripToEmpty((String)param.get("phone"));
        String email = StringUtils.stripToEmpty((String)param.get("email"));

        // phone, email AES256 암호화
        if (!StringUtils.isEmpty(phone)) {
            String encryptedPhone = AES256Util.encrypt(phone);
            param.put("phone", encryptedPhone);
        }

        if (!StringUtils.isEmpty(email)) {
            String encryptedEmail = AES256Util.encrypt(email);
            param.put("email", encryptedEmail);
        }

        if (pgHomeMemberMapper.setMemberUpdate(param) > 0) {
            result = true;
        }

        return result;
    }

    /**
     * 회원 - 비밀번호 변경
     * @param param
     * @return 비밀번호 변경 결과 여부
     * @throws Exception
     */
    @Transactional
    @Override
    public boolean setPasswordUpdate(HashMap<String, Object> param) throws Exception {
        boolean result = false;

        Long memberId = (Long)param.get("memberId");
        String oldPassword = StringUtils.stripToEmpty((String)param.get("oldPassword"));
        String newPassword = StringUtils.stripToEmpty((String)param.get("newPassword"));

        // 회원 조회
        EgovMap memberMap = pgHomeMemberMapper.getMemberById(memberId);

        // 회원이 입력한 기존 비밀번호가 실제 비밀번호와 일치하는지 확인
        if (!passwordEncoder.matches(oldPassword, (String) memberMap.get("password"))) {
            throw new ArgumentNotValidException("기존 비밀번호가 올바르지 않습니다.");
        }

        // 비밀번호 암호화 후 저장
        String encodedPassword = passwordEncoder.encode(newPassword);
        param.put("password", encodedPassword);

        if (pgHomeMemberMapper.setMemberUpdate(param) > 0) {
            return true;
        }

        return result;

    }
}
