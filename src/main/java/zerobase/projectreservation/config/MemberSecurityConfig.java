package zerobase.projectreservation.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import zerobase.projectreservation.security.CustomAuthenticationEntryPoint;
import zerobase.projectreservation.security.JwtAuthenticationFilter;

import static zerobase.projectreservation.domain.type.Authority.USER;

@Configuration
public class MemberSecurityConfig extends BaseSecurityConfig {

    private final UserDetailsService memberService;

    public MemberSecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            @Qualifier("memberService") UserDetailsService memberService) {

        super(jwtAuthenticationFilter, customAuthenticationEntryPoint);
        this.memberService = memberService;
    }

    /**
     * admin, user의 회원가입 경로는 접근 제한 없음
     * 사용자의 역할 기반[USER, ADMIN]으로 권한 관리
     */
    @Bean
    public SecurityFilterChain memberSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/member/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "auth/member/signup", "auth/admin/signup",
                                "/member/**", "/arrival",
                                "/h2-console/**", "/favicon.ico", "/error"
                        ).permitAll()
                        .requestMatchers(
                                "/auth/member/signin").hasRole(USER.name())
                        .requestMatchers("**/member/**").hasRole(USER.name())
                        .anyRequest().authenticated()
                );
        configureCommonSecurity(http);
        return http.build();
    }
}
