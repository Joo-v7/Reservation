package egovframework.home.pg.common.config;

import egovframework.home.pg.common.filter.AuthFilter;
import egovframework.home.pg.common.security.handler.*;
import egovframework.home.pg.common.security.service.CustomOAuthUserService;
import egovframework.home.pg.common.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = false)
@EnableMethodSecurity(prePostEnabled = true) // 메서드 레벨에서 보안(권한 체크)을 추가할 수 있게함
public class WebSecurityConfig {

    private final AuthFilter authFilter;

    private final CustomUserDetailsService userDetailsService;
    private final CustomOAuthUserService oauthUserService;

    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;

    private final CustomLogoutSuccessHandler logoutSuccessHandler;

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    private final CustomOAuthAuthenticationSuccessHandler customOAuthAuthenticationSuccessHandler;
    private final CustomOAuthAuthenticationFailureHandler customOAuthAuthenticationFailureHandler;

    // 정적 파일들 security 필터 패싱
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring()
//                .requestMatchers(
//                        "/css/**", "/js/**", "/fonts/**", "/styles/**"
//                );

        // SpringBoot와 달리, MVC가 아니라 servlet 앞단 전역 Ant 패턴을 알려주기 위해 아래 코드로 수정.
        return web -> web.ignoring().requestMatchers(
                new AntPathRequestMatcher("/css/**"),
                new AntPathRequestMatcher("/js/**"),
                new AntPathRequestMatcher("/fonts/**"),
                new AntPathRequestMatcher("/styles/**"),
                new AntPathRequestMatcher("/images/**")
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector handlerMappingIntrospector) throws Exception {

        // SpringBoot와 달리, DispatcherServlet의 패턴 (*.do)을 명시적으로 알려줌.
        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(handlerMappingIntrospector)
                .servletPath("*.do");

        // csrf disable
        http.csrf(AbstractHttpConfigurer::disable);

        // DaoAuthenticationProvider
        http.authenticationProvider(daoAuthenticationProvider());

        // 세션 필터 추가
        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        // 폼 로그인
        http.formLogin(formLogin -> formLogin
                .loginPage("/login.do")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/loginProcess.do")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
        );

        // OAuth 로그인
        // OAuth2LoginAuthenticationFilter
        http.oauth2Login(oauth -> oauth
                .loginPage("/login.do")
                .userInfoEndpoint(userInfo -> userInfo.userService(oauthUserService))
                .successHandler(customOAuthAuthenticationSuccessHandler)
                .failureHandler(customOAuthAuthenticationFailureHandler)
                .permitAll()
        );

        // Authorization
        http.authorizeHttpRequests(authRequest -> authRequest
                // OAuth2 관련 엔드포인트는 모두 허용
//                .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()

                .requestMatchers(
                        new AntPathRequestMatcher("/oauth2/**"),
                        new AntPathRequestMatcher("/login/oauth2/**")
                ).permitAll()

                .requestMatchers(mvc.pattern("/admin/**")).hasRole("ADMIN")
                .requestMatchers(mvc.pattern("/**")).permitAll()

        );

        // 로그아웃
        http.logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout.do", "GET"))
//                .logoutUrl("/logout.do") // POST
                .logoutSuccessHandler(logoutSuccessHandler)
        );

        // 401, 403
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint) // 401: 인증X
                .accessDeniedHandler(accessDeniedHandler) // 403: 인가X
        );

        return http.build();
    }

    // SpringBoot와 다르게 SpringFramework는 DaoAuthenticationProvider를 명시적으로 등록
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false); // 마스킹 해제
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }


}
