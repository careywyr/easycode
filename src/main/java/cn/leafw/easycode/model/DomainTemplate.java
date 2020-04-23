package cn.leafw.easycode.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 实体模版参数
 *
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2020/4/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DomainTemplate extends CommonTemplate{

    /** 包名*/
    private String packageName;
    /** 表名*/
    private String tableName;
    /** 表注释*/
    private String tableDesc;

    private List<Column> columns;

}

