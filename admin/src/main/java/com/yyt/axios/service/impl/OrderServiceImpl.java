package com.yyt.axios.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yyt.axios.entity.OrderPO;
import com.yyt.axios.mapper.OrderMapper;
import com.yyt.axios.service.OrderService;
import com.yyt.axios.vo.OrderPageVO;
import com.yyt.axios.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;

    @Override
    public OrderPageVO doGetAllOrders(String query, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<OrderPO> orders = orderMapper.selectAllOrders(query);
        PageInfo<OrderPO> pageInfo = new PageInfo<>(orders);
        return new OrderPageVO()
                .setPageNum(pageInfo.getPageNum())
                .setTotal(pageInfo.getTotal())
                .setOrders(transformOrderPOs2OrderVOs(orders));

    }

    private OrderVO transformOrderPO2OrderVO(OrderPO orderPO) {
        return new OrderVO()
                .setOrder_id(orderPO.getOrderId())
                .setUser_id(orderPO.getUserId())
                .setOrder_number(orderPO.getOrderNumber())
                .setOrder_price(orderPO.getOrderPrice())
                .setOrder_pay(orderPO.getOrderPay())
                .setIs_send(orderPO.getIsSend())
                .setTrade_no(orderPO.getTradeNo())
                .setOrder_fapiao_title(orderPO.getOrderFapiaoTitle())
                .setOrder_fapiao_company(orderPO.getOrderFapiaoCompany())
                .setOrder_fapiao_content(orderPO.getOrderFapiaoContent())
                .setConsignee_addr(orderPO.getConsigneeAddr())
                .setPay_status(orderPO.getPayStatus())
                .setCreate_time((long) orderPO.getCreateTime())
                .setUpdate_time((long) orderPO.getUpdateTime());
    }

    private List<OrderVO> transformOrderPOs2OrderVOs(List<OrderPO> orders) {
        List<OrderVO> res = new ArrayList<>();
        if(orders == null) {
            return res;
        }
        orders.forEach(order -> res.add(transformOrderPO2OrderVO(order)));
        return res;
    }
}
