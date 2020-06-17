package com.yyt.axios.service;

import java.util.List;

public interface RightsService {
    Object getAllRights(String type);

    List<String> getPermissionsByIds(List<Integer> ids);
}
