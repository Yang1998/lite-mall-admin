package com.yyt.axios.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsAttrPO {
    private int id;
    private int goods_id;
    private int attr_id;
    private String attr_value;
    private BigDecimal add_price;
}
