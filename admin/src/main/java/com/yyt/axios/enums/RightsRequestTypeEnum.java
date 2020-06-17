package com.yyt.axios.enums;

public enum RightsRequestTypeEnum {
    LIST("list"),
    TREE("tree");
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    RightsRequestTypeEnum(String type) {
        this.type = type;
    }
}
