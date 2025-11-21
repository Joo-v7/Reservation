package egovframework.home.pg.service.impl;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;

@Mapper
public interface PgHomeMemberRoleMapper {

    /**
     * MEMBER_ROLE insert
     */
    int setMemberRoleInsert(HashMap<String, Object> param) throws DataAccessException;

    /**
     * MEMBER_ROLE delete
     */
    int setMemberRoleAllDelete(HashMap<String, Object> param) throws DataAccessException;

}
