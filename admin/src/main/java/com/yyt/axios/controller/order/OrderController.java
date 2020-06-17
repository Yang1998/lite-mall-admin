package com.yyt.axios.controller.order;

import com.yyt.axios.enums.CodeEnum;
import com.yyt.axios.service.OrderService;
import com.yyt.axios.vo.BaseVO;
import com.yyt.axios.vo.OrderPageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@Slf4j
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/orders")
    @RequiresPermissions({"orders:index"})
    public BaseVO<OrderPageVO> getAllOrders(String query, @NotNull(message = "页大小不可为空") Integer pageSize, @NotNull(message = "页码不可为空")Integer pageNum) {
        try {
            return new BaseVO<OrderPageVO>()
                    .setData(orderService.doGetAllOrders(query, pageNum, pageSize))
                    .setState(CodeEnum.GET_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseVO<OrderPageVO>().setState(CodeEnum.GET_ERROR);
        }
    }
}
