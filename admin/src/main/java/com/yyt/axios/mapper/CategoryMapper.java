package com.yyt.axios.mapper;

import com.yyt.axios.entity.CategoryPO;
import com.yyt.axios.enums.CategoryLevelEnum;
import com.yyt.axios.vo.AddAttributeVO;
import com.yyt.axios.vo.CateInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    List<CategoryPO> getAllCategories();

    List<CategoryPO> getCategoryByLevel(@Param("level") int level);

    int addCategory(@Param("cate") CateInfoVO cateInfo);

    CategoryPO getCategoryById(@Param("id") int id);

    int updateCategory(@Param("id") Integer id, @Param("name") String cat_name);

    int deleteCategory(@Param("id") Integer id);

}
