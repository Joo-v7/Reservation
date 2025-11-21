package egovframework.home.pg.common.security.user;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class CustomUserDetails implements UserDetails {

    private final EgovMap memberMap;

    public CustomUserDetails(EgovMap memberMap) {
        this.memberMap = memberMap;
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

    // MemberId(PK)
    public Long getId() {
        return (Long)memberMap.get("memberId");
    }

    @Override
    public String getUsername() {
        return (String) memberMap.get("username");
    }

    @Override
    public String getPassword() {
        return (String) memberMap.get("password");
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
