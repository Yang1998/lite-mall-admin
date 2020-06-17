package com.yyt.axios.service;

import com.yyt.axios.vo.*;

import java.util.List;

public interface CategoryService {
    CatePageVO getAllCategory(Integer type, Integer pageNum, Integer pageSize);

    CateVO doAddCategory(CateInfoVO cateInfo);

    CateVO doGetCategory(int id);

    CateVO doUpdateCategory(Integer id, CateUpdateVO cateUpdateVO);

    boolean doDeleteCategory(Integer id);

    AttributeVO doAddAttribute(Integer cid, AddAttributeVO addAttributeVO);

    List<AttributeVO> getAttributesList(Integer cid, String sel);

    boolean doDeleteAttribute(Integer cid, Integer attrId);

    AttributeVO doGetAttributeInfo(Integer cid, Integer attrId, String sel, String vals);

    AttributeVO doUpdateAttribute(Integer cid, Integer attrId, AddAttributeVO addAttributeVO);
}
