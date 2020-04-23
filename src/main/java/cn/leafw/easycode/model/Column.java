package cn.leafw.easycode.model;

import lombok.Data;

/**
 * 表字段
 *
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2020/4/22
 */
@Data
public class Column {

    private String databaseColumnName;

    private String javaName;

    private String databaseColumnType;

    private String javaType;

    private String comment;

    private Boolean primary = false;

    /** 1日期，2时间 时间类型必传*/
    private Integer timeDate;

    /** 是否审计字段*/
    private Boolean isCommon = false;

    /** 生成该字段值所需要的方法*/
    private String generateMethod;

    /** 生成字段值需要的方法所依赖的包地址*/
    private String generateMethodDependency;
}

