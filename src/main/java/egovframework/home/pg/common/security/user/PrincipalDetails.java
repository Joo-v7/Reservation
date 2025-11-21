package egovframework.home.pg.common.security.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrincipalDetails implements UserDetails, OAuth2User {

    private final EgovMap memberMap;

    public PrincipalDetails(EgovMap memberMap) {
        this.memberMap = memberMap;
    }

    // 공통: memberId(PK)
    public Long getId() {
        return (Long) memberMap.get("memberId");
    }

    // UserDetails

    @Override
    public String getUsername() {
        return (String) memberMap.get("username");
    }

    @Override
    public String getPassword() {
        return (String) memberMap.get("password");
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
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    // OAuth2
    @Override
    public Map<String, Object> getAttributes() {
        return memberMap;
    }

    @Override
    public String getName() {
        return (String) memberMap.get("username");
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }
}
