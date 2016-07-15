package data.Thread;

import data.core.Column;
import data.core.Table;
import data.core.XmlDatasFactory;
import data.file.CreateSqlFile;
import data.file.FileBatInsert;
import data.jdbc.JdbcBatInsert;
import data.utils.DataUtils;
import data.utils.Params;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by wy on 2016/7/5.
 */
public class FileIOThread implements Runnable {

    private Table table;

    private int num;

    public FileIOThread(Table table,int num) {
        this.table = table;
        this.num=num;
    }

    @Override
    public void run() {
        try {
            int count=0;
            while (count<num) {
                Map<String, List<Map<String, Object>>> map = Params.blockingDeque.take();
                for (Map.Entry<String,List<Map<String, Object>>> obj:map.entrySet()){
                    if (obj.getKey().equals(table.getName())){
                        count+=obj.getValue().size();
                    }
//                    CreateSqlFile.createFile(XmlDatasFactory.getTableByTableName(obj.getKey(), table), obj.getValue());
                    this.ioFileOut(XmlDatasFactory.getTableByTableName(obj.getKey(), table),obj.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ioFileOut(Table table,List<Map<String,Object>> records){
        try {
            BufferedWriter buffer = new BufferedWriter(new FileWriter("E:/"+table.getName()+".sql",true));
            String info=this.getSQL(table,records);
            buffer.write(info);
            buffer.flush();
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getColumns(Table table){
        StringBuffer columns=new StringBuffer();
        for (Column column:table.getColumns()){
            columns.append(column.getName()).append(",");
        }
        columns.deleteCharAt(columns.length()-1);
        return columns.toString();
    }

    private String getSQL(Table table,List<Map<String,Object>> records){
        StringBuffer sql=new StringBuffer();
        StringBuffer columns=new StringBuffer();
        StringBuffer columnsValues=new StringBuffer();
        sql.append("INSERT INTO ").append(table.getName()).append("(").append(this.getColumns(table)).append(") VALUES");
        for (Map<String,Object> record:records){
            columnsValues.append("(");
            for (Column column:table.getColumns()){
                Object columnValue = record.get(column.getName());
                if(columnValue == null){
                    columnsValues.append(columnValue).append(",");
                }else {
                    columnsValues.append("'").append(columnValue.toString().replace("'","''")).append("'").append(",");
                }
            }
            columnsValues.deleteCharAt(columnsValues.length()-1);
            columnsValues.append("),");
        }
        columnsValues.deleteCharAt(columnsValues.length()-1);
        sql.append(columnsValues);
        return sql.toString();
    }
}
