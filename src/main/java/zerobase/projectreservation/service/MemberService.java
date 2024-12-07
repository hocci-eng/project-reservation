package zerobase.projectreservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.type.Authority;
import zerobase.projectreservation.dto.MemberAuth;
import zerobase.projectreservation.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 사용자 회원 가입
     */
    public Member register(MemberAuth.SignUp member) {
        boolean exists = memberRepository.existsByPhoneNumber(member.getPhoneNumber());
        if (exists) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        member.setAuthority(Authority.USER);

        return memberRepository.save(member.toEntity());
    }
}
