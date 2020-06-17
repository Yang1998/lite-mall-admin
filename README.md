# lite-mall-admin
电商后台管理系统 前端使用vue+element.ui，后端使用springboot+shiro

## 前端

进入admin-vue，运行

```bash
npm instal
npm run serve
```

如果依赖下载缓慢，可用cnpm

## 后端

填上自己本地的spring.datasource.url，spring.redis.host以及在classpath下创建名为OSSConfig.yml的配置文件，填写自己的阿里云oss服务信息。

### shiro

1. 项目不再基于session了，如何知道访问者是谁?
2. 如何确认访问者的权限？

**前后端分离，一般都是通过token实现；用户登录时，生成token及 token过期时间，token与用户是一一对应关系，调用接口的时候，把token放到header或者请求参数中，服务端就知道是谁在调用接口。**

shiro提供的FormAuthenticationFilter达不到要求，只能继承AuthenticatingFilter，实现自定义Filter, 因为跳转是前端完成，后端无需干涉，还有默认过滤器是基于session cookie的，也不适合本项目。

#### 权限设计

通常我们的权限设计都是 用户--角色--权限 ,其中**角色**是我们写代码的人没法控制的,它可以有多条权限,每个用户又可以设计为拥有多个角色.因此如果从角色着手进行权限验证,系统都必须根据用户的配置动起来,非常复杂.

所以我们后台设计的关键点就在于: **后台接口只验证权限,不看角色.**

角色的作用其实只是用来管理分配权限的,真正的验证只验证**权限** ,而不去管你是否是那种角色.体现在代码上就是接口上注解为

```java
@RequiresPermissions("goods:add")
```

而不是

```java
@RequiresRoles(value = {"admin","manager","test"}, logical = Logical.OR) 
```

## TODO

1. 前端通过后端的权限信息，通过动态路由**隐藏掉不具有权限的菜单(路由)和按钮**