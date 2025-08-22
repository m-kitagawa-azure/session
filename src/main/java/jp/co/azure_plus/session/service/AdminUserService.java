package jp.co.azure_plus.session.service;

import jp.co.azure_plus.session.entity.AuthorityEntity;
import jp.co.azure_plus.session.entity.UserEntity;
import jp.co.azure_plus.session.repository.AuthorityRepository;
import jp.co.azure_plus.session.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    /** ユーザー一覧の取得（username/有効フラグ） */
    @Transactional(readOnly = true)
    public List<UserRow> listUsers() {
        return userRepository.findAll(Sort.by("username"))
                .stream()
                .map(userEntity -> new UserRow(userEntity.getUsername(), userEntity.isEnabled()))
                .toList();
    }

    /** 特定ユーザーの権限一覧 */
    @Transactional(readOnly = true)
    public List<String> listAuthorities(String username) {
        return authorityRepository.findByUser_Username(username)
                .stream()
                .map(AuthorityEntity::getAuthority)
                .sorted()
                .toList();
    }

    /** ユーザー追加（存在チェック、BCrypt、ロール付与） */
    public void createUser(String username, String rawPassword, List<String> roles) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("既に存在します: " + username);
        }
        var user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEnabled(true);

        Set<AuthorityEntity> auths = roles.stream()
                .map(r -> "ROLE_" + r.toUpperCase())           // "USER"→"ROLE_USER"
                .map(role -> {
                    var authorityEntity = new AuthorityEntity();
                    authorityEntity.setUser(user);
                    authorityEntity.setAuthority(role);
                    return authorityEntity;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));

        user.setAuthorities(auths);
        userRepository.save(user);
    }

    /** 権限を置き換え（既存を全消去→新規付与） */
    public void replaceRoles(String username, List<String> roles) {
        UserEntity user = userRepository.findById(username)
                .orElseThrow(() -> new NoSuchElementException("ユーザーが見つかりません: " + username));

        // 既存権限のクリア
        user.getAuthorities().clear();

        roles.stream()
                .map(r -> "ROLE_" + r.toUpperCase())
                .forEach(role -> {
                    var authorityEntity = new AuthorityEntity();
                    authorityEntity.setUser(user);
                    authorityEntity.setAuthority(role);
                    user.getAuthorities().add(authorityEntity);
                });
        // 追跡中エンティティなので save 不要
    }

    /** 有効/無効の切替 */
    public void setEnabled(String username, boolean enabled) {
        UserEntity user = userRepository.findById(username)
                .orElseThrow(() -> new NoSuchElementException("ユーザーが見つかりません: " + username));
        user.setEnabled(enabled);
    }

    /** 削除（外部キー CASCADE or orphanRemoval で権限も消える） */
    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    // 一覧用 DTO
    public record UserRow(String username, boolean enabled) {}
}

