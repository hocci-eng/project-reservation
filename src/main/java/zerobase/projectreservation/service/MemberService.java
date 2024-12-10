package zerobase.projectreservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.type.Authority;
import zerobase.projectreservation.dto.MemberDto;
import zerobase.projectreservation.exception.impl.AlreadyExistException;
import zerobase.projectreservation.exception.impl.NotFoundLoginIdException;
import zerobase.projectreservation.exception.impl.NotMatchedPasswordException;
import zerobase.projectreservation.repository.MemberRepository;

@Service("memberService")
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * DB에서 사용자를 검색하고, 인증 정보를 Spring Security에 제공
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String loginId) {
        return memberRepository.findByLoginId(loginId).orElseThrow(
                        NotFoundLoginIdException::new);
    }

    /**
     * 사용자 회원 가입
     */
    public Member register(MemberDto.SignUp memberDto) {
        boolean exists = memberRepository.existsByPhoneNumber(memberDto.getPhoneNumber());
        if (exists) {
            throw new AlreadyExistException();
        }

        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        memberDto.setAuthority(Authority.USER);

        return memberRepository.save(memberDto.toEntity());
    }

    /**
     * 로그인 비밀번호 검증
     */
    @Transactional(readOnly = true)
    public Member authenticate(MemberDto.SignIn memberDto) {
        Member member = memberRepository.findByLoginId(memberDto.getLoginId())
                .orElseThrow(NotFoundLoginIdException::new);

        if (!passwordEncoder.matches(memberDto.getPassword(), member.getPassword())) {
            throw new NotMatchedPasswordException();
        }

        return member;
    }
}
