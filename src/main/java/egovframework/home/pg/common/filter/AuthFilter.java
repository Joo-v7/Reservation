package egovframework.home.pg.common.filter;

import egovframework.home.pg.common.code.MemberStatus;
import egovframework.home.pg.common.security.handler.CustomAuthenticationFailureHandler;
import egovframework.home.pg.common.utils.AuthUtil;
import egovframework.home.pg.service.impl.PgHomeMemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final PgHomeMemberServiceImpl pgHomeMemberService;
    private final AuthUtil authUtil;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    private final AntPathRequestMatcher loginMatcher = new AntPathRequestMatcher("/loginProcess.do", "POST");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 로그인 요청일 때만 필터링
        if (!loginMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // param 체크
            HashMap<String, Object> param = new HashMap<>();
            String username = request.getParameter("username");
            param.put("username", username);

            if (username == null || username.isEmpty()) {
                throw new AuthenticationServiceException("요청에서 아이디를 찾을 수 없습니다.");
            }

            // 아이디 체크
            if (!pgHomeMemberService.existsByUsername(username)) {
                throw new UsernameNotFoundException("아이디가 존재하지 않습니다: " + username);
            }

            // 회원 상태 확인 (ACTIVE, INACTIVE, DELETED)
            String status = pgHomeMemberService.getMemberStatusByUsername(username);
            if (!StringUtils.isEmpty(status)) {

                if (status.equals(MemberStatus.INACTIVE.name())) {
                    throw new LockedException("비활성화된 계정입니다.");
                }

                if (status.equals(MemberStatus.DELETED.name())) {
                    throw new LockedException("삭제된 계정입니다.");
                }

            }

            // 블랙리스트 확인
            if (authUtil.isBlacklist(username)) {
                long remainingTime = authUtil.getBlacklistRemainSeconds(username);
                throw new LockedException((int)Math.ceil(remainingTime) + "분 후 다시 로그인을 시도하세요");
            }

            // Redis 확인
            if (authUtil.existsRedis(username)) {
                // 현재 로그인 시도 횟수
                int count = authUtil.getLoginAttemptCount(username);
                // Redis에서 username의 로그인 시도 횟수 조회 실패
                if (count < 0) {
                    throw new AuthenticationServiceException(username + " 로그인한 횟수를 찾을 수 없습니다.");
                }

                // 로그인 시도 횟수 5회 초과
                if (count + 1 > 5) {
                    // 블랙리스트 등록 (15분)
                    authUtil.addBlacklist(username);
                    // 해당 아이디의 로그인 시도 횟수 시간 리셋 설정 (15분)
                    authUtil.resetLoginAttemptTTL(username);
                    throw new LockedException("로그인 시도 횟수 5회 초과, 15분 후 다시 시도하세요.");
                }

            } else {
                authUtil.registerInRedis(username);
            }

            filterChain.doFilter(request, response);

        } catch (AuthenticationException e) {
            customAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
        }

    }

}
