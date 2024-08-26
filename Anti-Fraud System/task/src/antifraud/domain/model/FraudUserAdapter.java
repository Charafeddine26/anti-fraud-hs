package antifraud.domain.model;

import antifraud.domain.enums.UserRoles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class FraudUserAdapter implements UserDetails {

    private final FraudUser user;

    public FraudUserAdapter(FraudUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // If user.getAuthority() returns null or empty string, provide a default authority
        UserRoles authority = (user.getRole() != null && !user.getRole().isEmpty())
                ? user.getRole()
                : UserRoles.MERCHANT;
        return Collections.singletonList(new SimpleGrantedAuthority(authority.toString()));
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isLocked();
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
