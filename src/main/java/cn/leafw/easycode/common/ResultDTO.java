package cn.leafw.easycode.common;

import lombok.Data;

/**
 * 请求返回包装
 *
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2019/12/16
 */
@Data
public class ResultDTO<T> {

    private Boolean success = true;

    private Integer code;

    private String errMsg;

    private T data;

    public ResultDTO(Boolean success, String errMsg) {
        this.success = success;
        this.errMsg = errMsg;
    }

    public ResultDTO(T data){
        this.success = true;
        this.data = data;
        this.code = 200;
    }

}

