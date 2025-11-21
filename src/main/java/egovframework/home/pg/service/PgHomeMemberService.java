package egovframework.home.pg.service;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

public interface PgHomeMemberService {

    double getMemberTotalCnt(HashMap<String, Object> param) throws DataAccessException;

    List<EgovMap> getMemberList(HashMap<String, Object> param) throws DataAccessException;

    EgovMap getMember(HashMap<String, Object> param) throws Exception;

    EgovMap getMemberByUsername(HashMap<String, Object> param) throws DataAccessException;

    boolean setMemberMerge(HashMap<String, Object> param) throws Exception;

    String getMemberStatus(HashMap<String, Object> param) throws DataAccessException;

    boolean setMemberStatus(HashMap<String, Object> param) throws DataAccessException;

    boolean existsByUsername(HashMap<String, Object> param) throws DataAccessException;
}

