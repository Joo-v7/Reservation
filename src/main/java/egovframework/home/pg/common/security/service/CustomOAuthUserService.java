package egovframework.home.pg.common.security.service;

import egovframework.home.pg.common.security.user.CustomOAuth2User;
import egovframework.home.pg.common.security.user.PrincipalDetails;
import egovframework.home.pg.service.PgHomeMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuthUserService extends DefaultOAuth2UserService {

    private final PgHomeMemberService pgHomeMemberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        /**
         * OAuth2: NAVER
         */
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("username", response.get("id")); // OAuth에서 주는 ID는 해당 회원의 고유한 값임.

        // username(OAuth2 id)를 조회해서 없으면 회원가입 처리
        if (!pgHomeMemberService.existsByUsername(param)) {
            // Naver OAuth2의 응답값 -> Member 값
            HashMap<String, Object> memberMap = new HashMap<String, Object>();
            String username = StringUtils.stripToEmpty((String)response.get("id"));
            String password = "naver";
            String name = StringUtils.stripToEmpty((String)response.get("name"));
            String phone = StringUtils.stripToEmpty((String)response.get("mobile"));
            String email = StringUtils.stripToEmpty((String)response.get("email"));
            String birthyear = StringUtils.stripToEmpty((String)response.get("birthyear"));
            String birthday = StringUtils.stripToEmpty((String)response.get("birthday"));
            String birthdate = StringUtils.stripToEmpty(birthyear + "-" + birthday);

            memberMap.put("username", username);
            memberMap.put("password", password); // 임의값: OAuth2로 로그인한 사용자는 비밀번호 사용 X
            memberMap.put("name", name);
            memberMap.put("phone", phone);
            memberMap.put("email", email);
            memberMap.put("birthdate", birthdate);

            try {
                pgHomeMemberService.setMemberMerge(memberMap);
            } catch (Exception e) {
                log.error("Naver OAuth 회원가입 처리 중 오류 발생");
                throw new OAuth2AuthenticationException("Failed to create OAuth2 member");
            }
        }

        EgovMap member = pgHomeMemberService.getMemberByUsername(param);

        return new PrincipalDetails(member);
    }
}


