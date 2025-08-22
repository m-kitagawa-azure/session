package jp.co.azure_plus.session.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/new").permitAll()
                        .requestMatchers("/hello").authenticated()
                        .anyRequest().permitAll()
                )
                // .formLogin(Customizer.withDefaults()) // デフォルトのログインを使う
                .formLogin(login -> login
                        .loginPage("/login")     // 自前のログインページを使う
                        .permitAll()
                )
                // .logout(Customizer.withDefaults()) // デフォルトのログアウトを使う
                .logout(logout -> logout
                        .logoutSuccessUrl("/")   // ログアウト後はトップに戻す
                        .permitAll()
                )
        ;
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
