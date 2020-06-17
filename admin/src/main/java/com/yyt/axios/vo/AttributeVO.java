package com.yyt.axios.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class AttributeVO {
    private int attr_id;
    private String attr_name;
    private int cat_id;
    private String attr_sel;
    private String  attr_write;
    private String attr_vals;
}
