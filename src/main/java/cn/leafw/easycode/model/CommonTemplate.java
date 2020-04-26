package cn.leafw.easycode.model;

import lombok.Data;

/**
 * 公共需要的参数
 *
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2020/4/22
 */
@Data
public class CommonTemplate {

    /** 包名*/
    private String packageName;
    /** 作者*/
    private String author;
    /** 创建时间*/
    private String createTime;
}

