package com.yyt.axios.vo;

import com.yyt.axios.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class BaseVO<T> {
    private int code;
    private String message;
    private T data;

    public BaseVO<T> setState(CodeEnum codeEnum) {
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMsg();
        return this;
    }
}
