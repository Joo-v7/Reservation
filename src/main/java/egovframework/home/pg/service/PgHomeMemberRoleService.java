package egovframework.home.pg.service;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

public interface PgHomeMemberRoleService {
    /**
     * ROLE 전체 조회
     */
    List<EgovMap> getRoleList() throws DataAccessException;

    /**
     * MEMBER_ROLE insert
     */
    boolean setMemberRoleInsert(HashMap<String, Object> param) throws DataAccessException;

    /**
     * MEMBER_ROLE delete
     */
    boolean setMemberRoleAllDeleteByMemberId(Long memberId) throws DataAccessException;

}
