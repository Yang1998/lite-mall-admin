package com.yyt.axios.vo;

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
public class CatePageVO {
    private long total;
    private int pageNum;
    private int pageSize;
    private List<CateVO> result;
}
