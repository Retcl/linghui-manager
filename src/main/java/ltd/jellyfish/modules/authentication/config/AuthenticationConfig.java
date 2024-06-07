package ltd.jellyfish.modules.authentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ltd.jellyfish.modules.authentication.filter.TokenFilter;
import ltd.jellyfish.modules.authentication.handler.FailHandler;
import ltd.jellyfish.modules.authentication.handler.SuccessHandler;
import ltd.jellyfish.modules.authentication.manager.AuthenticationCustomManager;

@Configuration
public class AuthenticationConfig {

    @Autowired
    private AuthenticationCustomManager authenticationCustomManager;

    @Autowired
    private TokenFilter tokenFilter;

    /**
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((request) -> {
            request.anyRequest().permitAll();
        });
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return authenticationCustomManager;
    }

    @Bean
    public SuccessHandler successHandler(){
        return new SuccessHandler();
    }

    @Bean
    public FailHandler failHandler(){
        return new FailHandler();
    }

}
