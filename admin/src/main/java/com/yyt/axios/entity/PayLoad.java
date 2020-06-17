package com.yyt.axios.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayLoad {
    private String username;
    private Date iat;

    public static final String USERNAME = "username";
}
