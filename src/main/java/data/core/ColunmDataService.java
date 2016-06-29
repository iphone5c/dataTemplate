package data.core;

import data.utils.DataUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 字段数据业务
 * Created by wy on 2016/6/27.
 */
public class ColunmDataService {

    /**
     * 获取字段值
     * @param column 字段对象
     * @param record 数据记录生成存放
     * @param applicationContext 应用上下文
     * @return
     */
    public Map<String,Object> getColunmData(Column column,Map<String,Object> record,ApplicationContext applicationContext){
        if (column==null)
            throw new IllegalArgumentException("column不能为空或null");
        if (DataUtils.isEmptyOrNull(column.getColumnType().name()))
            throw new IllegalArgumentException("字段的产生数据方式不能为空或null");
        if (DataUtils.isEmptyOrNull(column.getGenerator()))
            throw new IllegalArgumentException("字段的生成方式不能为空或null");
        if (record.get(column.getName())==null){
            switch (column.getGenerator()){
                case "UUID":
                    record.put(column.getName(),UUID.randomUUID().toString());
                case "SEQ":
                    StringBuffer key=new StringBuffer(column.getTable().getName()).append(".").append(column.getName());
                    record.put(column.getName(),this.getSEQ(key.toString(),Integer.parseInt(column.getMin()),applicationContext));
                case "LABLE":
                    String content = column.getContent();
                    String[] colunmNames=DataUtils.getString(content,"(\\[[^\\]]+\\])");
                    if (colunmNames!=null){
                        for (String name:colunmNames){
                            String keyName=name.substring(1,name.length()-1);
                            this.getColunmData(XmlDatasFactory.getColumnByName(keyName,column.getTable()),record,applicationContext);
                            content=content.replaceAll("\\["+keyName+"\\]", (String) record.get(keyName));
                        }
                    }
                    colunmNames=DataUtils.getString(content,"(\\{[^\\}]+\\})");
                    if (colunmNames!=null){
                        for (String name:colunmNames){
                            //TODO 处理SEQ这类型的
                            String keyName=name.substring(1,name.length()-1);
                        }
                    }
                    break;
                case "RANDOM":
                    break;
                case "TEXT":
                    String info= (String) applicationContext.getParams().get(XmlDatasFactory.getRootTableByColumn(column).getName());
                    record.put(column.getName(),info.substring(0,DataUtils.getRanDom(1,info.length())));
            }
        }
        return record;
    }

    /**
     * 获取SEQ序列
     * @param key
     * @param min
     * @param applicationContext
     * @return
     */
    private Integer getSEQ(String key,Integer min,ApplicationContext applicationContext){
        Map<String,Object> map=applicationContext.getParams();
        Object obj=map.get(key);
        if (obj==null){
            applicationContext.getParams().put(key,min+1);
            return min;
        }else {
            Integer data= (Integer) obj;
            applicationContext.getParams().put(key,data+1);
            return data;
        }
    }

    public Map<String,Object> getRecord(List<Column> columnList,ApplicationContext applicationContext){
        Map<String,Object> record=null;
        if (columnList==null)
            throw new IllegalArgumentException("字段集合不能为空或null");
        for (Column column:columnList){
            record=new HashMap<>();
            record.put(column.getName(),this.getColunmData(column,record,applicationContext));
        }
        return record;
    }

}
