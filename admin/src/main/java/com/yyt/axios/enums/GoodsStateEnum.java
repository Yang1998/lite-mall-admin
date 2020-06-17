package com.yyt.axios.enums;

public enum GoodsStateEnum implements ICode{
    NOT_PASS(0, "未通过"),
    UNDER_REVIEW(1, "审核中"),
    AUDITED(2, "已审核");

    private Integer code;
    private String msg;

    GoodsStateEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
