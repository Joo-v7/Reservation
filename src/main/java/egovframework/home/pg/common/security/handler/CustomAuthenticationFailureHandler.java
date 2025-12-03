package egovframework.home.pg.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import egovframework.home.pg.common.utils.RedisAuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final RedisAuthUtil redisAuthUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String username = request.getParameter("username");
        // 로그인 시도 횟수 + 1
        redisAuthUtil.increaseLoginAttemptCount(username);
        int count = redisAuthUtil.getLoginAttemptCount(username);

        String errorMsg = "로그인 실패 (" + count + "/5) \\n";

        if (exception instanceof UsernameNotFoundException) {
            errorMsg += exception.getMessage();
        } else if (exception instanceof DisabledException) {
            errorMsg += exception.getMessage();
        } else if (exception instanceof AuthenticationServiceException) {
            errorMsg += exception.getMessage();
        } else if (exception instanceof LockedException) {
            errorMsg = exception.getMessage();
        } else if (exception instanceof BadCredentialsException) {
            errorMsg += "비밀번호가 일치하지 않습니다.";
        }

        request.getSession().setAttribute("errorMsg", errorMsg);

        log.info("=== auth fail === request URI: {}", request.getRequestURI());
        log.info("=== auth fail === referer URI: {}", request.getHeader("referer"));

        String referer = request.getHeader("referer");

        if (referer != null && referer.contains("admin")) {
            response.sendRedirect(request.getContextPath() + "/admin/login.do");
        } else {
            response.sendRedirect(request.getContextPath() + "/login.do");
        }

    }
}
