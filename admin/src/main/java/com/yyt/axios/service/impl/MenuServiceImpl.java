package com.yyt.axios.service.impl;

import com.yyt.axios.mapper.PermissionMapper;
import com.yyt.axios.entity.PermissionPO;
import com.yyt.axios.service.MenuService;
import com.yyt.axios.vo.MenuVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Data
public class MenuServiceImpl implements MenuService {
    private static final String FLEVEL = "0";

    private static final String CLEVEL = "1";

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     *   这里如果层数过深，可改写递归版
     *   但是我这里只有三层， 为了方便就直接写的迭代
     * @return 树级列表
     */
    @Override
    public List<MenuVO> getMenuList() {
        List<PermissionPO> fatherPermissions = permissionMapper.getPermissionListByPidLevel(null, FLEVEL);
        List<MenuVO> res = new ArrayList<>();
        fatherPermissions.forEach(father -> {
            List<PermissionPO> childrens = permissionMapper.getPermissionListByPidLevel(father.getId(), CLEVEL);
            List<MenuVO> childs = new ArrayList<>();
            childrens.forEach(child -> {
                MenuVO childMenu = new MenuVO()
                        .setId(child.getId())
                        .setAuthName(child.getName())
                        .setPath(permissionMapper.getPathByPsid(child.getId()));
                childs.add(childMenu);
            });
            MenuVO fatherMenu = new MenuVO()
                    .setAuthName(father.getName())
                    .setId(father.getId())
                    .setPath(permissionMapper.getPathByPsid(father.getId()))
                    .setChildren(childs);
            res.add(fatherMenu);
        });
        return res;
    }
}
