package com.yyt.axios.vo;

import com.yyt.axios.entity.GoodsAttr;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class GoodsDetailVO {
        /** 商品id **/
        private int goods_id;
        /** 商品名称 **/
        private String goods_name;
        /** 商品价格 **/
        private BigDecimal goods_price;
        /** 商品数量 **/
        private Integer goods_number;
        /** 商品重量 **/
        private BigDecimal goods_weight;
        /** 商品状态 商品状态 0: 未通过 1: 审核中 2: 已审核**/
        private Integer goods_state;
        /** 商品添加时间  以秒来返回**/
        private Long add_time;
        /** 商品更新时间 以秒来返回**/
        private Long upd_time;
        /** 热销品数量 **/
        private Integer hot_number;
        /** 是否是热销品 **/
        private Boolean is_promote;
        /** pic数组 **/
        private List<GoodsPicsVO> pics;
        /** goods_attr对象数组 **/
        private List<GoodsAttr> attrs;
}
