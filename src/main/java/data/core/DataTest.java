package data.core;

import data.utils.ColumnType;
import data.utils.DataUtils;
import data.utils.Params;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by wy on 2016/7/5.
 */
public class DataTest {

    public void getTableList(List<Table> tableList,ApplicationContext applicationContext){
        for (Table table:tableList){
            this.getTableData(table, table.getNum(),null, applicationContext);
        }
    }

    public void getTableData(Table table,Integer recordNum,Map<String,Object> parentRecord,ApplicationContext applicationContext){
        List<Map<String,Object>> records=new ArrayList<>();
        List<Map<String,Object>> userList= Params.userList;
        int size=0;
        for (int i=0;i<recordNum;i++){
            Map<String,Object> record=this.getRecord(table.getColumns(), userList.get(DataUtils.getRanDom(0, userList.size() - 1)),parentRecord, applicationContext);
            records.add(record);
            for (Table child:table.getChildTalbes()){
                this.getTableData(child, Integer.parseInt(this.getQZ(child.getProportion(), child.getName(), applicationContext)), record, applicationContext);
            }
            size++;
            if (records.size()>10000) {
                records.clear();
//                this.saveFiles(table,records);
            }
        }
//        this.saveFiles(table, records);
        if (table.getName().equals("T_XS_AJ")){
            applicationContext.getParams().put("records",size);
        }
    }

    public Map<String,Object> getRecord(List<Column> columns,Map<String,Object> user,Map<String,Object> parentRecord,ApplicationContext applicationContext){
        Map<String,Object> record=new HashMap<>();
        for (Column column:columns){
            this.getColumn(column, record, user,parentRecord, applicationContext);
            if (column.getColumnType()==ColumnType.PK)
                record.put("["+column.getTable().getName()+"."+column.getName()+"]",record.get(column.getName()));
        }
        return record;
    }

