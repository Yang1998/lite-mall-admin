package com.yyt.axios.controller.user;

import com.yyt.axios.enums.CodeEnum;
import com.yyt.axios.service.UserService;
import com.yyt.axios.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@Slf4j
@Validated
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 获取用户列表
     *
     * @param query    查询参数可以为空，作用是对用户名进行模糊查询
     * @param pageNum  当前页码
     * @param pageSize 一页的数据条数
     * @return
     */
    @GetMapping("/users")
    @RequiresPermissions({"users:index"})
    public BaseVO<UserListVO> getUserList(String query, @NotNull(message = "页码不能为空") Integer pageNum, @NotNull(message = "页大小不能为空") Integer pageSize) {
        try {
            return new BaseVO<UserListVO>()
                    .setState(CodeEnum.GET_USERLIST_SUCCESS)
                    .setData(userService.getAllUsers(query, pageNum, pageSize));
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return new BaseVO<UserListVO>().setState(CodeEnum.BAD_REQUEST);
        }
    }

    @PutMapping("/users/{uid}/state/{state}")
    @RequiresPermissions({"users:update"})
    public BaseVO<UserStateVO> changeUserState(@PathVariable("uid") Integer uid,
                                               @PathVariable("state") Boolean state) {
        try {
            return new BaseVO<UserStateVO>()
                    .setData(userService.doChangeUserState(uid, state))
                    .setState(CodeEnum.UPDATE_USER_STATE_SUCCESS);
        } catch (Exception e) {
            log.debug("更新用户状态失败 uid = {}", uid);
            return new BaseVO<UserStateVO>().setState(CodeEnum.UPDATE_USER_STATE_ERROR);
        }
    }

    @PostMapping("/users")
    @RequiresPermissions({"users:add"})
    public BaseVO<UserStateVO> createUser(@RequestBody UserInfo userInfo) {
        try {
            return new BaseVO<UserStateVO>()
                    .setState(CodeEnum.CREATE_USER_SUCCESS)
                    .setData(userService.doCreateUser(userInfo));
        } catch (Exception e) {
            log.error("创建用户失败", e);
            return new BaseVO<UserStateVO>().setState(CodeEnum.CREATE_USER_FAIL);
        }
    }

    @GetMapping("/users/{id}")
    @RequiresPermissions({"users:index"})
    public BaseVO<ManagerVO> getUserInfo(@PathVariable("id") Integer id) {
        try {
            return new BaseVO<ManagerVO>()
                    .setState(CodeEnum.QUERY_USER_SUCCESS)
                    .setData(userService.getUserById(id));
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return new BaseVO<ManagerVO>().setState(CodeEnum.QUERY_USER_FAIL);
        }
    }

    @PutMapping("/users/{id}")
    @RequiresPermissions({"users:update"})
    public BaseVO<ManagerVO> updateUserInfo(@PathVariable("id") Integer id,
                                            @RequestBody EditUserInfo userInfo) {
        try {
            return new BaseVO<ManagerVO>()
                    .setData(userService.doUpdateUserInfo(id, userInfo))
                    .setState(CodeEnum.UPDATE_USERINFO_SUCCESS);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return new BaseVO<ManagerVO>().setState(CodeEnum.UPDATE_USERINFO_ERROR);
        }
    }

    @DeleteMapping("/users/{id}")
    @RequiresPermissions({"users:delete"})
    public BaseVO deleteUser(@PathVariable("id") Integer id) {
        try {
            boolean res = userService.doDeleteUser(id);
            if(res) {
                return new BaseVO().setState(CodeEnum.DELETE_USER_SUCCESS);
            }
            // 这通常是用户不存在发生的情况
            return new BaseVO().setState(CodeEnum.DELETE_USER_ERROR);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return new BaseVO().setState(CodeEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users/{id}/role")
    @RequiresPermissions({"users:update"})
    public BaseVO<UserStateVO> addRole(@PathVariable("id") Integer id, @RequestBody RidVO ridVO) {
        try {
            UserStateVO userState = userService.doAddRole(id, ridVO.getRid());
            if(userState != null) {
                return new BaseVO<UserStateVO>()
                        .setData(userState)
                        .setState(CodeEnum.ADD_ROLE_SUCCESS);
            } else {
                throw new RuntimeException("用户不存在 id " + id + " 不存在或者角色不存在 id " + ridVO.getRid());
            }
        } catch (Exception e) {
            return new BaseVO<UserStateVO>()
                    .setState(CodeEnum.ADD_ROLE_ERROR);
        }
    }
}
