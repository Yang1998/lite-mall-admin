package com.yyt.axios.mapper;

import com.yyt.axios.entity.RolePO;
import com.yyt.axios.vo.RoleVO;
import com.yyt.axios.vo.RolesVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    String getRoleNameByRoleId(@Param("rid") int rid);

    List<RolePO> getAllRoles();

    int insertRole(@Param("role") RolesVO rolesVO);

    RolePO getRoleById(@Param("rid") int rid);

    int updateRole(@Param("rid") int rid,@Param("role") RoleVO role);

    int deleteRole(@Param("rid") int rid);

    int updateRoleIds(@Param("rid") int roleId, @Param("ids") List<Integer> newIds);

    int allocateRoleIds(@Param("rid") int rid, @Param("rids") String rids);
}
