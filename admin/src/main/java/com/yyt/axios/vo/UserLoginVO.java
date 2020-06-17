package com.yyt.axios.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO {
    @NotBlank(message = "用户名不能为空")
    @Length(min = 2, max = 7, message = "用户名不合法")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 15, message = "密码不合法")
    private String password;
}
