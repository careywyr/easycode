package cn.leafw.easycode.service;

import cn.leafw.easycode.model.Column;
import cn.leafw.easycode.model.DomainMapperTemplate;
import cn.leafw.easycode.utils.Database2JavaPropUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作服务
 *
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2020/4/22
 */
@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取所有表名
     * @return  表名列表
     */
    public List<String> getAllTables(){
        List<String> tables = new ArrayList<>();
        List<Map<String, Object>> result = jdbcTemplate.queryForList("show tables ");
        for (Map<String, Object> map : result) {
            for (Map.Entry<String, Object> item : map.entrySet()) {
                tables.add(item.getValue().toString());
            }
        }
        return tables;
    }

    /**
     * 获得所有列
     * @param tableName tableName
     * @return Columns
     */
    public List<Column> getColumns(String tableName){
        List<Column> columns = new ArrayList<>();
        String sql = "show full columns from " + tableName;
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : maps) {
            Column column = new Column();
            String name = map.get("Field").toString();
            column.setDatabaseColumnName(name);
            column.setDatabaseColumnType(map.get("Type").toString());
            Object key = map.get("Key");
            if(null != key && "PRI".equals(key.toString())){
                column.setPrimary(true);
            }
            columns.add(column);
        }
        return columns;
    }

    /**
     * 实体文件内容
     * @param domainMapperTemplate domainTemplate
     * @return content
     */
    private String domainContentGenerator(DomainMapperTemplate domainMapperTemplate){
        StringBuilder content = new StringBuilder();
        // 第一行一定是包, 空两行好看点
        content.append("package ").append(domainMapperTemplate.getPackageName()).append("\r\n").append("\r\n");

        // 后面是一大坨包的引入, @id, @data, @table, @generateValue是一定有的
        StringBuilder importContent = new StringBuilder();
        String mustImport = "import lombok.Data; \r\n"
                + "import javax.persistence.Id;\r\n"
                + "import javax.persistence.Table;\r\n"
                + "import javax.persistence.GeneratedValue;\r\n";
        importContent.append(mustImport);

        // 类注释
        String classCommentTemplate = "/**\n" +
                " * <p>\r\n" +
                " * %s \r\n" +
                " * </p>\r\n" +
                " *\r\n" +
                " * @author %s \r\n" +
                " * @date %s \r\n" +
                " */" +
                "\r\n";
        String classComment = String.format(classCommentTemplate, domainMapperTemplate.getTableDesc(), domainMapperTemplate.getAuthor(), domainMapperTemplate.getCreateTime());

        // 类注解
        String classAnnotationTemplate = "@Table(name=\"{table_name}\")\r\n" +
                "@Data\r\n";
        String classAnnotation = String.format(classAnnotationTemplate, domainMapperTemplate.getTableName());

        //public class 类名 {
        String classFirstStatement = "public class " + Database2JavaPropUtil.tableName2JavaName(domainMapperTemplate.getTableName()) + " { \r\n";

        // 所有的column
        StringBuilder columnsContent = new StringBuilder();
        for (Column column : domainMapperTemplate.getColumns()) {
            StringBuilder columnContent = new StringBuilder();
            String type = Database2JavaPropUtil.columnTransfer(column.getDatabaseColumnType());
            String javaName = Database2JavaPropUtil.databaseColumnName2JavaName(column.getDatabaseColumnName());
            // 时间类型要处理是localDate还是localDateTime
            if("date".equals(type)){
                type = Database2JavaPropUtil.dateAttrTransfer(column.getTimeDate());
            }
            if(column.getPrimary()){
                columnContent.append( "   @Id\r\n" + "   @GeneratedValue(generator = \"JDBC\")\r\n");
            }
            columnContent.append("   /** ").append(column.getComment()).append("*/\r\n");
            columnContent.append("   private ").append(type).append(" ").append(javaName).append("\r\n");
            columnsContent.append(columnContent);
        }
        // 判断需要引入哪些包
        if(columnsContent.toString().contains("private LocalDateTime ")){
            importContent.append("import java.time.LocalDateTime;\r\n");
        }
        if(columnsContent.toString().contains("private LocalDate ")){
            importContent.append("import java.time.LocalDate;\r\n");
        }
        if(columnsContent.toString().contains("private BigDecimal ")){
            importContent.append("import java.math.BigDecimal;\r\n");
        }

        // 包 + 引入 + 类注释 + 类注解 + 类名 + columns
        content.append(importContent).append(classComment).append(classAnnotation).append(classFirstStatement).append(columnsContent);

        // 结束
        content.append("}");

        return content.toString();
    }

    /**
     * 写DO文件
     * @param domainMapperTemplate domainMapperTemplate
     */
    public void writeDomain(DomainMapperTemplate domainMapperTemplate) throws Exception{
        File domainFile = new File("/Users/carey/Desktop/test/test.java");
        if(!domainFile.exists()){
            domainFile.createNewFile();
        }
        FileWriter fw = new FileWriter(domainFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        String content = domainContentGenerator(domainMapperTemplate);
        bw.write(content);
        bw.close();
    }

    private String mapperContentGenerator(DomainMapperTemplate domainMapperTemplate){
        StringBuilder content = new StringBuilder();
        // 第一行一定是包, 空两行好看点
        content.append("package ").append(domainMapperTemplate.getPackageName()).append("\r\n").append("\r\n");
        // 包一定会有BaseMapper和实体的包位置
        content.append("import tk.mybatis.mapper.common.BaseMapper;\r\n" + "import ").append(domainMapperTemplate.getDomainPackagePosition()).append("\r\n");

        // 类注释
        String classCommentTemplate ="/**\r\n" +
                " *\r\n" +
                " * @author {author}\r\n" +
                " * @date {now}\r\n" +
                " */\r\n";
        String classComment = String.format(classCommentTemplate, domainMapperTemplate.getAuthor(), domainMapperTemplate.getCreateTime());
        content.append(classComment);

        // public interface ...
        String classFirstStatement = "public interface " + Database2JavaPropUtil.tableName2JavaName(domainMapperTemplate.getTableName()) + "Mapper extends BaseMapper<"
                + Database2JavaPropUtil.tableName2JavaName(domainMapperTemplate.getTableName()) + "> {\r\n";
        content.append(classFirstStatement);
        content.append("}");
        return content.toString();
    }

    /**
     * 写mapper文件
     * @param domainMapperTemplate domainMapperTemplate
     */
    public void writeMapper(DomainMapperTemplate domainMapperTemplate) throws Exception{
        File domainFile = new File("/Users/carey/Desktop/test/mapper.java");
        if(!domainFile.exists()){
            domainFile.createNewFile();
        }
        FileWriter fw = new FileWriter(domainFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        String content = mapperContentGenerator(domainMapperTemplate);
        bw.write(content);
        bw.close();
    }

    public void writeService(){

    }

}

