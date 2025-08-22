package jp.co.azure_plus.session.repository;

import jp.co.azure_plus.session.entity.AuthorityEntity;
import jp.co.azure_plus.session.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    @EntityGraph(attributePaths = "authorities")   // 権限をまとめて取得（N+1防止）
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

}