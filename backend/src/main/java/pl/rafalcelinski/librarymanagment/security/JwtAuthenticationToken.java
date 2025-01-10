package pl.rafalcelinski.librarymanagment.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class JwtAuthenticationToken implements Authentication {

    private final String token;
    private final Long userId;
    private final Collection<? extends GrantedAuthority> authorities;
    private boolean authenticated;

    public JwtAuthenticationToken(String token, Long userId, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.userId = userId;
        this.authorities = authorities;
        this.authenticated = false;
    }

    @Override
    public String getName() {
        return userId.toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }
}
