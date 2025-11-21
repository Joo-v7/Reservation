package egovframework.home.pg.service.impl;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface PgHomeMemberMapper {
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
    EgovMap getMember(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 회원 - 단일 조회 by Username
     */
    EgovMap getMemberByUsername(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 회원 - Merge
     */
    int setMemberMerge(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 회원 - 회원 상태 조회
     */
    String getMemberStatus(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 회원 - 상태 변경 (ACTIVE/INACTIVE/DELETED)
     */
    int setMemberStatus(HashMap<String, Object> param) throws DataAccessException;

    /**
     * 회원 - 로그인 ID 존재여부 확인
     */
    boolean existsByUsername(HashMap<String, Object> param) throws DataAccessException;


}
