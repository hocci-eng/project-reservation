package zerobase.projectreservation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.type.Authority;
import zerobase.projectreservation.dto.MemberAuth;
import zerobase.projectreservation.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Test
    void 회원가입() {
        // given
        MemberAuth.SignUp memberAuth = createMember("user", "password", "jay", "01011111111", Authority.USER);

        // when
        Member member = memberService.register(memberAuth);

        // then
        assertEquals(memberAuth.getLoginId(), member.getLoginId());
        assertEquals(memberAuth.getPassword(), member.getPassword());
        assertEquals(memberAuth.getUsername(), member.getUsername());
        assertEquals(memberAuth.getPhoneNumber(), member.getPhoneNumber());
        assertEquals(memberAuth.getAuthority(), member.getAuthority());
    }

    @Test()
    void 중복_회원_검증() {
        // given
        MemberAuth.SignUp member = createMember(
                "user", "password", "jay",
                "01011111111", Authority.USER
        );
        memberService.register(member);

        MemberAuth.SignUp member2 = createMember(
                "user1", "password1", "jay2",
                "01011111111", Authority.USER
        );

        // when & then
        assertThrows(IllegalStateException.class, () -> memberService.register(member2));
    }

    private static MemberAuth.SignUp createMember(String loginId, String password, String username, String phoneNumber, Authority authority) {
        MemberAuth.SignUp memberAuth = new MemberAuth.SignUp();
        memberAuth.setLoginId(loginId);
        memberAuth.setPassword(password);
        memberAuth.setUsername(username);
        memberAuth.setPhoneNumber(phoneNumber);
        memberAuth.setAuthority(authority);
        return memberAuth;
    }
}
