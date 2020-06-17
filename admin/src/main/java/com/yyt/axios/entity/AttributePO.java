package com.yyt.axios.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributePO {
    private int attr_id;
    private String attr_name;
    private int cat_id;
    private String attr_sel;
    private String attr_write;
    private String attr_vals;
    private int delete_time;
}
