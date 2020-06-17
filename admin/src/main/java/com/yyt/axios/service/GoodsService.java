package com.yyt.axios.service;

import com.yyt.axios.vo.GoodsAddVO;
import com.yyt.axios.vo.GoodsDetailVO;
import com.yyt.axios.vo.GoodsPageVO;

public interface GoodsService {

    GoodsPageVO doGetGoodsList(String query, Integer pageNum, Integer pageSize);

    boolean doAddGoods(GoodsAddVO goodsAddVO);

    GoodsDetailVO doGetGoodsById(Integer id);

    boolean doDeleteGoods(Integer id);
}
