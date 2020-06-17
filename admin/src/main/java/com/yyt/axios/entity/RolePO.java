package com.yyt.axios.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class RolePO {
    private int id;
    private String roleName;
    private List<Integer> ids;
    private String controller;
    private String desc;
}
