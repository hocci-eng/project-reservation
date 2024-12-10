package zerobase.projectreservation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import zerobase.projectreservation.domain.type.Authority;
import zerobase.projectreservation.domain.type.Partnership;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Admin extends BaseEntity implements UserDetails {

    @Id @GeneratedValue
    @Column(name = "admin_id")
    private Long id;

    @Column(name = "business_no", unique = true, nullable = false)
    private String loginId;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String username;
    @Column(unique = true, nullable = false, length = 11)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Authority authority; // [USER, ADMIN]

    @Enumerated(EnumType.STRING)
    private Partnership partnership; // [PARTNER]

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Restaurant> restaurants = new ArrayList<>();

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + authority.name()));
    }
}
