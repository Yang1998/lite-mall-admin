package com.yyt.axios.mapper;

import com.yyt.axios.entity.PermissionPO;
import com.yyt.axios.entity.RightsPO;
import com.yyt.axios.vo.RoleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper {
    List<PermissionPO> getPermissionListByPidLevel(@Param("id") Integer id, @Param("level") String level);

    String getPathByPsid(@Param("psid") int psid);

    List<RightsPO> getAllPermissions();

    /**
     * 通过id的列表查询
     * @param ids id列表
     */
    List<RightsPO> getPermissionsByIds(@Param("ids") List<Integer> ids);

    List<String> getPermissionStrings(@Param("ids") List<Integer> ids);
}
