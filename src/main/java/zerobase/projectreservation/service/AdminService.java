package zerobase.projectreservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Admin;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.Restaurant;
import zerobase.projectreservation.domain.type.Authority;
import zerobase.projectreservation.dto.AdminAuth;
import zerobase.projectreservation.dto.MemberAuth;
import zerobase.projectreservation.dto.RestaurantDto;
import zerobase.projectreservation.repository.AdminRepository;
import zerobase.projectreservation.repository.MemberRepository;
import zerobase.projectreservation.repository.RestaurantRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final AdminRepository adminRepository;
    private final RestaurantRepository restaurantRepository;

    public Admin register(AdminAuth.SignUp admin) {
        boolean exists = adminRepository.existsByPhoneNumber(admin.getPhoneNumber());
        if (exists) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        admin.setAuthority(Authority.ADMIN);

        return adminRepository.save(admin.toEntity());
    }


    public String saveRestaurantInfo(RestaurantDto restaurantDto, String loginId) {
        Admin admin = adminRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalStateException("없는 회원입니다."));

        Restaurant savedRestaurant = restaurantRepository.save(
                Restaurant.builder()
                        .name(restaurantDto.getName())
                        .description(restaurantDto.getDescription())
                        .build()
        );

        savedRestaurant.addRestaurant(admin);
        return savedRestaurant.getName();
    }
}
