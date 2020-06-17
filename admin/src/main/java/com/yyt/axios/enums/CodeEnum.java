package com.yyt.axios.enums;

/**
 * 系统状态枚举
 */
public enum CodeEnum {
    LOGIN_SUCCESS(200, "登陆成功"),
    GET_USERLIST_SUCCESS(200, "获取成功"),
    GET_MENU_SUCCESS(200, "获取菜单列表成功"),
    UPDATE_USER_STATE_SUCCESS(200, "设置状态成功"),
    QUERY_USER_SUCCESS(200, "查询成功"),
    UPDATE_USERINFO_SUCCESS(200, "更新用户成功"),
    DELETE_USER_SUCCESS(200, "删除成功"),
    UPDATE_SUCCESS(200, "更新成功"),
    UPDATE_ERROR(400, "更新失败"),
    GET_PERMISSIONLIST_SUCCESS(200, "获取权限列表成功"),
    GET_ROLESLIST_SUCCESS(200, "获取角色列表成功"),
    GET_SUCCESS(200, "获取成功"),
    DELETE_RIGHT_SUCCESS(200, "删除权限成功"),
    ADD_ROLE_SUCCESS(200, "设置角色成功"),
    UPLOAD_SUCCESS(200, "上传成功"),
    UPLOAD_ERROR(400, "上传失败"),
    ADD_ROLE_ERROR(400, "设置角色失败"),
    DELETE_RIGHT_ERROR(400, "删除权限失败"),
    GET_ERROR(404, "获取失败"),
    INSERT_ROLE_SUCCESS(201, "创建成功"),
    INSERT_ROLE_ERROR(422, "创建失败"),
    UPDATE_USERINFO_ERROR(400, "更新用户失败"),
    UPDATE_USER_STATE_ERROR(400, "更改用户状态失败"),
    CREATED(201, "创建成功"),
    CREATE_USER_SUCCESS(201, "创建用户成功"),
    ADD_CATEGORY_SUCCESS(201, "添加商品分类成功"),
    ADD_CATEGORY_ERROR(400, "添加商品分类失败"),
    DELETE_SUCCESS(204, "删除成功"),
    DELETE_ERROR(400, "删除失败"),
    BAD_REQUEST(400, "请求的地址不存在或者包含不支持的参数"),
    GET_PERMISSIONLIST_ERROR(400, "获取权限列表失败"),
    GET_ROLESLIST_ERROR(400, "获取角色列表失败"),
    USERNAME_PASSWORD_ERROR(400, "用户名或密码错误, 请重新登录"),
    REQUEST_PARAMS_ERROR(400, "请求参数有误"),
    REQUEST_PARAMS_VALIDATION_ERROR(400, "请求参数验证失败"),
    REQUEST_METHOD_NOT_SUPPORTED_ERROR(405, "请求方法错误"),
    INSERT_SUCCESS(201, "添加成功"),
    INSERT_ERROR(422, "添加失败"),
    UNAUTHORIZED(401, "未授权"),
    UNLOGIN(405, "未登录"),
    FORBIDDEN(403, "被禁止访问"),
    NOT_FOUND(404, "请求的资源不存在"),
    DELETE_ROLE_ERROR(404, "删除角色失败"),
    DELETE_USER_ERROR(404, "删除用户失败"),
    QUERY_USER_FAIL(404, "用户不存在"),
    UNPROCESABLE(422, "创建对象验证错误"),
    CREATE_USER_FAIL(422, "创建用户失败"),
    INTERNAL_SERVER_ERROR(500, "内部错误");
    private int code;
    private String msg;
    private CodeEnum(int code, String msg) {
        this.code = code;
        this.msg  = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
