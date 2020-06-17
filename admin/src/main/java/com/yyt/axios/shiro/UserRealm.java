package com.yyt.axios.shiro;

import com.yyt.axios.entity.ManagerPO;
import com.yyt.axios.entity.RolePO;
import com.yyt.axios.service.RightsService;
import com.yyt.axios.service.RoleService;
import com.yyt.axios.service.UserService;
import com.yyt.axios.util.MD5Util;
import com.yyt.axios.util.StringCacheUtil;
import com.yyt.axios.vo.ManagerVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * 自定义的用户Realm
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RightsService rightsService;

    @Autowired
    StringCacheUtil stringCacheUtil;
    @Autowired
    MD5Util md5Util;

    /**
     * 执行授权逻辑
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        ManagerVO user = (ManagerVO) principalCollection.getPrimaryPrincipal();
        RolePO role = roleService.dogetRolePO(user.getRid());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        if (role != null) {
            simpleAuthorizationInfo.addRole(role.getRoleName());
            List<String> permissions = rightsService.getPermissionsByIds(role.getIds());
            simpleAuthorizationInfo.addStringPermissions(permissions);
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 执行认证逻辑
     * <h3>没有使用shiro提供的盐值加密</h3>
     * 在方法里返回null shiro会自动抛出UnknownAccountException
     * 在最后的返回的AuthenticationInfo 是比较密码的关键
     * 比较密码会调用 doCredentialsMatch 传入token 和 AuthenticationInfo
     * 如果不匹配会抛出IncorrectCredentialsException
     * 如果不适用shiro提供的这个特性， 在创建AuthenticationInfo 时， 把credentials设置为用户传过来的密码
     * 这样在比较时就一定会相等
     * 如果想使用shiro提供的密码比较的话，对于盐值加密来说，需要在创建realm时传入CredentialsMatcher
     * 推荐使用HashedCredentialsMatcher 然后再设置它的HashIterations(加密次数)
     * 在创建SimpleAuthenticationInfo 时，指定盐值
     *
     * @param authenticationToken
     * @return SimpleAuthenticationInfo
     * @throws AuthenticationException 认证异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        AuthToken token = (AuthToken) authenticationToken;
//        String username = token.getUsername();
//        String password = new String(token.getPassword());
//
//        if(StringUtils.isEmpty(username)) {
//            throw new RuntimeException("用户名不能为空");
//        }
//
//        if(StringUtils.isEmpty(password)) {
//            throw new RuntimeException("用户密码不能为空");
//        }
//
//        List<ManagerPO> userFromDb = userService.getUsersByName(username);
//        Assert.state(userFromDb.size() < 2, "一个用户名存在两个账户");
//        if(userFromDb.size() == 0) {
//            throw new UnknownAccountException("找不到用户（" + username + "）的帐号信息");
//        }
//        ManagerPO user = userFromDb.get(0);
//        if(!md5Util.verify(password, user.getPwd())) {
//            throw new IncorrectCredentialsException("用户(" + username +  ")密码错误");
//        }
//        return new SimpleAuthenticationInfo(user, password, getName());

        String token = (String) authenticationToken.getPrincipal();
        String value = stringCacheUtil.getValue(token);
        if (value == null) {
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }
        ManagerVO user = userService.getUserById(Integer.parseInt(value));
        if (user == null) {
            throw new UnknownAccountException("用户不存在!");
        }
        return new SimpleAuthenticationInfo(user, token, getName());
    }
}
