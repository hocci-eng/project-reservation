package zerobase.projectreservation.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zerobase.projectreservation.service.AdminService;
import zerobase.projectreservation.service.MemberService;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static io.jsonwebtoken.Jwts.SIG.HS512;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration.time}")
    private long expirationTime;

    private final MemberService memberService;

    private final AdminService adminService;

    /**
     * JWT 서명에 사용할 비밀키 생성
     */
    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }

    /**
     * JWT 생성
     */
    public String generateToken(String loginId, String role) {
        log.info("토큰 발급 유저 아이디: {}", loginId);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(loginId)
                .claim("role", role) // 토큰에 담을 클레임 정보
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getKey(), HS512)
                .compact();
    }

    private String getRoleFromToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token);

        return claimsJws.getPayload().get("role").toString();
    }

    public Authentication getAuthentication(String token) {
        // 역할별 UserDetailsService 매핑
        Map<String, UserDetailsService> roleToServiceMap = Map.of(
                "ADMIN", adminService,
                "USER", memberService
        );

        String role = getRoleFromToken(token);

        UserDetailsService userDetailsService = roleToServiceMap.get(role.toUpperCase());

        UserDetails userDetails = userDetailsService.loadUserByUsername(getLoginId(token));
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities()
        );
    }

    public String getLoginId(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) return false;

        Claims claims = parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        log.info("Claims : {}", claims);
        return claims;
    }
}
