package com.yyt.axios.service.impl;

import com.yyt.axios.entity.Header;
import com.yyt.axios.entity.ManagerPO;
import com.yyt.axios.entity.PayLoad;
import com.yyt.axios.mapper.ManagerMapper;
import com.yyt.axios.service.LoginService;
import com.yyt.axios.util.JwtUtil;
import com.yyt.axios.util.MD5Util;
import com.yyt.axios.util.RsaUtil;
import com.yyt.axios.vo.ManagerVO;
import com.yyt.axios.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    ManagerMapper managerMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MD5Util md5Util;

    @Value("${com.yyt.jwt.algorithm}")
    private String algorithm;

    @Value("${com.yyt.keys.RSPrivateKey}")
    private String privateKey;

    @Override
    public ManagerVO doLogin(UserLoginVO loginVO) {
        ManagerVO res = new ManagerVO();
        List<ManagerPO> managersFromDB = managerMapper.getManagerByName(loginVO.getUsername());
        // 这里要注意下， state = 1表示该用户启用， 0 表示禁用
        ManagerPO managerFromDB;
        if(managersFromDB.size() != 1 || (managerFromDB = managersFromDB.get(0)).getState() == 0) {
            return null;
        }
        try {
            // 将前端传过来的密码解密
            loginVO.setPassword(RsaUtil.decrypt(loginVO.getPassword(), privateKey));
            if(md5Util.verify(loginVO.getPassword(), managerFromDB.getPwd(), managerFromDB.getSalt())) {
                Header header = jwtUtil.getHeader();
                PayLoad payLoad = jwtUtil.getPayLoad(loginVO.getUsername());
                return res.setId(managerFromDB.getId())
                        .setUsername(managerFromDB.getName())
                        .setEmail(managerFromDB.getEmail())
                        .setMobile(managerFromDB.getMobile())
                        .setRid(managerFromDB.getRoleId())
                        .setToken(jwtUtil.encryptToken(header, payLoad, jwtUtil.getAlgorithm(algorithm)));

            } else {
                throw new RuntimeException("用户 " + loginVO.getUsername() + " 登录密码错误: password=" + loginVO.getPassword());
            }
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
