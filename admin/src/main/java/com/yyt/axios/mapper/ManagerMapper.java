package com.yyt.axios.mapper;

import com.yyt.axios.entity.ManagerPO;
import com.yyt.axios.vo.EditUserInfo;
import com.yyt.axios.vo.ManagerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManagerMapper {
    List<ManagerPO> getManagerByName(@Param("name") String name);

    List<ManagerPO> getAllManagers(@Param("query") String query);

    ManagerPO getManagerById(@Param("uid") int uid);

    int updateUserState(@Param("uid") int uid, @Param("state") int state);

    Integer createUser(@Param("manager") ManagerPO managerPO);

    Integer updateUserInfo(@Param("uid") Integer id, @Param("userInfo") EditUserInfo userInfo);

    boolean deleteUserById(@Param("uid") Integer id);

    int setRoleId(@Param("id") int id, @Param("rid") int rid);
}
