package com.yyt.axios.controller.permission;

import com.yyt.axios.enums.CodeEnum;
import com.yyt.axios.service.RoleService;
import com.yyt.axios.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Slf4j
@Validated
public class RoleController {
    @Autowired
    RoleService roleService;

    @GetMapping("/roles")
    @RequiresPermissions({"roles:index"})
    public BaseVO<List<RolesVO>> getRoles() {
        try {
            return new BaseVO<List<RolesVO>>()
                    .setState(CodeEnum.GET_ROLESLIST_SUCCESS)
                    .setData(roleService.doGetRoles());
        } catch (Exception e) {
            log.error("获取权限列表失败");
            return new BaseVO<List<RolesVO>>().setState(CodeEnum.GET_ROLESLIST_ERROR);
        }
    }

    @PostMapping("/roles")
    @RequiresPermissions({"roles:add"})
    public BaseVO<RolesVO> createRole(@RequestBody RolesVO rolesVO) {
        try {
            return new BaseVO<RolesVO>()
                    .setData(roleService.doCreateRole(rolesVO))
                    .setState(CodeEnum.INSERT_ROLE_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new BaseVO<RolesVO>().setState(CodeEnum.INSERT_ROLE_ERROR);
        }
    }

    @GetMapping("/roles/{id}")
    @RequiresPermissions({"roles:index"})
    public BaseVO<RolesVO> getRoleInfo(@PathVariable("id") Integer id) {
        try {
            return new BaseVO<RolesVO>()
                    .setState(CodeEnum.GET_SUCCESS)
                    .setData(roleService.dogetRole(id));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new BaseVO<RolesVO>().setState(CodeEnum.GET_ERROR);
        }
    }

    @PutMapping("/roles/{id}")
    @RequiresPermissions({"roles:update"})
    public BaseVO<RolesVO> updateRole(@PathVariable("id") Integer id,
                                            @RequestBody RoleVO role) {
        try {
            return new BaseVO<RolesVO>()
                    .setState(CodeEnum.UPDATE_SUCCESS)
                    .setData(roleService.doUpdateRole(id, role));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new BaseVO<RolesVO>().setState(CodeEnum.UPDATE_ERROR);
        }
    }

    @DeleteMapping("/roles/{id}")
    @RequiresPermissions({"roles:delete"})
    public BaseVO deleteRole(@PathVariable("id") Integer rid) {
        try {
            boolean res = roleService.deleteRole(rid);
            if(res) {
                return new BaseVO()
                        .setState(CodeEnum.DELETE_SUCCESS);
            } else {
                throw new RuntimeException("用户不存在 id = " + rid);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new BaseVO().setState(CodeEnum.DELETE_ROLE_ERROR);
        }
    }

    @DeleteMapping("roles/{roleId}/rights/{rightId}")
    @RequiresPermissions({"roles:delete"})
    public BaseVO<List<RightVO>> deleteRight(@PathVariable("roleId") Integer roleId,
                                    @PathVariable("rightId") Integer rightId) {
        try {
            return new BaseVO<List<RightVO>>()
                    .setData(roleService.doDeleteRight(roleId, rightId))
                    .setState(CodeEnum.DELETE_RIGHT_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new BaseVO<List<RightVO>>().setState(CodeEnum.DELETE_RIGHT_ERROR);
        }
    }

    @PostMapping("/roles/{id}/rights")
    @RequiresPermissions({"roles:update"})
    public BaseVO allocateRights(@PathVariable("id") Integer rid, @RequestBody RidsVO rids) {
        try {
            boolean res = roleService.doAllocateRights(rid, rids.getRids());
            if(res) {
                return new BaseVO().setState(CodeEnum.UPDATE_SUCCESS);
            } else {
                throw new RuntimeException("更新失败 因为角色ID = " + rid + " 不存在");
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            return new BaseVO().setState(CodeEnum.UPDATE_ERROR);
        }
    }
}
