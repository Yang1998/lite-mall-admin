package com.yyt.axios.controller.report;

import com.yyt.axios.enums.CodeEnum;
import com.yyt.axios.mapper.ReportMapper;
import com.yyt.axios.service.ReportService;
import com.yyt.axios.vo.AreaReportVO;
import com.yyt.axios.vo.BaseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ReportController {
    @Autowired
    ReportService reportService;

    @GetMapping("/reports/type/{type}")
    public BaseVO<AreaReportVO> getReports(@PathVariable("type") Integer type) {
        try {
            return new BaseVO<AreaReportVO>()
                    .setState(CodeEnum.GET_SUCCESS)
                    .setData(reportService.doGetReportByType(type));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseVO<AreaReportVO>().setState(CodeEnum.GET_ERROR);
        }
    }
}
