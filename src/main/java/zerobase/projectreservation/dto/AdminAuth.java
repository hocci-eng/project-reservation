package zerobase.projectreservation.dto;

import lombok.Data;
import zerobase.projectreservation.domain.Admin;
import zerobase.projectreservation.domain.type.Authority;
import zerobase.projectreservation.domain.type.Partnership;

public class AdminAuth {

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
        private Partnership partnership;

        public Admin toEntity() {
            return Admin.builder()
                    .loginId(loginId)
                    .password(password)
                    .username(username)
                    .phoneNumber(phoneNumber)
                    .authority(authority)
                    .partnership(partnership)
                    .build();
        }
    }
}
