package zerobase.projectreservation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.projectreservation.domain.Admin;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.dto.AdminDto;
import zerobase.projectreservation.dto.MemberDto;
import zerobase.projectreservation.security.TokenProvider;
import zerobase.projectreservation.service.AdminService;
import zerobase.projectreservation.service.MemberService;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final AdminService adminService;
    private final TokenProvider tokenProvider;

    /**
     *
     */
    @PostMapping("/member/signup")
    public ResponseEntity<?> signup(@RequestBody MemberDto.SignUp request) {
        Member result = memberService.register(request);

        log.info("Signup result: {}", result);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/member/signin")
    public ResponseEntity<?> signIn(@RequestBody MemberDto.SignIn request) {
        Member member = memberService.authenticate(request);

        log.info("user login -> " + request.getLoginId());

        return ResponseEntity.ok(
                tokenProvider.generateToken(
                        member.getLoginId(), String.valueOf(member.getAuthority())
                )
        );
    }

    @PostMapping("/admin/signup")
    public ResponseEntity<?> signUp(@RequestBody AdminDto.SignUp request) {
        Admin admin = adminService.register(request);

        log.info("Signup result -> " + admin);

        return ResponseEntity.ok(admin);
    }

    @PostMapping("/admin/signin")
    public ResponseEntity<?> signIn(@RequestBody AdminDto.SignIn request) {
        Admin admin = adminService.authenticate(request);

        log.info("admin login -> " + request.getLoginId());

        return ResponseEntity.ok(
                tokenProvider.generateToken(
                        admin.getLoginId(), String.valueOf(admin.getAuthority())
                )
        );
    }
}
