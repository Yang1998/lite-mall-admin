package com.yyt.axios.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class OrderVO {
    private int order_id;
    private int user_id;
    private String order_number;
    private BigDecimal order_price;
    private String order_pay;
    private String is_send;
    private String trade_no;
    private String order_fapiao_title;
    private String order_fapiao_company;
    private String order_fapiao_content;
    private String consignee_addr;
    private String pay_status;
    private Long create_time;
    private Long update_time;
}
