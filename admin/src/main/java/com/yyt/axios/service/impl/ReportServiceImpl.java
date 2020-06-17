package com.yyt.axios.service.impl;

import com.yyt.axios.entity.AreaReportPO;
import com.yyt.axios.mapper.ReportMapper;
import com.yyt.axios.service.ReportService;
import com.yyt.axios.vo.AreaReportVO;
import com.yyt.axios.vo.report.LegendVO;
import com.yyt.axios.vo.report.SeriesItemVO;
import com.yyt.axios.vo.report.XAxisDateItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    private static final String seriesItemType = "line";
    /**
     *  根据type查报告数据
     * @param type 需要查询的报告类型
     *             1 -> 地区
     *             2 -> 商品
     * @return AreaReportVO
     */
    @Override
    public AreaReportVO doGetReportByType(Integer type) {
        if(type == null) {
            throw new RuntimeException("type有误, 请输入正确的type");
        }
        if(type == 1) {
            return doGetAreaReport();
        } else {
            throw new RuntimeException("type有误, 请输入正确的type");
        }
    }

    private AreaReportVO doGetAreaReport() {
        List<AreaReportPO> areaReport = reportMapper.getAreaReport();
        List<SeriesItemVO> seriesItems = new ArrayList<>();
        List<Object> xAxisList = new ArrayList<>();
        List<String> areas = areaReport.stream()
                .map(AreaReportPO::getArea)
                .distinct()
                .collect(Collectors.toList());
        List<Date> dateList = areaReport.stream()
                .map(AreaReportPO::getDate)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        xAxisList.add(new XAxisDateItemVO().setData(dateList));
        areas.forEach(area -> {
            List<AreaReportPO> collect = areaReport.stream().filter(item -> item.getArea().equals(area)).sorted(Comparator.comparing(AreaReportPO::getDate)).collect(Collectors.toList());
            // 相等说明一一对应
            List<BigDecimal> subDataList = null;
            if (collect.size() == dateList.size()) {
                subDataList = collect.stream().map(AreaReportPO::getCount).collect(Collectors.toList());
            } else {
                // collect.size < dateList.size 必成立
                subDataList = new ArrayList<>();
                int index = 0;
                for (int i = 0; i < dateList.size(); i++) {
                    if(dateList.get(i).equals(collect.get(index).getDate())) {
                        //相等 则把count加入
                        subDataList.add(collect.get(index).getCount());
                        index++;
                    } else {
                        subDataList.add(new BigDecimal(0));
                    }
                }
            }
            seriesItems.add(new SeriesItemVO().setName(area).setType(seriesItemType).setData(subDataList).setStack("总量"));
        });
        return new AreaReportVO().setLegend(new LegendVO().setData(areas)).setXAxis(xAxisList).setSeries(seriesItems);
    }
}
