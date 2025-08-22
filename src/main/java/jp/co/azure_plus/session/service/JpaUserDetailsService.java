package jp.co.azure_plus.session.service;

import jp.co.azure_plus.session.entity.UserEntity;
import jp.co.azure_plus.session.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        List<SimpleGrantedAuthority> auths = userEntity.getAuthorities().stream()
                .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                .toList();

        return User.withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(auths)
                .disabled(!userEntity.isEnabled())
                .build();
    }
}

