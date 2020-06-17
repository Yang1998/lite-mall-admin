package com.yyt.axios.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class CateVO {
    private int cat_id;
    private String cat_name;
    private int cat_pid;
    private int cat_level;
    private boolean cat_deleted;
    private List<CateVO> children;
}
