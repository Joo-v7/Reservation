package egovframework.home.pg.common.security.handler;

import egovframework.home.pg.common.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthUtil authUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // redis 정리
        String username = authentication.getName(); // username
        authUtil.removeFromRedis(username);

        // 세션 & 쿠키 발급
        HttpSession session = request.getSession(true); // 없으면 새로 생성

        Cookie cookie = new Cookie("OnRoom", session.getId());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60*60); // 1시간
        cookie.setPath("/");
        response.addCookie(cookie);

        response.sendRedirect(request.getContextPath() + "/");
    }
}
