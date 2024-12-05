package zerobase.projectreservation.domain;

import jakarta.persistence.*;
import lombok.*;
import zerobase.projectreservation.domain.type.Authority;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String loginId;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Authority authority; // [USER, ADMIN]

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Reservation reservation;
}
