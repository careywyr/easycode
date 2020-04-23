package cn.leafw.easycode.utils;

import com.google.common.base.CaseFormat;

/**
 * TODO
 *
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2020/4/23
 */
public class Database2JavaPropUtil {

    private static final Integer DATE = 1;
    private static final Integer TIME = 2;

    /**
     * <p>数据库属性转java属性</p>
     * @param databaseAttr
     * @return
     */
    public static String columnTransfer(String databaseAttr){
        if(databaseAttr.contains("varchar")){
            return "String";
        }
        if(databaseAttr.contains("int")){
            return "Integer";
        }
        // 时间特殊要判断返回LocalDate还是LocalDateTime
        if(databaseAttr.contains("date")){
            return "date";
        }
        if(databaseAttr.contains("decimal")){
            return "BigDecimal";
        }
        return null;
    }

    /**
     * 时间类型的数据库字段属性转化
     * @param type time/date
     * @return javaType
     */
    public static String dateAttrTransfer(Integer type){
        if(type.equals(TIME)){
            return "LocalDateTime";
        }
        return "LocalDate";
    }

    public static String tableName2JavaName(String tableName){
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
    }

    /**
     * 下划线变量转驼峰
     * @param databaseName
     * @return
     */
    public static String databaseColumnName2JavaName(String databaseName){
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, databaseName);
    }
}

