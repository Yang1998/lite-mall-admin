package com.yyt.axios.service.impl;

import com.yyt.axios.entity.RightsPO;
import com.yyt.axios.entity.RolePO;
import com.yyt.axios.enums.RightsEnum;
import com.yyt.axios.mapper.PermissionMapper;
import com.yyt.axios.mapper.RoleMapper;
import com.yyt.axios.service.RoleService;
import com.yyt.axios.vo.RightVO;
import com.yyt.axios.vo.RoleVO;
import com.yyt.axios.vo.RolesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<RolesVO> doGetRoles() {
        List<RolePO> allRoles = roleMapper.getAllRoles();
        List<RolesVO> res = new LinkedList<>();
        allRoles.forEach(role -> {
            RolesVO rolesVO = getRoleInfo(role);
            res.add(rolesVO);
        });
        return res;
    }

    @Transactional
    @Override
    public RolesVO doCreateRole(RolesVO rolesVO) {
        int cnt = roleMapper.insertRole(rolesVO);
        if(cnt != 1) {
            throw new RuntimeException("插入角色失败 " + rolesVO.toString());
        }
        return rolesVO;
    }

    @Override
    public RolesVO dogetRole(int rid) {
        RolePO roleFromDb = roleMapper.getRoleById(rid);
        return new RolesVO()
                .setId(roleFromDb.getId())
                .setRoleName(roleFromDb.getRoleName())
                .setRoleDesc(roleFromDb.getDesc());
    }

    @Transactional
    @Override
    public RolesVO doUpdateRole(int rid, RoleVO role) {
        int cnt = roleMapper.updateRole(rid, role);
        if(cnt != 1) {
            throw new RuntimeException("用户不存在 rid = " + rid);
        }
        RolePO roleById = roleMapper.getRoleById(rid);
        return new RolesVO()
                .setId(roleById.getId())
                .setRoleDesc(roleById.getDesc())
                .setRoleName(roleById.getRoleName());
    }

    @Override
    public boolean deleteRole(int rid) {
        return roleMapper.deleteRole(rid) == 1;
    }

    @Transactional
    @Override
    public List<RightVO> doDeleteRight(int roleId, int rightId) {
        RolePO role = roleMapper.getRoleById(roleId);
        RolesVO roleInfo = getRoleInfo(role);
        deleteRoleRecursive(roleInfo.getChildren(), rightId);
        List<Integer> newIds = new ArrayList<>();
        getRoleIdsRecursive(newIds, roleInfo.getChildren());
        int res = roleMapper.updateRoleIds(roleId, newIds);
        if(res != 1) {
            throw new RuntimeException("更新失败 + roleId" + roleId);
        }
        return getRoleInfo(roleMapper.getRoleById(roleId)).getChildren();
    }

    @Transactional
    @Override
    public boolean doAllocateRights(int rid, String rids) {
        int cnt = roleMapper.allocateRoleIds(rid, rids);
        return cnt == 1;
    }

    @Override
    public RolePO dogetRolePO(int roleId) {
        return roleMapper.getRoleById(roleId);
    }

    /**
     * 注意children属性没赋值
     * @param pid
     * @param childLevel
     * @param rights
     * @return
     */
    List<RightVO> getChildRights(int pid, String childLevel, List<RightsPO> rights) {
        LinkedList<RightVO> res = new LinkedList<>();
        rights.stream()
                .filter(right -> childLevel.equals(right.getLevel()) && pid == right.getPid())
                .forEach(right -> {
                    RightVO rightVO = new RightVO();
                    rightVO.setId(right.getId())
                            .setAuthName(right.getAuthName())
                            .setPath(right.getPath());
                    res.add(rightVO);
                });
        return res;
    }

    /**
     * 将RightPO 转换为 RightVO
     */
    private RightVO transformRightPO2RightVO(RightsPO rightsPO, List<RightVO> child) {
        return new RightVO()
                .setId(rightsPO.getId())
                .setPath(rightsPO.getPath())
                .setAuthName(rightsPO.getAuthName())
                .setChildren(child);
    }

    /**
     * 将RolePO 转换为 RoleVO
     */
    private RolesVO transformRolesPO2RoleVO(RolePO rolePO, List<RightVO> child) {
        return new RolesVO()
                .setId(rolePO.getId())
                .setRoleDesc(rolePO.getDesc())
                .setRoleName(rolePO.getRoleName())
                .setChildren(child);
    }

    private RolesVO getRoleInfo(RolePO role) {
        List<RightVO> child = new LinkedList<>();
        RolesVO rolesVO = transformRolesPO2RoleVO(role, child);
        List<Integer> ids = role.getIds();
        // 要是ids为空， 数据库直接报错 'where id in'  (sql错误)
        if(ids != null && !ids.isEmpty()) {
            List<RightsPO> rights = permissionMapper.getPermissionsByIds(ids);
            if (rights != null && !rights.isEmpty()) {
                // 一级权限
                rights.stream()
                        .filter(right -> RightsEnum.HIGH.getLevel().equals(right.getLevel()))
                        .forEach(parentRight -> {
                    // 二级权限
                    List<RightVO> childRights = getChildRights(parentRight.getId(), RightsEnum.MIDDLE.getLevel(), rights);
                    // 为每个二级权限赋值三级权限
                    childRights.forEach(item -> {
                        item.setChildren(getChildRights(item.getId(), RightsEnum.LOW.getLevel(), rights));
                    });
                    child.add(transformRightPO2RightVO(parentRight, childRights));
                });
            }
        }
        return rolesVO;
    }

    private void deleteRoleRecursive(List<RightVO> rights, int rightId) {
        if(rights == null || rights.isEmpty()) {
            return;
        }
        for (RightVO right : rights) {
            if(right.getId() == rightId) {
                rights.remove(right);
                return;
            } else {
                deleteRoleRecursive(right.getChildren(), rightId);
            }
        }
    }

    private void getRoleIdsRecursive(List<Integer> ids, List<RightVO> rights) {
        if(rights == null || rights.isEmpty()) {
            return;
        }
        for (RightVO right : rights) {
            ids.add(right.getId());
            getRoleIdsRecursive(ids, right.getChildren());
        }
    }
}
