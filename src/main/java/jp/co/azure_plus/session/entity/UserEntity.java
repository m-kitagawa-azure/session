package jp.co.azure_plus.session.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<AuthorityEntity> authorities = new LinkedHashSet<>();

}