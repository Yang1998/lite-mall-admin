package com.yyt.axios.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetailPO {
    private GoodsPO goods;
    private GoodsPicsPO goodsPics;
    private GoodsAttr goodsAttrs;
}
