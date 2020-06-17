package com.yyt.axios.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RightsPO {
    private int id;
    private String authName;
    private String level;
    private int pid;
    private String path;
}
