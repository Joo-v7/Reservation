package egovframework.home.pg.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import egovframework.home.pg.common.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
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
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final AuthUtil authUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String username = request.getParameter("username");
        // 로그인 시도 횟수 + 1
        authUtil.increaseLoginAttemptCount(username);
        int count = authUtil.getLoginAttemptCount(username);

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
        response.sendRedirect(request.getContextPath() + "/login.do");
    }
}
