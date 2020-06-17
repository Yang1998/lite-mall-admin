package com.yyt.axios.mapper;

import com.yyt.axios.entity.AttributePO;
import com.yyt.axios.vo.AddAttributeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AttributeMapper {

    int addAttribute(@Param("cid") Integer cid, @Param("attr") AddAttributeVO addAttributeVO);

    AttributePO getAttributeById(@Param("id") int attr_id);

    List<AttributePO> getAttributesByCidSel(@Param("cid") Integer cid, @Param("sel") String sel);

    int deleteAttribute(@Param("cid") Integer cid, @Param("attrId") Integer attrId, @Param("deleteTime") long deleteTime);

    int updateAttribute(@Param("cid") Integer cid, @Param("attrId") Integer attrId, @Param("attr") AddAttributeVO addAttributeVO);


}
