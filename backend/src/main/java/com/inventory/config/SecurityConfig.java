package com.inventory.config;
import com.inventory.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration @EnableWebSecurity
public class SecurityConfig {
    @Autowired private JwtAuthFilter jwtAuthFilter;
    @Autowired private UserDetailsService uds;

    @Bean public PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

    @Bean public DaoAuthenticationProvider authProvider(){
        var p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds); p.setPasswordEncoder(passwordEncoder()); return p;
    }

    @Bean public AuthenticationManager authManager(AuthenticationConfiguration cfg) throws Exception { return cfg.getAuthenticationManager(); }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(c->c.configurationSource(corsSource()))
            .csrf(c->c.disable())
            .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a->a
                .requestMatchers(new AntPathRequestMatcher("/api/auth/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/"),new AntPathRequestMatcher("/index.html"),
                    new AntPathRequestMatcher("/login.html"),new AntPathRequestMatcher("/register.html"),
                    new AntPathRequestMatcher("/forgot-password.html"),new AntPathRequestMatcher("/reset-password.html"),
                    new AntPathRequestMatcher("/css/**"),new AntPathRequestMatcher("/js/**"),
                    new AntPathRequestMatcher("/pages/**"),new AntPathRequestMatcher("/favicon.ico"),
                    new AntPathRequestMatcher("/error"),new AntPathRequestMatcher("/.well-known/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/**")).authenticated()
                .anyRequest().permitAll()
            )
            .authenticationProvider(authProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsSource(){
        var cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of("*"));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS","PATCH"));
        cfg.setAllowedHeaders(List.of("*")); cfg.setAllowCredentials(true);
        var src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**",cfg); return src;
    }
}
