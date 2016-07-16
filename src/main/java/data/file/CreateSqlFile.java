package data.file;

import data.core.Column;
import data.core.Table;
import data.utils.Params;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

/**
 * Created by gene on 2016/7/11.
 */
public class CreateSqlFile {

    /**
     * 根据表和数据写sql文件
     * @param table
     * @param list
     */
    public static void createFile(Table table,List<Map<String,Object>> list){
        //以时间戳加表名命名文件夹名称
        long dateTime = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString().replace("-","");
        if(table == null || list == null || list.size() == 0){
            System.out.println("组装sql文件时，表和数据不能为空.");
        }
        String tableStruct = Params.tableMap.get(table.getName()) == null ? "" :Params.tableMap.get(table.getName()).toString() ;
        if(tableStruct.isEmpty()){
            tableStruct = getTableStructSql(table);
            Params.tableMap.put(table.getName(),tableStruct);
        }
        //values用来组装所有数据(1,2,3),(1,2,3)
        StringBuffer values = new StringBuffer();
        for(int i=0; i<list.size(); i++){
            Map<String,Object> map = list.get(i);
            //val用来组装单挑数据(1,2,3)
            StringBuffer val = new StringBuffer();
            val.append("(");
            for(Column column : table.getColumns()){
                Object columnValue = map.get(column.getName());
                if(columnValue == null){
                    val.append(columnValue).append(",");
                }else {
                    val.append("'").append(columnValue.toString().replace("'","''")).append("'").append(",");
                }
            }
            val.deleteCharAt(val.length()-1);
            val.append(")");
            values.append(val).append(",");
        }
        values.deleteCharAt(values.length() -1);
        StringBuffer sql = new StringBuffer();
        sql.append(tableStruct).append(values);
        try{
            File dir = new File(Params.sqlDir);
            if(!dir.exists()){
                //创建目录
                dir.mkdirs();
            }
            String sqlPath = Params.sqlDir + "/" + dateTime + "_" + table.getName()+".sql";
            FileWriter fw=null;
            fw=new FileWriter(sqlPath);
            BufferedWriter buffer = new BufferedWriter(fw);
            buffer.write(sql.toString());
            buffer.flush();
            buffer.close();
            FileBatInsert.fileBatDeal(new File(sqlPath));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 根据table获取表结构
     * @param table
     * @return
     */
    private static String getTableStructSql(Table table){
        StringBuffer sb = new StringBuffer();
        sb.append("insert into ").append(table.getName()).append("(");
        for (Column column: table.getColumns()){
            sb.append(column.getName()).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(")").append(" values ");
        return sb.toString();
    }
}
