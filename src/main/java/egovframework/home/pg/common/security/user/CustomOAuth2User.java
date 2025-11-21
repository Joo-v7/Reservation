package egovframework.home.pg.common.security.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomOAuth2User implements OAuth2User {

    private final EgovMap memberMap;

    public CustomOAuth2User(EgovMap memberMap) {
        this.memberMap = memberMap;
    }

    // MemberId(PK)
    public Long getId() {
        return (Long) memberMap.get("memberId");
    }

    @Override
    public String getName() {
        return (String) memberMap.get("username");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return memberMap;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        String role = (String) memberMap.get("role");
        if (role == null || role.trim().isEmpty()) {
            return authorities;
        }

        String[] roleArr = role.split(",");

        for (String roleName : roleArr) {
            roleName = roleName.trim();
            if (!roleName.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority(roleName));
            }
        }

        return authorities;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

}
