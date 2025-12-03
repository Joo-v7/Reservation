package egovframework.home.pg.service.impl;

import egovframework.home.pg.service.PgHomeMemberRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PgHomeMemberRoleServiceImpl implements PgHomeMemberRoleService {

    private final PgHomeMemberRoleMapper pgHomeMemberRoleMapper;

    /**
     * 회원의 권한 데이터 리스트
     * @return 권한 데이터 리스트
     * @throws DataAccessException
     */
    @Override
    public List<EgovMap> getRoleList() throws DataAccessException {
        return pgHomeMemberRoleMapper.getRoleList();
    }

    /**
     * 회원의 권한 저장
     * @param param
     * @return 회원 권한 저장 결과 여부
     * @throws DataAccessException
     */
    @Transactional
    @Override
    public boolean setMemberRoleInsert(HashMap<String, Object> param) throws DataAccessException {
        boolean result = false;

        if (pgHomeMemberRoleMapper.setMemberRoleInsert(param) > 0) {
            result = true;
        }

        return result;
    }

    /**
     * 회원의 권한 모두 삭제
     * @param memberId
     * @return 회원 권한 삭제 결과 여부
     * @throws DataAccessException
     */
    @Transactional
    @Override
    public boolean setMemberRoleAllDeleteByMemberId(Long memberId) throws DataAccessException {
        boolean result = false;

        if (pgHomeMemberRoleMapper.setMemberRoleAllDeleteByMemberId(memberId) > 0) {
            result = true;
        }

        return result;
    }
}
