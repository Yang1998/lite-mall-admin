package com.yyt.axios.service.impl;

import com.yyt.axios.entity.RightsPO;
import com.yyt.axios.enums.RightsEnum;
import com.yyt.axios.enums.RightsRequestTypeEnum;
import com.yyt.axios.mapper.PermissionMapper;
import com.yyt.axios.service.RightsService;
import com.yyt.axios.vo.RightsListVO;
import com.yyt.axios.vo.RightsTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RightsServiceImpl implements RightsService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Object getAllRights(String type) {
        // 在controller里面已经检验过了，不会有异常的
        if (RightsRequestTypeEnum.LIST.getType().equals(type)) {
            return getListRights();
        } else if (RightsRequestTypeEnum.TREE.getType().equals(type)) {
            return getTreeRights();
        } else {
            throw new RuntimeException("请求的类型有误 type = " + type);
        }
    }

    @Override
    public List<String> getPermissionsByIds(List<Integer> ids) {
        if(ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return permissionMapper.getPermissionStrings(ids);
    }

    private List<RightsListVO> getListRights() {
        List<RightsPO> rights = permissionMapper.getAllPermissions();
        List<RightsListVO> res = new ArrayList<>();
        rights.forEach(right -> {
            RightsListVO rightsListVO = new RightsListVO()
                    .setAuthName(right.getAuthName())
                    .setId(right.getId())
                    .setLevel(right.getLevel())
                    .setPath(right.getPath())
                    .setPid(right.getPid());
            res.add(rightsListVO);
        });
        return res;
    }

    private List<RightsTreeVO> getTreeRights() {
        List<RightsPO> rights = permissionMapper.getAllPermissions();
        List<RightsTreeVO> res = new ArrayList<>();
        if (rights != null) {
            // 一级权限
            rights.stream()
                    .filter(right -> RightsEnum.HIGH.getLevel().equals(right.getLevel()))
                    .forEach(right -> {
                        // 二级权限
                        List<RightsTreeVO> childRights = getChildRights(right.getId(), RightsEnum.MIDDLE.getLevel(), rights);
                        childRights.forEach(childRight -> {
                            // 三级权限
                            List<RightsTreeVO> children = getChildRights(childRight.getId(), RightsEnum.LOW.getLevel(), rights);
                            // 这里要更改三级权限的pid 它的pid 等于二级的pid 加上他本身的
                            children.forEach(child -> child.setPid(childRight.getPid() + "," +  child.getPid()));
                            childRight.setChildren(children);
                        });
                        res.add(transformRightsPO2RightsTreeVO(right).setChildren(childRights));
                    });
        }
        return res;
    }

    /**
     * 将rightsPO转换为RightsTreeVO, 注意不设置children属性
     *
     * @param rightsPO
     * @return
     */
    private RightsTreeVO transformRightsPO2RightsTreeVO(RightsPO rightsPO) {
        return new RightsTreeVO()
                .setId(rightsPO.getId())
                .setAuthName(rightsPO.getAuthName())
                .setPath(rightsPO.getPath())
                .setPid(rightsPO.getPid() + "");
    }

    private List<RightsTreeVO> getChildRights(int pid, String level, List<RightsPO> rights) {
        List<RightsTreeVO> res = new ArrayList<>();
        rights.stream()
                .filter(right -> pid == right.getPid() && level.equals(right.getLevel()))
                .forEach(right -> {
                    res.add(transformRightsPO2RightsTreeVO(right));
                });
        return res;
    }
}
