package com.yyt.axios.service;

import com.yyt.axios.vo.ManagerVO;
import com.yyt.axios.vo.UserLoginVO;

public interface LoginService {
    ManagerVO doLogin(UserLoginVO loginVO);
}
