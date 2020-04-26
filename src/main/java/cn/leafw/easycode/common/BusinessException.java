package cn.leafw.easycode.common;

/**
 * 全局异常
 *
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2020/4/26
 */
public class BusinessException extends RuntimeException{
    private BusinessException(String message){
        super(message);
    }

    public static BusinessException of(String msg){
        return new BusinessException(msg);
    }
}

