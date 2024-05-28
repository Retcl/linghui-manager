package ltd.jellyfish.modules.authentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import ltd.jellyfish.modules.authentication.manager.AuthenticationCustomManager;

@Configuration
public class AuthenticationConfig {

    @Autowired
    private AuthenticationCustomManager authenticationCustomManager;

    /**
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return authenticationCustomManager;
    }

}
