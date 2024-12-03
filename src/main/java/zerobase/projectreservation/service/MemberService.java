package zerobase.projectreservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.type.Authority;
import zerobase.projectreservation.dto.MemberAuth;
import zerobase.projectreservation.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public Member register(MemberAuth.SignUp member) {
        boolean exists = memberRepository.existsByPhoneNumber(member.getPhoneNumber());
        if (exists) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        member.setAuthority(Authority.USER);
        return memberRepository.save(member.toEntity());
    }

}
