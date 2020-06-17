package com.yyt.axios.service;

import com.yyt.axios.vo.OrderPageVO;

public interface OrderService {
    OrderPageVO doGetAllOrders(String query, Integer pageNum, Integer pageSize);
}
