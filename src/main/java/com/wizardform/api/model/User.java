package com.wizardform.api.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "Users")
public class User implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "roleId", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Request> requestList;

    // Overridden method of UserDetails class
    // Returns all the roles associated with a user. A single user can have multiple roles
    // Required to return role in format ROLE_{}
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authority = role.getRoleType().toUpperCase();
        return List.of(new SimpleGrantedAuthority(authority));
    }

    // Overridden method of UserDetails class
    // Should return a property which is uniquely defined for each user
    @Override
    public String getUsername() {
        return email;
    }

    // Not required to override this method
    // Provides a pre-defined method to use the isActive property
    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
