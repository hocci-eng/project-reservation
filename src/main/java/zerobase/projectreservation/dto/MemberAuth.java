package zerobase.projectreservation.dto;

import lombok.Data;
import zerobase.projectreservation.domain.Member;
import zerobase.projectreservation.domain.type.Authority;

public class MemberAuth {

    @Data
    public static class SignIn {
        private String loginId;
        private String password;
    }

    @Data
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
                    .authority(authority)
                    .build();
        }
    }
}
