package com.yyt.axios.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
/**
 *  state -> 1: 启用 0 禁用
 *  time -> 注册时间, 类似System.currentTimeMillis()格式
 */
public class ManagerPO {
    private int id;
    private String name;
    private String pwd;
    private String salt;
    private long time;
    private int roleId;
    private String mobile;
    private String email;
    private int state;
}
