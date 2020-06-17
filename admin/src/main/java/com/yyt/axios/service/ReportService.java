package com.yyt.axios.service;

import com.yyt.axios.vo.AreaReportVO;

public interface ReportService {
    AreaReportVO doGetReportByType(Integer type);
}
