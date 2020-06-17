package com.yyt.axios.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CateUpdateVO {
    @NotBlank(message = "商品名不能为空")
    private String cat_name;
}
