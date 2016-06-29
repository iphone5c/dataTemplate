package data.core;

import data.utils.ColumnType;
import data.utils.DataUtils;

import java.util.*;

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
    public Map<String,Object> getColunmData(Column column,Map<String,Object> record,Object fkValue,ApplicationContext applicationContext){
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
                    record.put(column.getName(),this.getSEQ(key.toString(),Integer.parseInt(column.getMin()),true,applicationContext));
                case "LABLE":
                    String content = column.getContent();
                    String[] colunmNames=DataUtils.getString(content,"(\\[[^\\]]+\\])");
                    if (colunmNames!=null){
                        for (String name:colunmNames){
                            String keyName=name.substring(1,name.length()-1);
                            this.getColunmData(XmlDatasFactory.getColumnByName(keyName,column.getTable()),record,null,applicationContext);
                            content=content.replaceAll("\\["+keyName+"\\]", (String) record.get(keyName));
                        }
                    }
                    colunmNames=DataUtils.getString(content,"(\\{[^\\}]+\\})");
                    if (colunmNames!=null){
                        for (String name:colunmNames){
                            String keyName=name.substring(1,name.length()-1);
                            switch (keyName){
                                case "UUID":
                                    content=content.replaceAll("\\{"+keyName+"\\}",UUID.randomUUID().toString());
                                    break;
                                case "SEQ":
                                    content=content.replaceAll("\\{"+keyName+"\\}",this.getSEQ(keyName,Integer.parseInt(column.getMin()),false,applicationContext).toString());
                                    break;
                                case "TEXT":
                                    String info= (String) applicationContext.getParams().get(XmlDatasFactory.getRootTableByColumn(column).getName());
                                    content=content.replaceAll("\\{"+keyName+"\\}",info.substring(0,DataUtils.getRanDom(1,info.length())));
                                    break;
                                default:
                                    content=content.replaceAll("\\{"+keyName+"\\}",this.getQZ(keyName,column.getTable().getName()+"."+column.getName()+"."+name,applicationContext));
                            }
                        }
                    }
                    record.put(column.getName(),content);
                    break;
                case "RANDOM":
                    if(column.getColumnType()==ColumnType.DATE){
                        Date start=null;
                        Date end=null;
                        String[] startDate=DataUtils.getString(column.getMin(),"(\\[[^\\]]+\\])");
                        String[] endDate=DataUtils.getString(column.getMax(),"(\\[[^\\]]+\\])");
                        if (startDate==null){
                            start=DataUtils.toDate(column.getMin(),DataUtils.DATEFORMAT_DATA_EN_LONG);
                        }else {
                            String keyName=startDate[0].substring(1,startDate[0].length()-1);
                            this.getColunmData(XmlDatasFactory.getColumnByName(keyName,column.getTable()),record,null,applicationContext);
                            start= (Date) record.get(keyName);
                        }

                        if (endDate==null){
                            end=DataUtils.toDate(column.getMin(),DataUtils.DATEFORMAT_DATA_EN_LONG);
                        }else {
                            String keyName=endDate[0].substring(1,endDate[0].length()-1);
                            this.getColunmData(XmlDatasFactory.getColumnByName(keyName,column.getTable()),record,null,applicationContext);
                            end= (Date) record.get(keyName);
                        }
                        record.put(column.getName(),DataUtils.getRanDomDate(start,end));
                    }else if(column.getColumnType()==ColumnType.NUM){
                        record.put(column.getName(),DataUtils.getRanDom(Integer.parseInt(column.getMin()),Integer.parseInt(column.getMax())));
                    }else if(column.getColumnType()==ColumnType.ORGAN){
                        //TODO 获取级联表数据
                    }
                    break;
                case "TEXT":
                    String info= (String) applicationContext.getParams().get(XmlDatasFactory.getRootTableByColumn(column).getName());
                    record.put(column.getName(),info.substring(0,DataUtils.getRanDom(1,info.length())));
                    break;
                default:
                    if (DataUtils.isEmptyOrNull(column.getGenerator())){
                        if (column.getColumnType()==ColumnType.FK){
                            record.put(column.getName(),fkValue);
                        }
                    }else {
                        String temp=column.getGenerator().substring(1,column.getGenerator().length()-1);
                        record.put(column.getName(),this.getQZ(temp,column.getTable().getName()+"."+column.getName()+"."+column.getGenerator(),applicationContext));
                    }
            }
        }
        return record;
    }

    /**
     * 获取SEQ序列
     * @param key
     * @param min
     * @param isAuto 是否自增
     * @param applicationContext
     * @return
     */
    private Integer getSEQ(String key,Integer min,boolean isAuto,ApplicationContext applicationContext){
        Map<String,Object> map=applicationContext.getParams();
        Object obj=map.get(key);
        if (obj==null){
            applicationContext.getParams().put(key,min+1);
            return min;
        }else {
            Integer data= (Integer) obj;
            if(isAuto)
                applicationContext.getParams().put(key,data+1);
            return data;
        }
    }

    /**
     * 根据权重获取随机值
     * @param content
     * @param key
     * @param applicationContext
     * @return
     */
    private String getQZ(String content,String key,ApplicationContext applicationContext){
        Map<String,Object> map=applicationContext.getParams();
        Object obj=map.get(key);
        if (obj==null){
            String qzInfo=content.substring(1,content.length()-1);
            List<String> datas=new ArrayList<>();
            for (String a1:qzInfo.split(",")){
                String[] a2=a1.split(":");
                for (int i=0;i<Integer.parseInt(a2[1]);i++){
                    datas.add(a2[0]);
                }
            }
            if (datas.size()>0)
                applicationContext.getParams().put(key,datas);
            return datas.get(DataUtils.getRanDom(0,datas.size()));
        }else {
            List<String> list= (List<String>) obj;
            return list.get(DataUtils.getRanDom(0,list.size()));
        }
    }

    /**
     * 获取一条记录
     * @param columnList
     * @param applicationContext
     * @return
     */
    public Map<String,Object> getRecord(List<Column> columnList,Object fkValue,ApplicationContext applicationContext){
        Map<String,Object> record=new HashMap<>();
        if (columnList==null)
            throw new IllegalArgumentException("字段集合不能为空或null");
        for (Column column:columnList){
            this.getColunmData(column,record,fkValue,applicationContext);
            if (column.getColumnType()==ColumnType.PK)
                record.put(column.getTable()+".pkValue",record.get(column.getName()));
        }
        return record;
    }

    /**
     * 获取表数据
     * @param table
     * @param num
     * @param results
     * @param applicationContext
     */
    public void getTable(Table table,Integer num,Map<String,List<Map<String,Object>>> results,Object fkValue,ApplicationContext applicationContext){
        List<Map<String,Object>> records=new ArrayList<>();
        for (int i=0;i<num;i++){
            Map<String,Object> record=this.getRecord(table.getColumns(),fkValue,applicationContext);
            records.add(record);
            for (Table child:table.getChildTalbes()){
                this.getTable(child,Integer.parseInt(this.getQZ(table.getProportion(),table.getName(),applicationContext)),results,record.get(table.getName()+".pkValue"),applicationContext);
            }
        }
        List<Map<String,Object>> temp=results.get(table.getName());
        if (temp==null)
            results.put(table.getName(),records);
        else
            results.get(table.getName()).addAll(records);
    }

}
