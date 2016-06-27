package data.core;

import data.utils.Params;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用上下文
 * Created by wy on 2016/6/27.
 */
public class ApplicationContext {

    private Map<String,Object> params;

    private List<Table> tableList;

    public ApplicationContext(){
        //初始化数据库连接参数
        Params.initParam();
        //初始化全局参数
        params=new HashMap<String, Object>();
        //初始化解析XML
        try {
            tableList=XmlDatasFactory.getAllTableList("templateConfig.xml");
            for (Table table:tableList){
                StringBuffer info=new StringBuffer();
                String s="";
                File file=new File(table.getTextFile());
                BufferedReader reader=new BufferedReader(new FileReader(file));
                while ((s = reader.readLine()) != null) {
                    info.append(s);
                }
                params.put(table.getName(),info.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public List<Table> getTableList() {
        return tableList;
    }

    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }
}
