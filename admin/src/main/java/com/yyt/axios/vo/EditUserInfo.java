package com.yyt.axios.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class EditUserInfo {
    @Email
    @NotNull
    private String email;
    @NotNull
    @Pattern(regexp = "^1[3456789]\\d{9}$")
    private String mobile;
}
