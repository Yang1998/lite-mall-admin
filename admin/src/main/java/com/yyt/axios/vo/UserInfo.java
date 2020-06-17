package com.yyt.axios.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class UserInfo {
    @NotNull
    @Min(2)
    @Max(7)
    private String username;
    @NotNull
    @Min(6)
    @Max(15)
    private String password;
    @NotNull
    @Min(6)
    @Max(15)
    private String reconfirmPassword;
    @Email
    @NotNull
    private String email;
    @NotNull
    @Pattern(regexp = "^1[3456789]\\d{9}$")
    private String phone;
}
