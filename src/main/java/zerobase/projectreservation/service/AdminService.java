package zerobase.projectreservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Admin;
import zerobase.projectreservation.domain.type.Authority;
import zerobase.projectreservation.domain.type.Partnership;
import zerobase.projectreservation.dto.AdminAuth;
import zerobase.projectreservation.repository.AdminRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final AdminRepository adminRepository;

    /**
     * 매장 점주 회원가입
     * 가입한 점주 엔티티 반환
     */
    public Admin register(AdminAuth.SignUp admin) {
        boolean exists = adminRepository.existsByPhoneNumber(admin.getPhoneNumber());
        if (exists) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        admin.setAuthority(Authority.ADMIN);
        admin.setPartnership(Partnership.PARTNER);

        return adminRepository.save(admin.toEntity());
    }
}
