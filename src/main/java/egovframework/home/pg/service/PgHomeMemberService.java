package egovframework.home.pg.service;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

public interface PgHomeMemberService {

    double getMemberTotalCnt(HashMap<String, Object> param) throws DataAccessException;

    List<EgovMap> getMemberList(HashMap<String, Object> param) throws DataAccessException;

    EgovMap getMemberById(Long memberId) throws Exception;

    EgovMap getMemberByUsername(String username) throws DataAccessException;

    boolean setMemberMerge(HashMap<String, Object> param) throws Exception;

    String getMemberStatusByUsername(String username) throws DataAccessException;

    boolean setMemberStatus(HashMap<String, Object> param) throws DataAccessException;

    boolean existsByUsername(String username) throws DataAccessException;

    boolean setUpdateLastLoginAtByUsername(String username) throws DataAccessException;
}

