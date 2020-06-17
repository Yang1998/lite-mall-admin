package com.yyt.axios.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class GoodsAttr {
    private int goods_id;
    private int attr_id;
    private String attr_value;
    private BigDecimal add_price;
    private String attr_name;
    private String attr_sel;
    private String attr_write;
    private String attr_vals;
}
