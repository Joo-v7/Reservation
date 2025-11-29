package egovframework.home.pg.common.security.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String errorMsg = "로그인이 필요한 서비스입니다.";

        request.getSession().setAttribute("errorMsg", errorMsg);

        if(request.getRequestURI().contains("admin")) {
            response.sendRedirect(request.getContextPath() + "/admin/login.do");
        } else {
            response.sendRedirect(request.getContextPath() + "/login.do");
        }

    }
}
