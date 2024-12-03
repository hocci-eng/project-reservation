package zerobase.projectreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.projectreservation.domain.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByLoginId(String loginId);

    boolean existsByPhoneNumber(String phoneNumber);

}
