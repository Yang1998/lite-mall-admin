package com.yyt.axios.service;

import com.yyt.axios.entity.ManagerPO;
import com.yyt.axios.vo.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {
    UserListVO getAllUsers(String query, Integer pageNum, Integer pageSize);

    UserStateVO doChangeUserState(int uid, boolean state);

    UserStateVO doCreateUser(UserInfo userInfo) throws UnsupportedEncodingException;

    ManagerVO getUserById(Integer id);

    ManagerVO doUpdateUserInfo(Integer id, EditUserInfo userInfo);

    boolean doDeleteUser(Integer id);

    UserStateVO doAddRole(int id, int rid);

}
