package data.Thread;

import data.batfile.FileBatInsert;
import data.core.ApplicationContext;
import data.core.Column;
import data.core.Table;
import data.utils.Params;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by wy on 2016/7/5.
 */
public class StartMain {

    public static void start(int threadNum,ApplicationContext applicationContext){
        System.out.println("数据组装开始，请稍后。。。");
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        LinkedBlockingDeque<Map<String,List<Map<String,Object>>>> blockingDeque=new LinkedBlockingDeque<>();
        ExecutorService exe = Executors.newFixedThreadPool(50);
        for (Table table:applicationContext.getTableList()){
            int num=1;
            if (table.getNum()>threadNum){
                num=table.getNum()/threadNum;
            }
            for (int i = 1; i <= threadNum; i++) {
                exe.execute(new StartThread(table,num,applicationContext));
                exe.execute(new FileIOThread(table,num));
            }
            exe.shutdown();
            while (true) {
                if (exe.isTerminated()) {
                    endTime = System.currentTimeMillis();
                    break;
                }
            }
        }
        System.out.println("数据组装结束（输出到文件），耗费时间为："+(endTime-startTime)/1000+"秒");
        System.out.println("数据开始入库");
        new StartMain().runBat(applicationContext.getTableList());

    }


    public void runBat(List<Table> tableList){
        if (tableList!=null&&tableList.size()>0){
            for (Table table:tableList){
                this.createBatFile(table,Params.sqlDir  + "/data/"+table.getName()+".txt");
                if (table.getChildTalbes()!=null&&table.getChildTalbes().size()>0)
                    this.runBat(table.getChildTalbes());
            }
        }
    }

    /**
     *创建bat文件
     * @param table
     * @param dataPath
     */
    public void createBatFile(Table table,String dataPath){
        FileBatInsert fileBatInsert = new FileBatInsert();
        String uuid = UUID.randomUUID().toString().replace("-","");
        String fileDir = Params.sqlDir  + "/bat";
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
                    .append("set base=" + Params.DATA_SOURCE_DB).append("\n")
                    .append("set host=" + Params.DATA_SOURCE_IP).append("\n")
                    .append("set port=" + Params.DATA_SOURCE_PORT).append("\n")
                    .append("set psql=psql -h %host% -p %port% -d %base% -U postgres").append("\n")
                    .append("set PGPASSWORD=" + Params.DATA_SOURCE_PASSWORD).append("\n")
                    .append("set fileUrl=" + dataPath).append("\n")
                    .append("set tableStruct=" + tableStruct).append("\n")
                    .append("%psql% -c \"copy %tableStruct% from E'%fileUrl%' USING delimiters ',';\"").append("\n")
                    .append("echo " + dataPath + " copy data end ").append("\n")
                    .append("echo %time%").append("\n")
                    .append("exit");
            buffer.write(sb.toString());
            buffer.flush();
            buffer.close();
            fileBatInsert.callCmd(batPath);
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
