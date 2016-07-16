package data.Thread;

import data.core.Table;
import data.core.XmlDatasFactory;
import data.batfile.CreateFile;
import data.utils.DataUtils;
import data.utils.Params;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
            CreateFile createFile = new CreateFile();
            int count=0;
            while (count<num) {
                Map<String, List<Map<String, Object>>> map = Params.blockingDeque.take();
                for (Map.Entry<String,List<Map<String, Object>>> obj:map.entrySet()){
                    if (obj.getKey().equals(table.getName())){
                        count+=obj.getValue().size();
                    }
                    System.out.println("执行插入开始："+obj.getKey()+"====>"+obj.getValue().size()+"====>"+ DataUtils.dateToString(new Date(),DataUtils.DATEFORMAT_DATETIME_EN_LONG));
                    createFile.createDataFile(XmlDatasFactory.getTableByTableName(obj.getKey(), table), obj.getValue());
                    System.out.println("执行插入结束："+obj.getKey()+"====>"+obj.getValue().size()+"====>"+ DataUtils.dateToString(new Date(),DataUtils.DATEFORMAT_DATETIME_EN_LONG));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ioFileOut(String tableName,List<Map<String,Object>> records){
        try {
            BufferedWriter buffer = new BufferedWriter(new FileWriter("E:/"+tableName+".txt",true));
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
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
