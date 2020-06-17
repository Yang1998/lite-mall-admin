package com.yyt.axios.controller.goods;

import com.yyt.axios.enums.CodeEnum;
import com.yyt.axios.service.GoodsService;
import com.yyt.axios.vo.BaseVO;
import com.yyt.axios.vo.GoodsAddVO;
import com.yyt.axios.vo.GoodsDetailVO;
import com.yyt.axios.vo.GoodsPageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@RestController
@Slf4j
public class GoodsController {
    @Autowired
    GoodsService goodsService;

    @GetMapping("/goods")
    @RequiresPermissions({"goods:index"})
    public BaseVO<GoodsPageVO> getGoodsPageInfo(String query,
                                                @NotNull(message = "页码不能为空") @Min(value = 1, message = "页码最小为1") Integer pageNum,
                                                @NotNull(message = "页的大小不能为空") @Min(value = 1, message = "页的大小最小为1") Integer pageSize) {
        try {
            return new BaseVO<GoodsPageVO>()
                    .setState(CodeEnum.GET_SUCCESS)
                    .setData(goodsService.doGetGoodsList(query, pageNum, pageSize));
        }catch (Exception e) {
            log.debug(e.getMessage(), e);
            return new BaseVO<GoodsPageVO>().setState(CodeEnum.GET_ERROR);
        }
    }

    @PostMapping("/goods")
    @RequiresPermissions({"goods:add"})
    public BaseVO<Object> addGoods(@RequestBody GoodsAddVO goodsAddVO) {
        try {
            boolean res = goodsService.doAddGoods(goodsAddVO);
            return new BaseVO<Object>().setState(CodeEnum.INSERT_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseVO<Object>().setState(CodeEnum.INSERT_ERROR);
        }
    }

    @GetMapping("/goods/{id}")
    @RequiresPermissions({"goods:index"})
    public BaseVO<GoodsDetailVO> getGoodsById(@PathVariable("id") Integer id) {
        try {
            return new BaseVO<GoodsDetailVO>()
                    .setState(CodeEnum.GET_SUCCESS)
                    .setData(goodsService.doGetGoodsById(id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseVO<GoodsDetailVO>().setState(CodeEnum.GET_ERROR);
        }
    }

    @DeleteMapping("/goods/{id}")
    @RequiresPermissions({"goods:delete"})
    public BaseVO<Object> deleteGoods(@PathVariable("id") Integer id) {
        try {
            boolean res = goodsService.doDeleteGoods(id);
            return new BaseVO<>().setState(CodeEnum.DELETE_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseVO<>().setState(CodeEnum.DELETE_ERROR);
        }
    }
}
