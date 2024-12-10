package zerobase.projectreservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Admin;
import zerobase.projectreservation.dto.AdminDto;
import zerobase.projectreservation.exception.impl.AlreadyExistException;
import zerobase.projectreservation.exception.impl.NotFoundLoginIdException;
import zerobase.projectreservation.exception.impl.NotMatchedPasswordException;
import zerobase.projectreservation.repository.AdminRepository;

@Service("adminService")
@RequiredArgsConstructor
@Transactional
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * DB에서 사용자를 검색하고, 인증 정보를 Spring Security에 제공
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String loginId) {
        return adminRepository.findByLoginId(loginId).orElseThrow(
                NotFoundLoginIdException::new
        );
    }

    /**
     * 매장 점주 회원가입
     * 가입한 점주 엔티티 반환
     */
    @PreAuthorize("hasRole('ADMIN')")
    public Admin register(AdminDto.SignUp adminDto) {
        boolean exists = adminRepository.existsByPhoneNumber(adminDto.getPhoneNumber());
        if (exists) {
            throw new AlreadyExistException();
        }

        adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));

        return adminRepository.save(adminDto.toEntity());
    }

    /**
     * 로그인 비밀번호 검증
     */
    @Transactional(readOnly = true)
    public Admin authenticate(AdminDto.SignIn adminDto) {
        Admin admin = adminRepository.findByLoginId(adminDto.getLoginId())
                .orElseThrow(NotFoundLoginIdException::new);

        if (!passwordEncoder.matches(adminDto.getPassword(), admin.getPassword())) {
            throw new NotMatchedPasswordException();
        }

        return admin;
    }
}
