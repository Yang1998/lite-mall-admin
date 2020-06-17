package com.yyt.axios.controller.permission;

import com.yyt.axios.enums.CodeEnum;
import com.yyt.axios.service.RightsService;
import com.yyt.axios.vo.BaseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

@RestController
@Slf4j
@Validated
public class RightsController {
    @Autowired
    RightsService rightsService;

    @GetMapping("/rights/{type}")
    @RequiresPermissions({"rights:index"})
    public BaseVO getAllRights(@PathVariable("type")
                                   @Pattern(regexp = "^(tree|list)$", message = "type值只能为tree或list") String type) {
        try {
            return new BaseVO<>()
                    .setState(CodeEnum.GET_PERMISSIONLIST_SUCCESS)
                    .setData(rightsService.getAllRights(type));
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new BaseVO().setState(CodeEnum.GET_PERMISSIONLIST_ERROR);
        }
    }
}
