package egovframework.home.pg.common.security.handler;

import egovframework.home.pg.common.utils.RedisAuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
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
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final RedisAuthUtil redisAuthUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = request.getParameter("username");
        log.info("로그아웃: " + username);

        if (redisAuthUtil.existsRedis(username)) {
            redisAuthUtil.removeFromRedis(username);
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        // default: 사용자 페이지
        String redirectUrl = request.getContextPath() + "/";

        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("admin")) {
            // 관리자 쪽에서 로그아웃 누른 경우
            redirectUrl = request.getContextPath() + "/admin/login.do";
        }

        response.sendRedirect(redirectUrl);

    }
}
