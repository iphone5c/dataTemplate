package data.Thread;

import data.core.Table;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2016/7/5.
 */
public class FileIOThread implements Runnable {

    private Table table;
    private List<Map<String,Object>> records;

    public FileIOThread(Table table, List<Map<String, Object>> records) {
        this.table = table;
        this.records = records;
    }

    @Override
    public void run() {
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
