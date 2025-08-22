package jp.co.azure_plus.session.repository;

import jp.co.azure_plus.session.entity.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
    List<AuthorityEntity> findByUser_Username(String username);
}

