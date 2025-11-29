package egovframework.home.pg.service;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

public interface PgHomeMemberRoleService {
    /**
     * MEMBER_ROLE insert
     */
    int setMemberRoleInsert(HashMap<String, Object> param) throws DataAccessException;

    /**
     * MEMBER_ROLE delete
     */
    int setMemberRoleAllDelete(HashMap<String, Object> param) throws DataAccessException;

    /**
     * ROLE 전체 조회
     */
    List<EgovMap> getRoleList() throws DataAccessException;
}
