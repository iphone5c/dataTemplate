package data.test;
import data.utils.Factory;
import data.utils.Params;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class FileMultCalc implements Callable<String> {

    private int thread;
//    private static int batchSize = 1000;
    private String tableStruct;
    private String fileName;
    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public String getTableStruct() {
        return tableStruct;
    }

    public void setTableStruct(String tableStruct) {
        this.tableStruct = tableStruct;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String getMapString(Map<String,Object> map){
        StringBuffer sb = new StringBuffer();
      /*  sb.append("\"").append(map.get("c_key")).append("\"").append(",");
        sb.append("\"").append(map.get("n_value")).append("\"").append(",");
        sb.append("\"").append(map.get("n_fldlength")).append("\"").append("\n");*/

        sb.append(map.get("c_key")).append(",");
        sb.append(map.get("n_value")).append("\n");
      /*  sb.append(map.get("n_value")).append(",");
        sb.append(map.get("n_fldlength")).append("\n");*/
        return sb.toString();
    }

    @Override
    public String call(){
        try{
//            String fileName = "E:/test/"+"a"+thread+".txt";
            //写入文件地址
        /*    FileWriter writer = new FileWriter(fileName);
            BufferedWriter buffer = new BufferedWriter(writer);
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for(Map<String,Object> map : list){
                sb.append(getMapString(map));
                if(++count % batchSize == 0){
                    buffer.write(sb.toString());
                    sb = new StringBuilder();
                }
            }
            buffer.write(sb.toString());
            buffer.flush();
            buffer.close();*/

            String batPath="bat"+thread+".bat";
            //创建bat文件
            creatBat(batPath,fileName);

            //运行bat文件
            callCmd(batPath);
        }catch (Exception e){
        }finally {
        }
        System.out.println(new Date() + "----------线程" + thread + "----------执行完毕.....");
        return null;
    }

    //创建bat文件
    public void creatBat(String batPath,String filePath) throws Exception{
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
                    .append("set fileUrl="+filePath).append("\n")
                    .append("set tableStruct="+tableStruct).append("\n")
                    .append("%psql% -c \"copy %tableStruct% from E'%fileUrl%' USING delimiters ',';\"").append("\n")
                    .append("echo \"data copy end\"").append("\n");

            buffer.write(sb.toString());
            buffer.flush();
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
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

    public void  callCmd(String locationCmd) throws Exception{
        try {
            Process child = Runtime.getRuntime().exec("cmd.exe /C start /b "+locationCmd);
            child.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
