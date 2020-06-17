package com.yyt.axios.enums;

/**
 * 权限等级分为三级，0 ， 1， 2 高 中 低
 */
public enum RightsEnum {
    HIGH("0"),
    MIDDLE("1"),
    LOW("2");
    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    RightsEnum(String level) {
        this.level = level;
    }
}
