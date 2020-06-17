package com.yyt.axios.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yyt.axios.vo.report.LegendVO;
import com.yyt.axios.vo.report.SeriesItemVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class AreaReportVO {
    private LegendVO legend;
    @JsonProperty("xAxis")
    private List<Object> XAxis;
    private List<SeriesItemVO> series;

}
