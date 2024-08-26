package antifraud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.stereotype.Component;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@EnableWebSecurity
@Component
public class SecurityConfig {

    public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(Customizer.withDefaults())
                .csrf(
                        csrf -> csrf
                                .ignoringRequestMatchers(toH2Console())
                                .disable()
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(restAuthenticationEntryPoint))

                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/antifraud/suspicious-ip","/api/antifraud/suspicious-ip/**").hasAuthority("SUPPORT")
                        .requestMatchers("/api/antifraud/stolencard","/api/antifraud/stolencard/**").hasAuthority("SUPPORT")
                        .requestMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/auth/user/**").hasAuthority("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/api/auth/list").hasAnyAuthority("ADMINISTRATOR", "SUPPORT")
//                        .requestMatchers(HttpMethod.POST, "/api/antifraud/transaction/**").hasAuthority("MERCHANT")
                        .requestMatchers(HttpMethod.POST, "/api/antifraud/transaction/**").hasAuthority("MERCHANT")
                        .requestMatchers(HttpMethod.PUT, "/api/auth/access", "/api/auth/access/").hasAuthority("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT,"/api/auth/role", "/api/auth/role/").hasAuthority("ADMINISTRATOR")
                        .requestMatchers(toH2Console()).permitAll()
                        .requestMatchers("/actuator/shutdown").permitAll()
                        .anyRequest().authenticated())
                .headers(headers -> headers
                        .frameOptions()
                        .sameOrigin()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
