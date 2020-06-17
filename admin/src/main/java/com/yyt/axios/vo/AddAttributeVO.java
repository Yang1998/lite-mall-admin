package com.yyt.axios.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class AddAttributeVO {
    // 回填主键
    private int attr_id;

    @NotBlank(message = "属性名不可为空")
    private String attr_name;

    @NotNull(message = "属性类型不可为空")
    @Pattern(regexp = "^(only|many)$")
    private String attr_sel;

    private String attr_vals;
}