    public void getColumn(Column column,Map<String,Object> record,Map<String,Object> user,Map<String,Object> parentRecord,ApplicationContext applicationContext){
        if (record.get(column.getName())==null){
            switch (column.getGenerator()){
                case "UUID":
                    record.put(column.getName(), this.getUUID());
                    break;
                case "SEQ":
                    StringBuffer key=new StringBuffer(column.getTable().getName()).append(".").append(column.getName());
                    record.put(column.getName(),this.getSEQ(key.toString(),Integer.parseInt(column.getMin()),true,applicationContext));
                    break;
                case "LABLE":
                    String content = column.getContent();
                    String[] colunmNames=DataUtils.getString(content,"(\\[[^\\]]+\\])");
                    if (colunmNames!=null){
                        for (String name:colunmNames){
                            String keyName=name.substring(1,name.length()-1);
                            this.getColumn(XmlDatasFactory.getColumnByName(keyName, column.getTable()), record, user,parentRecord, applicationContext);
                            content=content.replaceAll("\\["+keyName+"\\]",record.get(keyName).toString());
                        }
                    }
                    colunmNames=DataUtils.getString(content,"(\\{[^\\}]+\\})");
                    if (colunmNames!=null){
                        boolean flag=true;
                        for (String name:colunmNames){
                            String keyName=name.substring(1,name.length()-1);
                            switch (keyName){
                                case "UUID":
                                    content=content.replaceAll("\\{"+keyName+"\\}",UUID.randomUUID().toString());
                                    break;
                                case "SEQ":
                                    content=content.replaceAll("\\{"+keyName+"\\}",this.getSEQ(column.getTable().getName()+"."+column.getName()+"."+keyName,Integer.parseInt(column.getMin()),flag,applicationContext).toString());
                                    flag=false;
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
                    if(column.getColumnType()== ColumnType.DATE){
                        Date start=null;
                        Date end=null;
                        String[] startDate=DataUtils.getString(column.getMin(),"(\\[[^\\]]+\\])");
                        String[] endDate=DataUtils.getString(column.getMax(),"(\\[[^\\]]+\\])");
                        if (startDate==null){
                            start=DataUtils.toDate(column.getMin(),DataUtils.DATEFORMAT_DATA_EN_LONG);
                        }else {
                            String keyName=startDate[0].substring(1,startDate[0].length()-1);
                            this.getColumn(XmlDatasFactory.getColumnByName(keyName, column.getTable()), record, user, parentRecord, applicationContext);
                            start= DataUtils.toDate((String) record.get(keyName),DataUtils.DATEFORMAT_DATA_EN_LONG);
                        }

                        if (endDate==null){
                            end=DataUtils.toDate(column.getMax(),DataUtils.DATEFORMAT_DATA_EN_LONG);
                        }else {
                            String keyName=endDate[0].substring(1,endDate[0].length()-1);
                            this.getColumn(XmlDatasFactory.getColumnByName(keyName, column.getTable()), record, user, parentRecord, applicationContext);
                            end= DataUtils.toDate((String) record.get(keyName),DataUtils.DATEFORMAT_DATA_EN_LONG);
                        }
                        record.put(column.getName(),DataUtils.dateToString(DataUtils.getRanDomDate(start,end),DataUtils.DATEFORMAT_DATA_EN_LONG));
                    }else if(column.getColumnType()==ColumnType.NUM){
                        record.put(column.getName(),DataUtils.getRanDom(Integer.parseInt(column.getMin()),DataUtils.isEmptyOrNull(column.getMax())?Integer.MAX_VALUE:Integer.parseInt(column.getMax())));
                    }else if(column.getColumnType()==ColumnType.ORGAN){
                        if (column.getContent().equals("user")){
                            record.put(column.getName(),user.get("c_id"));
                        }else if (column.getContent().equals("dept")){
                            record.put(column.getName(),user.get("c_dept"));
                        }else if (column.getContent().equals("corp")){
                            record.put(column.getName(),user.get("c_corp"));
                        }
                    }
                    break;
                case "TEXT":
                    String info= (String) applicationContext.getParams().get(XmlDatasFactory.getRootTableByColumn(column).getName());
                    record.put(column.getName(),info.substring(0,DataUtils.getRanDom(1,info.length())));
                    break;
                default:
                    if (DataUtils.isEmptyOrNull(column.getGenerator())){
                        if (column.getColumnType()==ColumnType.FK){
                            record.put(column.getName(),parentRecord.get(column.getContent()));
                        }
                    }else {
                        record.put(column.getName(),this.getQZ(column.getGenerator(),column.getTable().getName()+"."+column.getName()+"."+column.getGenerator(),applicationContext));
                    }
            }
        }
    }


    private static String getUUID(){
        return UUID.randomUUID().toString();
    }

    /**
     * 获取SEQ序列
     * @param key
     * @param min
     * @param isAuto 是否自增
     * @param applicationContext
     * @return
     */
    public Integer getSEQ(String key, Integer min, boolean isAuto, ApplicationContext applicationContext){
        synchronized (this){
            Map<String,Object> map=applicationContext.getParams();
            Object obj=map.get(key);
            if (obj==null){
                applicationContext.getParams().put(key,min+1);
                return min;
            }else {
                Integer data= (Integer) obj;
                if(isAuto){
                    applicationContext.getParams().put(key,data+1);
                    return data;
                }else {
                    return data-1;
                }

            }
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
        synchronized (this){
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
                return datas.get(DataUtils.getRanDom(0,datas.size()-1));
            }else {
                List<String> list= (List<String>) obj;
                return list.get(DataUtils.getRanDom(0,list.size()-1));
            }
        }
    }

    /**
     * 输出到文件并清空缓存
     * @param table
     * @param records
     */
    private void saveFiles(Table table,List<Map<String,Object>> records){
        try {
            BufferedWriter buffer = new BufferedWriter(new FileWriter("E:/"+table.getName()+".txt",true));
            for (Map<String,Object> record:records){
                StringBuilder info=new StringBuilder();
                for (Map.Entry<String, Object> column : record.entrySet()) {
                    info.append(column.getValue()).append(",");
                }
                info.deleteCharAt(info.length() - 1);
                info.append("\r\n");
                buffer.write(info.toString());
                buffer.flush();
            }
            records.clear();
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
