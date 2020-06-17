package com.yyt.axios.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionPO {
    private int id;
    private String name;
    private int pid;
    private String controller;
    private String method;
    // 数据库这里，level是枚举值
    private String level;
}
