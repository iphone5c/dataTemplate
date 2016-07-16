package data.batfile;

import data.core.Column;
import data.core.Table;
import data.utils.Params;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by gene on 2016/7/11.
 */
public class CreateFile {

    /**
     * 根据表和数据写csv文件
     * @param table
     * @param list
     */
    public void createDataFile(Table table,List<Map<String,Object>> list){
        String uuid = UUID.randomUUID().toString().replace("-","");
        if(table == null || list == null || list.size() == 0){
            System.out.println("组装sql文件时，表和数据不能为空.");
        }
        //values用来组装所有数据1,2,3
        StringBuffer values = new StringBuffer();
        for(int i=0; i<list.size(); i++){
            Map<String,Object> map = list.get(i);
            //val用来组装单挑数据(1,2,3)
            StringBuffer val = new StringBuffer();
            for(Column column : table.getColumns()){
                Object columnValue = map.get(column.getName());
                if(columnValue == null){
                    val.append(columnValue).append(",");
                }else {
                    val.append(columnValue.toString()).append(",");
                }
            }
            val.deleteCharAt(val.length()-1);
            values.append(val).append("\n");
        }
        try{
            String fileDir = Params.sqlDir + "/" + table.getName() + "/data";
            File dir = new File(fileDir);
            if(!dir.exists()){
                //创建目录
                dir.mkdirs();
            }
            String dataPath = fileDir + "/" + uuid + "_" + table.getName()+".txt";

            FileOutputStream fos = new FileOutputStream(dataPath);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
            osw.write(values.toString());
            osw.flush();
            createBatFile(table,dataPath);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     *创建bat文件
     * @param table
     * @param dataPath
     */
    public void createBatFile(Table table,String dataPath){
        String uuid = UUID.randomUUID().toString().replace("-","");
        String fileDir = Params.sqlDir + "/" + table.getName() + "/bat";
        String tableStruct = getTableStructSql(table);
        File dir = new File(fileDir);
        if(!dir.exists()){
            //创建目录
            dir.mkdirs();
        }
        String batPath = fileDir + "/" + uuid + "_" + table.getName()+".bat";
        FileWriter fw=null;
        try {
            fw=new FileWriter(batPath);
            BufferedWriter buffer = new BufferedWriter(fw);
            StringBuilder sb = new StringBuilder();
            sb.append("@echo off").append("\n")
                    .append("set base="+Params.DATA_SOURCE_DB).append("\n")
                    .append("set host="+Params.DATA_SOURCE_IP).append("\n")
                    .append("set port="+Params.DATA_SOURCE_PORT).append("\n")
                    .append("set psql=psql -h %host% -p %port% -d %base% -U postgres").append("\n")
                    .append("set PGPASSWORD="+Params.DATA_SOURCE_PASSWORD).append("\n")
                    .append("set fileUrl="+dataPath).append("\n")
                    .append("set tableStruct="+tableStruct).append("\n")
                    .append("%psql% -c \"copy %tableStruct% from E'%fileUrl%' USING delimiters ',';\"").append("\n")
                    .append("echo "+dataPath+" copy data end ").append("\n")
                    .append("exit");
            buffer.write(sb.toString());
            buffer.flush();
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(fw!=null){
                try {
                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据table获取表结构
     * @param table
     * @return
     */
    private String getTableStructSql(Table table){
        String tableStruct = Params.tableMap.get(table.getName()) == null ? "" :Params.tableMap.get(table.getName()).toString() ;
        if(tableStruct.isEmpty()){
            StringBuffer sb = new StringBuffer();
            sb.append(table.getName()).append("(");
            for (Column column: table.getColumns()){
                sb.append(column.getName()).append(",");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append(")");
            tableStruct = sb.toString();
            Params.tableMap.put(table.getName(),tableStruct);
        }
        return tableStruct;
    }
}
