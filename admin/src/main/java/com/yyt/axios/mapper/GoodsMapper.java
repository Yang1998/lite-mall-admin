package com.yyt.axios.mapper;

import com.yyt.axios.entity.GoodsDetailPO;
import com.yyt.axios.entity.GoodsPO;
import com.yyt.axios.vo.GoodsAddVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {
    List<GoodsPO> getAllGoods(@Param("query") String query);

    int addGoods(@Param("goods") GoodsAddVO goodsAddVO, @Param("one_id") Integer one,
                 @Param("two_id")Integer two, @Param("three_id")Integer three);

    List<GoodsDetailPO> getGoodsDetail(@Param("gid") Integer id);

    int deleteGoodsById(@Param("id") Integer id);
}
