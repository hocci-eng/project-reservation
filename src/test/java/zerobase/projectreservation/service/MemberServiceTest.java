package zerobase.projectreservation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.type.Authority;
import zerobase.projectreservation.dto.MemberDto;
import zerobase.projectreservation.exception.impl.AlreadyExistException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void 회원가입() {
        // given
        MemberDto.SignUp memberAuth = createMember("user", "password", "jay", "01011111111", Authority.USER);

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
        MemberDto.SignUp member = createMember(
                "user", "password", "jay",
                "01011111111", Authority.USER
        );
        memberService.register(member);

        MemberDto.SignUp member2 = createMember(
                "user1", "password1", "jay2",
                "01011111111", Authority.USER
        );

        // when & then
        assertThrows(AlreadyExistException.class, () -> memberService.register(member2));
    }

    private static MemberDto.SignUp createMember(String loginId, String password, String username, String phoneNumber, Authority authority) {
        MemberDto.SignUp memberAuth = new MemberDto.SignUp();
        memberAuth.setLoginId(loginId);
        memberAuth.setPassword(password);
        memberAuth.setUsername(username);
        memberAuth.setPhoneNumber(phoneNumber);
        memberAuth.setAuthority(authority);
        return memberAuth;
    }
}
