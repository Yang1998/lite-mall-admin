package com.yyt.axios.mapper;

import com.yyt.axios.vo.AddGoodsAttrVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsAttrMapper {
    int addGoodsAttr(@Param("gid") Integer id, @Param("attrs") List<AddGoodsAttrVO> attrs);

}
