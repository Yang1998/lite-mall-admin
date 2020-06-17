package com.yyt.axios.enums;

public enum AlgorithmEnum {
    RS256("RS256"),
    HS256("HS256");
    private String name;
    AlgorithmEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
