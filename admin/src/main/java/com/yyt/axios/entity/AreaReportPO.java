package com.yyt.axios.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaReportPO {
    private int id;
    private BigDecimal count;
    private String area;
    private Date date;
}
