package zerobase.projectreservation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.projectreservation.domain.Admin;
import zerobase.projectreservation.dto.AdminAuth.SignUp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class AdminServiceTest {

    @Autowired
    AdminService adminService;

    @Test
    void 회원가입() {
        // given
        SignUp memberAuth = createAdmin(
                "user", "password", "jay",
                "01011111111"
        );

        // when
        Admin member = adminService.register(memberAuth);

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
        Admin member1 = adminService.register(
                createAdmin(
                        "user", "password", "jay",
                        "01011111111"
                ));

        // when & then
        assertThrows(IllegalStateException.class,
                () -> adminService.register(
                        createAdmin(
                                "user1", "password1", "jay2",
                                "01011111111"
                        ))
        );
    }

    private static SignUp createAdmin(String loginId, String password,
                                      String username, String phoneNumber) {
        SignUp adminAuth = new SignUp();
        adminAuth.setLoginId(loginId);
        adminAuth.setPassword(password);
        adminAuth.setUsername(username);
        adminAuth.setPhoneNumber(phoneNumber);
        return adminAuth;
    }
}
