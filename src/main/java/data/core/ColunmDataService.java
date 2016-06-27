package data.core;

import data.utils.DataUtils;

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
     * @param applicationContext 应用上下文
     * @return
     */
    public Object getColunmData(Column column,ApplicationContext applicationContext){
        if (column==null)
            throw new IllegalArgumentException("column不能为空或null");
        if (DataUtils.isEmptyOrNull(column.getColumnType().name()))
            throw new IllegalArgumentException("字段的产生数据方式不能为空或null");
        if (DataUtils.isEmptyOrNull(column.getGenerator()))
            throw new IllegalArgumentException("字段的生成方式不能为空或null");
        switch (column.getGenerator()){
            case "UUID":
                return UUID.randomUUID().toString();
            case "SEQ":
                StringBuffer key=new StringBuffer(column.getTable().getName()).append(".").append(column.getName());
                return this.getSEQ(key.toString(),Integer.parseInt(column.getMin()),applicationContext);
            case "LABLE":

                break;
            case "RANDOM":
                break;
            case "TEXT":
                String info= (String) applicationContext.getParams().get(XmlDatasFactory.getRootTableByColumn(column).getName());
                return info.substring(0,DataUtils.getRanDom(1,info.length()));
        }
        return  0;
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

    private String getLABLE(Column column){
        String content=column.getContent();

        return null;
    }

}
