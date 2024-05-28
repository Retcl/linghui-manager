package ltd.jellyfish.modules.authentication.manager;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public record AuthenticationCustomManager(
    UserDetailsService userDetailsService,
    PasswordEncoder passwordEncoder
) implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String encoderPassword = userDetails.getPassword();
        if (!passwordEncoder.matches(encoderPassword, password)) {
            throw new BadCredentialsException("encoderPassword");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, encoderPassword);
    }

}
