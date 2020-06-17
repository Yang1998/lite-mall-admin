package com.yyt.axios.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsPO {
    private int goods_id;
    private String goods_name;
    private BigDecimal goods_price;
    private Integer goods_number;
    private BigDecimal goods_weight;
    private int cat_id;
    private String goods_introduce;
    private String goods_big_logo;
    private String goods_small_logo;
    private String is_del;
    private Long add_time;
    private Long upd_time;
    private Long delete_time;
    private int cat_one_id;
    private int cat_two_id;
    private int cat_three_id;
    private int hot_number;
    private int is_promote;
    private int goods_state;
}
