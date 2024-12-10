package zerobase.projectreservation.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import zerobase.projectreservation.security.CustomAuthenticationEntryPoint;
import zerobase.projectreservation.security.JwtAuthenticationFilter;

import static zerobase.projectreservation.domain.type.Authority.ADMIN;

@Configuration
public class AdminSecurityConfig extends BaseSecurityConfig {

    private final UserDetailsService adminService;

    public AdminSecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            @Qualifier("adminService") UserDetailsService adminService) {

        super(jwtAuthenticationFilter, customAuthenticationEntryPoint);
        this.adminService = adminService;
    }

    /**
     * admin, user의 회원가입 경로는 접근 제한 없음
     * 사용자의 역할 기반[USER, ADMIN]으로 권한 관리
     */
    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "auth/member/signup", "auth/admin/signup",
                                "/h2-console/**", "/favicon.ico", "/error"
                        ).permitAll()
                        .requestMatchers("**/admin/**").hasRole(ADMIN.name())
                        .anyRequest().authenticated());

        configureCommonSecurity(http);
        return http.build();
    }
}
