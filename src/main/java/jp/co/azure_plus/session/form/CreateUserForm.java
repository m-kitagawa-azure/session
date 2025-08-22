package jp.co.azure_plus.session.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

// 入力フォーム
@Data
public class CreateUserForm {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    // "USER","ADMIN" などを想定（ROLE_ は自動付与）
    private List<String> roles = List.of("USER", "ADMIN");
}
