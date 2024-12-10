package zerobase.projectreservation.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import zerobase.projectreservation.security.CustomAuthenticationEntryPoint;
import zerobase.projectreservation.security.JwtAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class BaseSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public void configureCommonSecurity(HttpSecurity http)
            throws Exception {

        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(STATELESS)
                )
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(
                                customAuthenticationEntryPoint)
                )
                .formLogin(withDefaults())
                .headers(headers -> headers.defaultsDisabled()
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // H2 Console iframe 사용을 위해 frameOptions 비활성화
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);
    }
}
