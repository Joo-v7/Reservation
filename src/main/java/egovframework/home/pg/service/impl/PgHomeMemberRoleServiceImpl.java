package egovframework.home.pg.service.impl;

import egovframework.home.pg.service.PgHomeMemberRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PgHomeMemberRoleServiceImpl implements PgHomeMemberRoleService {

    private final PgHomeMemberRoleMapper pgHomeMemberRoleMapper;

    // 구현
    @Override
    public int setMemberRoleInsert(HashMap<String, Object> param) throws DataAccessException {
        return 0;
    }

    // 구현
    @Override
    public int setMemberRoleAllDelete(HashMap<String, Object> param) throws DataAccessException {
        return 0;
    }

    @Override
    public List<EgovMap> getRoleList() throws DataAccessException {
        return pgHomeMemberRoleMapper.getRoleList();
    }
}
