package cz.cvut.fel.ear.meetingscheduler.security.model;

import cz.cvut.fel.ear.meetingscheduler.model.RegisteredUser;
import java.util.*;
import org.springframework.security.core.GrantedAuthority;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private RegisteredUser user;

    private final Set<GrantedAuthority> authorities;

    public UserDetails(RegisteredUser user) {
        Objects.requireNonNull(user);
        this.user = user;
        this.authorities = new HashSet<>();
    }

    public UserDetails(RegisteredUser user, Collection<GrantedAuthority> authorities) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(authorities);
        this.user = user;
        this.authorities = new HashSet<>();
        this.authorities.addAll(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(authorities);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
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

    public RegisteredUser getUser() {
        return user;
    }

    public void eraseCredentials() {
        user.erasePassword();
    }

    @Override
    public String toString() {
        return getUsername();
    }
}
