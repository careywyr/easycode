package cn.leafw.easycode.controller;

import cn.leafw.easycode.model.Column;
import cn.leafw.easycode.model.DomainMapperTemplate;
import cn.leafw.easycode.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO
 *
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2020/4/22
 */
@RestController
public class GenerateController {
    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/test")
    public void test(){
        databaseService.getColumns("user");
    }

    /**
     * 获取所有表
     */
    @GetMapping("/tables")
    public List<String> getAllTables(){
        return databaseService.getAllTables();
    }

    /**
     * 获取对应表的所有字段
     * @param tableName
     * @return
     */
    @GetMapping("/columns")
    public List<Column> getAllColumn(@RequestParam("tableName") String tableName){
        return databaseService.getColumns(tableName);
    }

    /**
     * 生成实体
     * @param domainMapperTemplate domainMapperTemplate
     */
    @PostMapping("/domain")
    public void generateDomain(@RequestBody DomainMapperTemplate domainMapperTemplate){
        try {
            databaseService.writeDomain(domainMapperTemplate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成mapper.java
     * @param domainMapperTemplate domainMapperTemplate
     */
    @PostMapping("/mapper")
    public void generateMapper(@RequestBody DomainMapperTemplate domainMapperTemplate){
        try {
            databaseService.writeMapper(domainMapperTemplate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

