package com.yyt.axios.enums;

/**
 * 分类层级的枚举
 */
public enum CategoryLevelEnum {
    ONE(0),
    TWO(1),
    THREE(2);
    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    CategoryLevelEnum(int level) {
        this.level = level;
    }

    /**
     * 通过level得到请求时的type,其实就相差1
     * @param level
     * @return
     */
    public int getTypeByLevel() {
        return this.getLevel() + 1;
    }

    public static CategoryLevelEnum getLevelByType(int type) {
        if(type == 1) {
            return ONE;
        } else if(type == 2) {
            return TWO;
        } else{
            // 其他情况， 一律三级
            return THREE;
        }
    }
}
