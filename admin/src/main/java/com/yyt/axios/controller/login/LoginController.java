package com.yyt.axios.controller.login;

import com.yyt.axios.enums.CodeEnum;
import com.yyt.axios.service.LoginService;
import com.yyt.axios.service.UserService;
import com.yyt.axios.util.HttpUtil;
import com.yyt.axios.util.StringCacheUtil;
import com.yyt.axios.vo.BaseVO;
import com.yyt.axios.vo.UserLoginVO;
import com.yyt.axios.vo.ManagerVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Autowired
    private StringCacheUtil stringCacheUtil;

    // key 的过期时间 10小时
    private static final long EXPIRE_TIME = 10 * 60 * 60;

    @PostMapping("/login")
    public BaseVO<ManagerVO> login(@RequestBody UserLoginVO login) {
        try {
            ManagerVO managerVO = loginService.doLogin(login);
            if (managerVO == null) {
                throw new RuntimeException("用户 " + login.getUsername() + " 不存在");
            }
            stringCacheUtil.setValue(managerVO.getToken(), managerVO.getId() + "", EXPIRE_TIME);
            return new BaseVO<ManagerVO>()
                    .setData(managerVO)
                    .setState(CodeEnum.LOGIN_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new BaseVO<ManagerVO>().setState(CodeEnum.USERNAME_PASSWORD_ERROR);
        }


//        /**
//         * 使用Shiro编写认证逻辑
//         */
//        // 1. 获取Subject
//        Subject subject = SecurityUtils.getSubject();
//        // 2. 封装用户数据
//        UsernamePasswordToken token = new UsernamePasswordToken(login.getUsername(), login.getPassword());
//        // 3. 执行登录方法
//        try {
//            if (!subject.isAuthenticated()) {
//                subject.login(token);
//            }
//            ManagerVO managerVO = loginService.doLogin(login);
//            return new BaseVO<ManagerVO>()
//                    .setData(managerVO)
//                    .setState(CodeEnum.LOGIN_SUCCESS);
//        } catch (UnknownAccountException | IncorrectCredentialsException e) {
//            log.error(e.getMessage(), e);
//            return new BaseVO<ManagerVO>().setState(CodeEnum.USERNAME_PASSWORD_ERROR);
//        }
    }

    @PostMapping("/logout")
    public BaseVO<Object> logout(ServletRequest servletRequest) {
        String token = HttpUtil.getRequestToken((HttpServletRequest) servletRequest);
        if (token != null) {
            stringCacheUtil.delKey(token);
        }
        return new BaseVO<>().setState(CodeEnum.LOGIN_SUCCESS);
    }
}
