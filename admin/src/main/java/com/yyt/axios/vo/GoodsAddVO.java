package com.yyt.axios.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class GoodsAddVO {
        // 回填主键
        private int id;
        /** 商品名称 **/
        @NotNull(message = "商品名称不能为空")
        private String goods_name;
        /** 以','分割的分类列表 **/
        @NotNull(message = "分类列表不能为空")
        private String goods_cat;
        /** 商品价格 **/
        @NotNull(message = "商品价格不能为空")
        @DecimalMin(value = "0.0", message = "商品价格非负")
        private BigDecimal goods_price;
        /** 商品重量 **/
        @NotNull(message = "商品重量不能为空")
        @DecimalMin(value = "0.0", message = "商品重量非负")
        private BigDecimal goods_weight;
        /** 商品数量 **/
        @NotNull(message = "商品数量不能为空")
        @Min(value = 0, message = "商品数量非负")
        private Integer goods_number;
        /** 商品介绍 **/
        private String goods_introduce;
        /** pic对象 **/
        private List<PicUrlVO> pics;
        /** goods_attr对象数组 **/
        private List<AddGoodsAttrVO> attrs;
}
