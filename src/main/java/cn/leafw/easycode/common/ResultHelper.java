package cn.leafw.easycode.common;

/**
 * ResultHelper
 *
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2019/12/17
 */
public class ResultHelper<T> {

    public static ResultDTO returnFalse(String msg){
        return new ResultDTO(false, msg);
    }

    public static <T> ResultDTO<T> returnOk(T data){
        return new ResultDTO<T>(data);
    }

}

