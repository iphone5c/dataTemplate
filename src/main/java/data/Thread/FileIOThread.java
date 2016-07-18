package data.Thread;

import data.core.Column;
import data.core.Table;
import data.core.XmlDatasFactory;
import data.batfile.CreateFile;
import data.utils.DataUtils;
import data.utils.Params;

import java.io.*;
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
//                    System.out.println("执行插入开始："+obj.getKey()+"====>"+obj.getValue().size()+"====>"+ DataUtils.dateToString(new Date(),DataUtils.DATEFORMAT_DATETIME_EN_LONG));
//                    createFile.createDataFile(XmlDatasFactory.getTableByTableName(obj.getKey(), table), obj.getValue());
                    this.ioFileOut(XmlDatasFactory.getTableByTableName(obj.getKey(), table), obj.getValue());
//                    System.out.println("执行插入结束："+obj.getKey()+"====>"+obj.getValue().size()+"====>"+ DataUtils.dateToString(new Date(),DataUtils.DATEFORMAT_DATETIME_EN_LONG));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ioFileOut(Table table,List<Map<String,Object>> records){
        try {
            FileOutputStream fos = new FileOutputStream(Params.sqlDir+"/data/"+table.getName()+".txt",true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
            for (Map<String,Object> record:records){
                StringBuffer info=new StringBuffer();
                for (Column column:table.getColumns()){
                    Object columnValue = record.get(column.getName());
                    if(columnValue == null){
                        info.append("0").append(",");
                    }else {
                        info.append(columnValue.toString()).append(",");
                    }
                }
                info.deleteCharAt(info.length() - 1);
                info.append("\n");
                osw.write(info.toString());
                osw.flush();
            }
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
