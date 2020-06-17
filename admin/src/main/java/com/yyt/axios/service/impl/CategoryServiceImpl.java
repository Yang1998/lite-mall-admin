package com.yyt.axios.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yyt.axios.entity.AttributePO;
import com.yyt.axios.entity.CategoryPO;
import com.yyt.axios.enums.CategoryLevelEnum;
import com.yyt.axios.mapper.AttributeMapper;
import com.yyt.axios.mapper.CategoryMapper;
import com.yyt.axios.service.CategoryService;
import com.yyt.axios.util.TimeUtil;
import com.yyt.axios.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private AttributeMapper attributeMapper;

    /**
     * @param type     分为1, 2, 3 个等级， 1 代表 只显示一层， 2表示显示1, 2层， 3表示显示1，2, 3层
     *                 不可为空
     * @param pageNum  页码 可以为空， 为空获取所有
     * @param pageSize 页的大小, 可以为空， 为空获取所有
     * @return 所有的分类列表
     */
    @Override
    public CatePageVO getAllCategory(Integer type, Integer pageNum, Integer pageSize) {
        List<CategoryPO> categories = categoryMapper.getAllCategories();
        final List<CateVO> total = new ArrayList<>();
        categories.forEach(category -> total.add(transformCatePO2CateVO(category)));
        if (pageNum == null || pageSize == null) {
            return new CatePageVO().setResult(getCatesByType(total, type, null));
        } else {
            PageHelper.startPage(pageNum, pageSize);
            List<CategoryPO> parentPO = categoryMapper.getCategoryByLevel(CategoryLevelEnum.ONE.getLevel());
            PageInfo<CategoryPO> pageInfo = new PageInfo<>(parentPO);
            List<CateVO> parent = new ArrayList<>();
            parentPO.forEach(po -> parent.add(transformCatePO2CateVO(po)));
            List<CateVO> ans = getCatesByType(total, type, parent);
            return new CatePageVO()
                    .setResult(ans)
                    .setTotal(pageInfo.getTotal())
                    .setPageNum(pageInfo.getPageNum())
                    .setPageSize(pageInfo.getPageSize());
        }
    }

    @Transactional
    @Override
    public CateVO doAddCategory(CateInfoVO cateInfo) {
        int cnt = categoryMapper.addCategory(cateInfo);
        if(cnt != 1) {
            throw new RuntimeException(String.format("插入失败 pid = %d, name = %s, level = %d",
                    cateInfo.getCat_pid(), cateInfo.getCat_name(), cateInfo.getCat_level()));
        }
        CategoryPO categoryFromDb = categoryMapper.getCategoryById(cateInfo.getId());
        return transformCatePO2CateVO(categoryFromDb);
    }

    @Override
    public CateVO doGetCategory(int id) {
        return transformCatePO2CateVO(categoryMapper.getCategoryById(id));
    }

    @Transactional
    @Override
    public CateVO doUpdateCategory(Integer id, CateUpdateVO cateUpdateVO) {
        int cnt = categoryMapper.updateCategory(id, cateUpdateVO.getCat_name());
        if(cnt != 1) {
            throw new RuntimeException(String.format("商品分类不存在 id = %d", id));
        }
        return transformCatePO2CateVO(categoryMapper.getCategoryById(id));
    }

    @Transactional
    @Override
    public boolean doDeleteCategory(Integer id) {
        int cnt = categoryMapper.deleteCategory(id);
        if(cnt != 1) {
            throw new RuntimeException(String.format("商品分类不存在 id = %d", id));
        }
        return true;
    }

    @Transactional
    @Override
    public AttributeVO doAddAttribute(Integer cid, AddAttributeVO addAttributeVO) {
        int cnt = attributeMapper.addAttribute(cid, addAttributeVO);
        if(cnt != 1) {
            throw new RuntimeException(String.format("插入分类参数失败 cid = %d, attr_name = %s, " +
                    "attr_sel = %s attr_vals = %s", cid, addAttributeVO.getAttr_name(),
                    addAttributeVO.getAttr_sel(), addAttributeVO.getAttr_vals()));
        }
        return transformAttrPO2AttrVO(attributeMapper.getAttributeById(addAttributeVO.getAttr_id()));
    }

    @Override
    public List<AttributeVO> getAttributesList(Integer cid, String sel) {
        List<AttributePO> attributeFromDb = attributeMapper.getAttributesByCidSel(cid, sel);
        return transformAttrPOs2AttrVos(attributeFromDb);
    }

    @Transactional
    @Override
    public boolean doDeleteAttribute(Integer cid, Integer attrId) {
        int cnt = attributeMapper.deleteAttribute(cid, attrId, TimeUtil.getNow());
        if(cnt != 1) {
            throw new RuntimeException(String.format("删除分类参数失败 cid = %d, attrId = %d", cid, attrId));
        }
        return true;
    }

    @Override
    public AttributeVO doGetAttributeInfo(Integer cid, Integer attrId, String sel, String vals) {
        AttributePO attribute = attributeMapper.getAttributeById(attrId);
        if(attribute.getCat_id() != cid || !sel.equals(attribute.getAttr_sel()) || vals != null && !vals.equals(attribute.getAttr_vals())) {
            throw new RuntimeException("输入参数有误");
        }
        return transformAttrPO2AttrVO(attribute);
    }

    @Transactional
    @Override
    public AttributeVO doUpdateAttribute(Integer cid, Integer attrId, AddAttributeVO addAttributeVO) {
        int cnt = attributeMapper.updateAttribute(cid, attrId, addAttributeVO);
        if(cnt != 1) {
            throw new RuntimeException(String.format("更新分类参数失败 cid = %d, attrId = %d", cid, attrId));
        }
        return transformAttrPO2AttrVO(attributeMapper.getAttributeById(attrId));
    }

    /**
     * 将categoryPO 转换为 CateVO
     *
     * @param categoryPO categoryPO
     * @return CateVO
     */
    private CateVO transformCatePO2CateVO(CategoryPO categoryPO) {
        return new CateVO()
                .setCat_id(categoryPO.getCat_id())
                // 1 为删除 0 为未删除
                .setCat_deleted(categoryPO.getCat_deleted() == 1)
                .setCat_level(categoryPO.getCat_level())
                .setCat_name(categoryPO.getCat_name())
                .setCat_pid(categoryPO.getCat_pid());
    }

    private List<CateVO> getCatesByType(List<CateVO> categories, int type, List<CateVO> parentCates) {
        if (categories == null) {
            return null;
        }
        List<CateVO> res = parentCates == null ? getValidCates(1, 0, categories) : parentCates;
        if (type == CategoryLevelEnum.ONE.getTypeByLevel()) {
            return res;
        } else if (type == CategoryLevelEnum.TWO.getTypeByLevel()) {
            res.forEach(parent -> {
                parent.setChildren(getValidCates(2, parent.getCat_id(), categories));
            });
            return res;
        } else if (type == CategoryLevelEnum.THREE.getTypeByLevel()) {
            res.forEach(oneLevel -> {
                List<CateVO> twoLevels = getValidCates(2, oneLevel.getCat_id(), categories);
                twoLevels.forEach(twoLevel -> {
                    twoLevel.setChildren(getValidCates(3, twoLevel.getCat_id(), categories));
                });
                oneLevel.setChildren(twoLevels);
            });
            return res;
        } else {
            return res;
        }
    }


    private List<CateVO> getValidCates(int type, int pid, List<CateVO> categories) {
        // 注意这里type要减一
        return categories.stream()
                .filter(category -> category.getCat_level() == type - 1 && category.getCat_pid() == pid)
                .collect(Collectors.toList());
    }

    /**
     * 将AttributePO转换为AttributeVO
     * @param attribute AttributePO
     * @return AttributeVO
     */
    private AttributeVO transformAttrPO2AttrVO(AttributePO attribute) {
        return new AttributeVO()
                .setAttr_id(attribute.getAttr_id())
                .setAttr_name(attribute.getAttr_name())
                .setAttr_sel(attribute.getAttr_sel())
                .setAttr_vals(attribute.getAttr_vals())
                .setAttr_write(attribute.getAttr_write())
                .setCat_id(attribute.getCat_id());
    }

    private List<AttributeVO> transformAttrPOs2AttrVos(List<AttributePO> attributes) {
        List<AttributeVO> res = new ArrayList<>();
        attributes.forEach(attribute -> res.add(transformAttrPO2AttrVO(attribute)));
        return res;
    }

}
