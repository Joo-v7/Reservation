package egovframework.home.pg.common.security.handler;

import egovframework.home.pg.common.code.MemberStatus;
import egovframework.home.pg.service.PgHomeMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final PgHomeMemberService pgHomeMemberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();

        // 회원 status 체크
        String status = pgHomeMemberService.getMemberStatusByUsername(username);

        if (!StringUtils.isEmpty(status)) {

            if (status.equals(MemberStatus.INACTIVE.name())) {
                throw new LockedException("비활성화된 계정입니다.");
            }

            if (status.equals(MemberStatus.DELETED.name())) {
                throw new LockedException("삭제된 계정입니다.");
            }

        }

        pgHomeMemberService.setUpdateLastLoginAtByUsername(username);

        // 세션 & 쿠키 발급
        HttpSession session = request.getSession(true); // 없으면 새로 생성

        Cookie cookie = new Cookie("OnRoom", session.getId());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60*60); // 1시간
        cookie.setPath("/");
        response.addCookie(cookie);

        // ROLE이 ADMIN이면 ADMIN 페이지로.
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(
                authority -> authority.getAuthority().equals("ROLE_ADMIN")
        );

        String redirectUrl = isAdmin ? (request.getContextPath() + "/admin/index.do") : (request.getContextPath() + "/");

        response.sendRedirect(redirectUrl);
    }
}
