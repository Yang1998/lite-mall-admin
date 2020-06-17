package com.yyt.axios.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissonApiPO {
    private int id;
    private int psid;
    private String apiService;
    private String apiAction;
    private String apiPath;
    private int apiOrder;
}
