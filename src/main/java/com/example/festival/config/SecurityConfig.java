package com.example.festival.config;

import com.example.festival.service.UsuarioDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioDetailService usuarioDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/login/**", "/registro", "/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/heroes/**").permitAll()

//                        .requestMatchers("/heroes/**").hasRole("USUARIO")
                        .requestMatchers("/intenciones/**").hasRole("USUARIO")
                        .requestMatchers("/canciones/**").hasRole("USUARIO")
                        .requestMatchers("/halloween/**").hasRole("USUARIO")
                        .requestMatchers("/reservas/**").hasRole("USUARIO")

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
//                        .successHandler(customAuthenticationSuccessHandler())
                        .defaultSuccessUrl("/heroes", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
//                )
//                .csrf(csrf -> csrf
//                        .ignoringRequestMatchers("")
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(usuarioDetailService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
        ;
        return builder.build();
    }

//    @Bean
//    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
//        return new SimpleUrlAuthenticationSuccessHandler() {
//            @Override
//            protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//                for (GrantedAuthority authority : authorities) {
//                    switch (authority.getAuthority()) {
//                        case "ROLE_ADMIN":
//                            return "/heroes";
//                        case "ROLE_USUARIO":
//                            return "/heroes";
//                        case "ROLE_VISITANTE":
//                            return "/heroes";
//                        default:
//                            System.out.println("autoridad o rol: "+authority.getAuthority());
//                    }
//                }
//                return "/login";
//            }
//        };
//    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
