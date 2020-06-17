package com.yyt.axios.mapper;

import com.yyt.axios.vo.PicUrlVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsPicMapper {

    int addGoodsPic(@Param("gid") int id, @Param("pics") List<PicUrlVO> pics);
}
