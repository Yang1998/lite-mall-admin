package com.yyt.axios.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CateInfoVO {
    // 回填的主键
    private int id;
    @NotNull(message = "商品分类父id不能为空")
    @Min(value = 0, message = "商品分类父id必须非负")
    private Integer cat_pid;
    @NotNull(message = "商品分类名不能为空")
    private String cat_name;
    @NotNull(message = "商品分类层级不能为空")
    @Range(min = 0, max = 2, message = "商品分类层级必须为0-2")
    private Integer cat_level;
}
