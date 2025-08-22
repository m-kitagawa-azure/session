package jp.co.azure_plus.session.entity;

import jakarta.persistence.*;
import lombok.Data;

// authorities テーブル
@Entity
@Table(
        name = "authorities",
        uniqueConstraints = @UniqueConstraint(name = "ix_auth_username_authority",
                columnNames = {"username","authority"})
)
@Data
public class AuthorityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "username", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 50)
    private String authority; // 例: ROLE_USER, ROLE_ADMIN
}
