package com.yyt.axios.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPO {
    private int cat_id;
    private String cat_name;
    private int cat_pid;
    private int cat_level;
    private int cat_deleted;
    private String cat_icon;
    private String cat_src;
}
