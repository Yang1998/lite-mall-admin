package com.yyt.axios.service;

import com.yyt.axios.entity.RolePO;
import com.yyt.axios.vo.RightVO;
import com.yyt.axios.vo.RoleVO;
import com.yyt.axios.vo.RolesVO;

import java.util.List;

public interface RoleService {
    List<RolesVO> doGetRoles();

    RolesVO doCreateRole(RolesVO rolesVO);

    RolesVO dogetRole(int rid);

    RolesVO doUpdateRole(int rid, RoleVO role);

    boolean deleteRole(int rid);

    List<RightVO> doDeleteRight(int roleId, int rightId);

    boolean doAllocateRights(int rid, String rids);

    RolePO dogetRolePO(int roleId);

}
