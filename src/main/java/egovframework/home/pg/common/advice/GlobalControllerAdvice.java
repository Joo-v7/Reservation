package egovframework.home.pg.common.advice;

import egovframework.home.pg.common.security.user.PrincipalDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    // 로그인 유무: 헤더 값 분기 처리
    @ModelAttribute
    public void isLogin(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isLogin = authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);

        String name = "";

        if (isLogin) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            name = principalDetails.getRealName();
            model.addAttribute("name", name);
        }

        model.addAttribute("isLogin", isLogin);

    }
}
