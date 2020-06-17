package com.yyt.axios.controller.goods;

import com.yyt.axios.enums.CodeEnum;
import com.yyt.axios.service.CategoryService;
import com.yyt.axios.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
@Slf4j
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    @RequiresPermissions({"categories:index"})
    public BaseVO<CatePageVO> getCategories(
            @NotNull(message = "type不可为空") @Range(min = 1, max = 3, message = "type只能是1-3") Integer type,
            Integer pageNum,
            Integer pageSize) {
        try {
            return new BaseVO<CatePageVO>()
                    .setData(categoryService.getAllCategory(type, pageNum, pageSize))
                    .setState(CodeEnum.GET_SUCCESS);
        } catch (Exception e) {
            log.error("获取分类列表失败");
            return new BaseVO<CatePageVO>().setState(CodeEnum.GET_ERROR);
        }
    }

    @PostMapping("/categories")
    @RequiresPermissions({"categories:add"})
    public BaseVO<CateVO> addCategory(@RequestBody CateInfoVO cateInfo) {
        try {
            return new BaseVO<CateVO>()
                    .setData(categoryService.doAddCategory(cateInfo))
                    .setState(CodeEnum.ADD_CATEGORY_SUCCESS);
        } catch (Exception e) {
            if(log.isDebugEnabled()) {
                log.debug(e.getMessage());
            }
            e.printStackTrace();
            return new BaseVO<CateVO>().setState(CodeEnum.ADD_CATEGORY_ERROR);
        }
    }

    @GetMapping("/categories/{id}")
    @RequiresPermissions({"categories:index"})
    public BaseVO<CateVO> getCategory(@PathVariable("id") Integer id) {
        try {
            return new BaseVO<CateVO>()
                    .setData(categoryService.doGetCategory(id))
                    .setState(CodeEnum.GET_SUCCESS);
        } catch (Exception e) {
            log.debug("商品分类不存在id = {}", id);
            e.printStackTrace();
            return new BaseVO<CateVO>().setState(CodeEnum.GET_ERROR);
        }
    }

    @PutMapping("/categories/{id}")
    @RequiresPermissions({"categories:update"})
    public BaseVO<CateVO> updateCategoryInfo(@PathVariable("id") Integer id, @RequestBody CateUpdateVO cateUpdateVO) {
        try {
            return new BaseVO<CateVO>()
                    .setData(categoryService.doUpdateCategory(id, cateUpdateVO))
                    .setState(CodeEnum.UPDATE_SUCCESS);
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return new BaseVO<CateVO>()
                    .setState(CodeEnum.UPDATE_ERROR);
        }
    }

    @DeleteMapping("/categories/{id}")
    @RequiresPermissions({"categories:delete"})
    public BaseVO deleteCategory(@PathVariable("id") Integer id) {
        try {
            categoryService.doDeleteCategory(id);
            return new BaseVO().setState(CodeEnum.DELETE_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new BaseVO().setState(CodeEnum.DELETE_ERROR);
        }
    }

    @GetMapping("/categories/{id}/attributes")
    @RequiresPermissions({"categories:index"})
    public BaseVO<List<AttributeVO>> getAttribute(@PathVariable("id") Integer cid, @RequestParam String sel) {
        try {
            return new BaseVO<List<AttributeVO>>()
                    .setData(categoryService.getAttributesList(cid, sel))
                    .setState(CodeEnum.GET_SUCCESS);
        }catch (Exception e) {
            log.debug(e.getMessage(), e);
            return new BaseVO<List<AttributeVO>>().setState(CodeEnum.GET_ERROR);
        }
    }

    @PostMapping("/categories/{id}/attributes")
    @RequiresPermissions({"categories:add"})
    public BaseVO<AttributeVO> addAttribute(@PathVariable("id") Integer cid,
                                            @RequestBody AddAttributeVO addAttributeVO) {
        try {
            return new BaseVO<AttributeVO>()
                    .setData(categoryService.doAddAttribute(cid, addAttributeVO))
                    .setState(CodeEnum.INSERT_SUCCESS);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return new BaseVO<AttributeVO>().setState(CodeEnum.INSERT_ERROR);
        }
    }

    @DeleteMapping("categories/{id}/attributes/{attrid}")
    @RequiresPermissions({"categories:delete"})
    public BaseVO<Object> deleteAttribute(@PathVariable("id") Integer cid, @PathVariable("attrid") Integer attrId) {
        try {
            boolean res = categoryService.doDeleteAttribute(cid, attrId);
            return new BaseVO<>().setState(CodeEnum.DELETE_SUCCESS);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return new BaseVO<>().setState(CodeEnum.DELETE_ERROR);
        }
    }

    @GetMapping("categories/{id}/attributes/{attrId}")
    @RequiresPermissions({"categories:index"})
    public BaseVO<AttributeVO> getAttributeInfo(@PathVariable("id") Integer cid, @PathVariable("attrId") Integer attrId,
                                                @RequestParam("attr_sel") String sel, @RequestParam(value = "attr_vals", required = false) String vals) {
        try {
            return new BaseVO<AttributeVO>().setData(categoryService.doGetAttributeInfo(cid, attrId, sel, vals))
                    .setState(CodeEnum.GET_SUCCESS);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return new BaseVO<AttributeVO>().setState(CodeEnum.GET_ERROR);
        }
    }

    @PutMapping("categories/{id}/attributes/{attrId}")
    @RequiresPermissions({"categories:update"})
    public BaseVO<AttributeVO> updateAttribute(@PathVariable("id") Integer cid, @PathVariable("attrId") Integer attrId, @RequestBody AddAttributeVO addAttributeVO) {
        try {
            return new BaseVO<AttributeVO>()
                    .setData(categoryService.doUpdateAttribute(cid, attrId, addAttributeVO))
                    .setState(CodeEnum.UPDATE_SUCCESS);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return new BaseVO<AttributeVO>().setState(CodeEnum.UPDATE_ERROR);
        }
    }
}
