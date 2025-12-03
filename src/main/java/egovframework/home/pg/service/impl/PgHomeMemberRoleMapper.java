package egovframework.home.pg.service.impl;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface PgHomeMemberRoleMapper {

    /**
     * ROLE 전체 조회
     */
    List<EgovMap> getRoleList() throws DataAccessException;

    /**
     * MEMBER_ROLE insert
     */
    int setMemberRoleInsert(HashMap<String, Object> param) throws DataAccessException;

    /**
     * MEMBER_ROLE delete
     */
    int setMemberRoleAllDeleteByMemberId(Long memberId) throws DataAccessException;

}
