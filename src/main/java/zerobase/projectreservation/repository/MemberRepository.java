package zerobase.projectreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zerobase.projectreservation.domain.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String loginId);

    boolean existsByPhoneNumber(String username);

    @Query("select m from Member m join fetch m.reservation where m.phoneNumber = :phoneNumber")
    Optional<Member> findMemberByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
