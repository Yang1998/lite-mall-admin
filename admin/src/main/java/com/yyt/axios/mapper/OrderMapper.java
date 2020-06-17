package com.yyt.axios.mapper;

import com.yyt.axios.entity.OrderPO;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer orderId);

    int insert(OrderPO record);

    int insertSelective(OrderPO record);

    OrderPO selectByPrimaryKey(Integer orderId);

    int updateByPrimaryKeySelective(OrderPO record);

    int updateByPrimaryKeyWithBLOBs(OrderPO record);

    int updateByPrimaryKey(OrderPO record);

    List<OrderPO> selectAllOrders(String query);

}