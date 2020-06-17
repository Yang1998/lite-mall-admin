package com.yyt.axios.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yyt.axios.entity.ManagerPO;
import com.yyt.axios.mapper.ManagerMapper;
import com.yyt.axios.mapper.RoleMapper;
import com.yyt.axios.service.UserService;
import com.yyt.axios.util.MD5Util;
import com.yyt.axios.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    // 对应用户状态的枚举， 1 表示 启用 0 表示禁用
    private static final int ENABLE = 1;
    private static final int DISABLE = 0;

    private static final int DEFAULTROLEID = -1;

    @Autowired
    ManagerMapper managerMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    MD5Util md5Util;
    @Override
    public UserListVO getAllUsers(String query, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserVO> users = new ArrayList<>();
        List<ManagerPO> allManagers = managerMapper.getAllManagers(query);
        PageInfo<ManagerPO> managers = new PageInfo<>(allManagers);
        managers.getList().forEach(manager -> {
            UserVO userVO = new UserVO()
                    .setUsername(manager.getName())
                    .setEmail(manager.getEmail())
                    .setId(manager.getId())
                    .setMobile(manager.getMobile())
                    .setState(manager.getState() == 1)
                    .setCreateTime(new Date(manager.getTime()))
                    .setRoleName(roleMapper.getRoleNameByRoleId(manager.getRoleId()));
            users.add(userVO);
        });
        return new UserListVO()
                .setPageNum(managers.getPageNum())
                .setTotalPage(managers.getTotal())
                .setUsers(users);
    }

    @Transactional
    @Override
    public UserStateVO doChangeUserState(int uid, boolean state) {
        int res = managerMapper.updateUserState(uid, state ? 1 : 0);
        if(res == 0) {
            throw new RuntimeException("用户id = " + uid + "不存在");
        }
        ManagerPO managerFromDB = managerMapper.getManagerById(uid);
        return new UserStateVO()
                .setRid(managerFromDB.getRoleId())
                .setEmail(managerFromDB.getEmail())
                .setId(managerFromDB.getId())
                .setMobile(managerFromDB.getMobile())
                .setUsername(managerFromDB.getName())
                .setState(managerFromDB.getState());
    }

    @Transactional
    @Override
    public UserStateVO doCreateUser(UserInfo userInfo) throws UnsupportedEncodingException {
        if (!Objects.equals(userInfo.getPassword(), userInfo.getReconfirmPassword())) {
            throw new RuntimeException("用户的确认密码有误");
        }
        List<ManagerPO> managerByName = managerMapper.getManagerByName(userInfo.getUsername());
        if(managerByName != null) {
            throw new RuntimeException("用户：" + userInfo.getUsername() + "已存在");
        }
        ManagerPO managerPO = new ManagerPO()
                .setEmail(userInfo.getEmail())
                .setMobile(userInfo.getPhone())
                .setPwd(md5Util.encrypByMD5(userInfo.getPassword()))
                .setTime(System.currentTimeMillis() / 1000)
                .setRoleId(DEFAULTROLEID)
                .setName(userInfo.getUsername())
                .setState(ENABLE);
        managerMapper.createUser(managerPO);
        return new UserStateVO()
                .setUsername(managerPO.getName())
                .setEmail(managerPO.getEmail())
                .setId(managerPO.getId())
                .setMobile(managerPO.getMobile())
                .setRid(managerPO.getRoleId());
    }

    @Override
    public ManagerVO getUserById(Integer id) {
        ManagerPO manager = managerMapper.getManagerById(id);
        if (manager == null) {
            throw new RuntimeException("用户id :" + id + "不存在!");
        }
        return new ManagerVO()
                .setRid(manager.getRoleId())
                .setMobile(manager.getMobile())
                .setEmail(manager.getEmail())
                .setId(manager.getId())
                .setUsername(manager.getName());
    }

    @Transactional
    @Override
    public ManagerVO doUpdateUserInfo(Integer id, EditUserInfo userInfo) {
        Integer res = managerMapper.updateUserInfo(id, userInfo);
        if(res != 1) {
            throw new RuntimeException("更新用户失败 userId = " + id);
        }
        ManagerPO manager = managerMapper.getManagerById(id);
        return new ManagerVO()
                .setRid(manager.getRoleId())
                .setMobile(manager.getMobile())
                .setEmail(manager.getEmail())
                .setId(manager.getId())
                .setUsername(manager.getName());
    }

    @Transactional
    @Override
    public boolean doDeleteUser(Integer id) {
        return managerMapper.deleteUserById(id);
    }

    @Transactional
    @Override
    public UserStateVO doAddRole(int id, int rid) {
        int cnt = managerMapper.setRoleId(id, rid);
        if(cnt != 1) {
            return null;
        }
        ManagerPO manager = managerMapper.getManagerById(id);
        return new UserStateVO()
                .setId(manager.getId())
                .setEmail(manager.getEmail())
                .setMobile(manager.getMobile())
                .setRid(manager.getRoleId());
    }

}
