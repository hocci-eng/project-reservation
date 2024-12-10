package zerobase.projectreservation.dto;

import lombok.Builder;
import lombok.Data;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.type.Authority;


public class MemberDto {

    @Data
    public static class SignIn {
        private String loginId;
        private String password;
    }

    @Data
    @Builder
    public static class SignUp {
        private String loginId;
        private String password;
        private String username;
        private String phoneNumber;
        private Authority authority;

        public Member toEntity() {
            return Member.builder()
                    .loginId(loginId)
                    .password(password)
                    .username(username)
                    .phoneNumber(phoneNumber)
                    .authority(Authority.USER)
                    .build();
        }
    }
}
