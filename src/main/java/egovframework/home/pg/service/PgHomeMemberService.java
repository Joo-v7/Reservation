package egovframework.home.pg.service;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

public interface PgHomeMemberService {
    /**
     * 회원 - 전체 회원수
     */
    double getMemberTotalCnt(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 회원 - 리스트
     */
    List<EgovMap> getMemberList(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 회원 - 단일 조회 by ID
     */
    EgovMap getMemberById(Long memberId) throws Exception;

    /**
     * 회원 - 단일 조회 by Username
     */
    EgovMap getMemberByUsername(String username) throws DataAccessException;

    /**
     * 회원 - Merge
     */
    boolean setMemberMerge(HashMap<String, Object> param) throws Exception;

    /**
     * 회원 - 회원 상태 조회
     */
    String getMemberStatusByUsername(String username) throws DataAccessException;

    /**
     * 회원 - username(로그인 ID) 중복 확인
     */
    boolean existsByUsername(String username) throws DataAccessException;

    /**
     * 회원 - 최근 로그인 여부 업데이트
     */
    void setUpdateLastLoginAtByUsername(String username) throws DataAccessException;
}

