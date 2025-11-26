package egovframework.home.pg.common.security.service;

import egovframework.home.pg.common.security.user.CustomUserDetails;
import egovframework.home.pg.common.security.user.PrincipalDetails;
import egovframework.home.pg.service.impl.PgHomeMemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.hsqldb.lib.StringUtil;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final PgHomeMemberServiceImpl pgHomeMemberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // username 체크
        if (!pgHomeMemberService.existsByUsername(username)) {
            // 내부적으로 BadCredentialsException 마스킹 처리
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다: " + username);
        }

        // 회원 상태 조회
        String memberStatus = pgHomeMemberService.getMemberStatusByUsername(username);
        if (StringUtil.isEmpty(memberStatus)) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        if (memberStatus.equalsIgnoreCase("INACTIVE")) {
            throw new DisabledException("비활성화된 계정입니다.");
        }

        if (memberStatus.equalsIgnoreCase("DELETED")) {
            throw new DisabledException("삭제된 계정입니다.");
        }

        EgovMap memberMap = pgHomeMemberService.getMemberByUsername(username);

        return new PrincipalDetails(memberMap);
    }
}
