package com.yyt.axios.vo;

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
public class RolesVO {
    private int id;
    private String roleName;
    private String roleDesc;
    private List<RightVO> children;
}
