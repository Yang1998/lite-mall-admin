package com.yyt.axios.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class GoodsPicsPO {
    /** 图片id **/
    private int pics_id;
    /** 商品id **/
    private int goods_id;
    /** 大图 **/
    private String pics_big;
    /** 中图 **/
    private String pics_mid;
    /** 小图 **/
    private String pics_sma;
}
