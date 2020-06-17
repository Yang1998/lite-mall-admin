package com.yyt.axios.controller.menu;

import com.yyt.axios.enums.CodeEnum;
import com.yyt.axios.annotation.Logging;
import com.yyt.axios.service.MenuService;
import com.yyt.axios.vo.BaseVO;
import com.yyt.axios.vo.MenuVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MenuController {
    @Autowired
    MenuService menuService;

    @GetMapping("/menus")
    public BaseVO<List<MenuVO>> getMeuns() {
        return new BaseVO<List<MenuVO>>()
                .setState(CodeEnum.GET_MENU_SUCCESS)
                .setData(menuService.getMenuList());
    }
}
