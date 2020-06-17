package com.yyt.axios.config;

import com.yyt.axios.shiro.CustomSessionManager;
import com.yyt.axios.shiro.MyAuthenticationFilter;
import com.yyt.axios.shiro.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    /**
     * shiroFilterFactoryBean
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("myAuthenticationFilter", new MyAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        /**
         * 添加shiro的内置过滤器 实现权限相关的拦截
         *  anon: 无需认证 即可匿名访问
         *  authc: 必须认证才可以使用
         *  user: 如果使用rememberMe的功能可以直接访问
         *  perms: 该资源必须授予权限才可以访问
         *  role: 该资源必须得到角色权限
         *   这里为保证顺序，使用linkedHashMap
         */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/**", "myAuthenticationFilter");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // 修改跳转的登录界面
//        shiroFilterFactoryBean.setLoginUrl("/unauth");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/login");
//        shiroFilterFactoryBean.setSuccessUrl("/home");
        return shiroFilterFactoryBean;
    }

    /**
     * 创建DefaultWebSecurityManager
     */
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(UserRealm userRealm, DefaultWebSessionManager sessionManager) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(userRealm);
        defaultWebSecurityManager.setSessionManager(sessionManager);
        return defaultWebSecurityManager;
    }

    /**
     * 创建Realm
     */
    @Bean
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher("MD5");
//        hashedCredentialsMatcher.setHashIterations(1);
//        userRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return userRealm;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

//    @Bean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
//        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
//        return defaultAdvisorAutoProxyCreator;
//    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Autowired DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;

    }

    @Bean
    public DefaultWebSessionManager defaultWebSessionManager() {
        CustomSessionManager customSessionManager = new CustomSessionManager();
        customSessionManager.setSessionIdUrlRewritingEnabled(false);
        return customSessionManager;
    }

//    @Bean
//    public FilterRegistrationBean delegatingFilterProxy() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
//        proxy.setTargetFilterLifecycle(true);
//        proxy.setTargetBeanName("shiroFilter");
//        filterRegistrationBean.setFilter(proxy);
//        return filterRegistrationBean;
//    }
}
