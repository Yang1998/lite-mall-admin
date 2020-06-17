package com.yyt.axios.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yyt.axios.entity.*;
import com.yyt.axios.mapper.GoodsAttrMapper;
import com.yyt.axios.mapper.GoodsMapper;
import com.yyt.axios.mapper.GoodsPicMapper;
import com.yyt.axios.service.GoodsService;
import com.yyt.axios.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    GoodsPicMapper goodsPicMapper;

    @Autowired
    GoodsAttrMapper goodsAttrMapper;
    @Override
    public GoodsPageVO doGetGoodsList(String query, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsPO> goodsFromDb = goodsMapper.getAllGoods(query);
        PageInfo<GoodsPO> pageInfo = new PageInfo<>(goodsFromDb);
        return new GoodsPageVO()
                .setTotal(pageInfo.getTotal())
                .setPageNum(pageInfo.getPageNum())
                .setGoods(transformGoodsPOs2GoodsListVOs(goodsFromDb));
    }

    @Override
    @Transactional
    public boolean doAddGoods(GoodsAddVO goodsAddVO) {
        String[] ids = goodsAddVO.getGoods_cat().split(",");
        Integer one = Integer.parseInt(ids[0]);
        Integer two = Integer.parseInt(ids[1]);
        Integer three = Integer.parseInt(ids[2]);
        int cnt1 = goodsMapper.addGoods(goodsAddVO, one, two ,three);
        if(cnt1 != 1) {
            throw new RuntimeException("插入商品失败");
        }
        System.out.println(goodsAddVO.getAttrs());
        System.out.println(goodsAddVO.getPics());
        int cnt2 = goodsPicMapper.addGoodsPic(goodsAddVO.getId(), goodsAddVO.getPics());
        if(cnt2 == 0) {
            throw new RuntimeException("插入商品图片失败");
        }
        int cnt3 = goodsAttrMapper.addGoodsAttr(goodsAddVO.getId(), goodsAddVO.getAttrs());
        if(cnt3 == 0) {
            throw new RuntimeException("插入商品分类数据失败");
        }
        return true;
    }

    @Override
    public GoodsDetailVO doGetGoodsById(Integer id) {
        List<GoodsDetailPO> goodsDetail = goodsMapper.getGoodsDetail(id);
        Set<GoodsPO> goodsSet = goodsDetail.stream().map(GoodsDetailPO::getGoods).collect(Collectors.toSet());
        if(goodsSet.size() != 1) {
            throw new RuntimeException(String.format("根据商品id获取的商品数据有误， goodsSet大小应为1，而查询得到的是%d", goodsSet.size()));
        }
        List<GoodsPO> goodsPOList = new ArrayList<>(goodsSet);
        List<GoodsPicsPO> goodsPics = new ArrayList<>(goodsDetail.stream().map(GoodsDetailPO::getGoodsPics).collect(Collectors.toSet()));
        List<GoodsAttr> goodsAttrs = new ArrayList<>(goodsDetail.stream().map(GoodsDetailPO::getGoodsAttrs).collect(Collectors.toSet()));
        return transform2GoodsDetailVO(goodsPOList.get(0), goodsPics, goodsAttrs);
    }

    @Transactional
    @Override
    public boolean doDeleteGoods(Integer id) {
        int cnt = goodsMapper.deleteGoodsById(id);
        if(cnt != 1) {
            throw new RuntimeException(String.format("删除商品失败, id=[%d]", id));
        }
        return true;
    }

    private GoodsListVO transformGoodsPO2GoodsListVO(GoodsPO goodsPO) {
        if(goodsPO == null) return null;
        return new GoodsListVO()
                .setAdd_time(goodsPO.getAdd_time())
                .setGoods_id(goodsPO.getGoods_id())
                .setGoods_name(goodsPO.getGoods_name())
                .setGoods_price(goodsPO.getGoods_price())
                .setGoods_state(goodsPO.getGoods_state())
                .setGoods_weight(goodsPO.getGoods_weight())
                .setHot_number(goodsPO.getHot_number())
                .setIs_promote(goodsPO.getIs_promote() == 1)
                .setUpd_time(goodsPO.getUpd_time());
    }

    private List<GoodsListVO>  transformGoodsPOs2GoodsListVOs(List<GoodsPO> goods) {
        List<GoodsListVO> res = new ArrayList<>();
        if(goods == null || goods.isEmpty()) {
            return res;
        }
        goods.forEach(goodsPO -> res.add(transformGoodsPO2GoodsListVO(goodsPO)));
        return res;
    }

    private List<GoodsPicsVO> transformGoodsPicsPO2GoodsPicsVO(List<GoodsPicsPO> goodsPics) {
        if(goodsPics == null){
            return null;
        }
        return goodsPics.stream().map(item -> new GoodsPicsVO()
                .setGoods_id(item.getGoods_id())
                .setPics_big(item.getPics_big())
                .setPics_id(item.getPics_id())
                .setPics_mid(item.getPics_mid())
                .setPics_sma(item.getPics_sma()))
                .collect(Collectors.toList());
    }

    private GoodsDetailVO transform2GoodsDetailVO(GoodsPO goodsPO, List<GoodsPicsPO> goodsPics, List<GoodsAttr> goodsAttrs) {
        return new GoodsDetailVO()
                .setPics(transformGoodsPicsPO2GoodsPicsVO(goodsPics))
                .setAttrs(goodsAttrs)
                .setGoods_id(goodsPO.getGoods_id())
                .setGoods_name(goodsPO.getGoods_name())
                .setGoods_price(goodsPO.getGoods_price())
                .setGoods_number(goodsPO.getGoods_number())
                .setGoods_weight(goodsPO.getGoods_weight())
                .setGoods_state(goodsPO.getGoods_state())
                .setAdd_time(goodsPO.getAdd_time())
                .setUpd_time(goodsPO.getUpd_time())
                .setHot_number(goodsPO.getHot_number())
                .setIs_promote(goodsPO.getIs_promote() == 1);
    }
}
