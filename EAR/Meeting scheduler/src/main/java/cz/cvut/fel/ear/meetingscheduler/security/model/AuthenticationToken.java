package cz.cvut.fel.ear.meetingscheduler.security.model;

import java.security.Principal;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class AuthenticationToken extends AbstractAuthenticationToken implements Principal {

    private UserDetails userDetails;

    public AuthenticationToken(Collection<? extends GrantedAuthority> authorities, UserDetails userDetails) {
        super(authorities);
        this.userDetails = userDetails;
        super.setAuthenticated(true);
        super.setDetails(userDetails);
    }

    @Override
    public String getCredentials() {
        return userDetails.getPassword();
    }

    @Override
    public UserDetails getPrincipal() {
        return userDetails;
    }
}
