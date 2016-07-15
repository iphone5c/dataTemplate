package data.test;
import data.utils.Factory;
import data.utils.Params;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 根据生成的表数据文件来写bat文件
 */
public class FileMultCalc implements Callable<String> {

    private int thread;
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

    @Override
    public String call(){
        try{
            System.out.println(new Date() + "----------线程" + thread + "----------执行开始.....");
            String batPath=TestSql.batDir+"/"+"bat"+thread+".bat";
            //创建bat文件
            creatBat(batPath,fileName);
        }catch (Exception e){
            System.out.println("------创建bat文件ERROR:" + thread + e.getMessage());
        }finally {
        }
        System.out.println(new Date() + "----------创建bat线程" + thread + "----------执行完毕.....");
        return null;
    }

    /**
     * 创建bat文件
     * @param batPath
     * @param filePath
     * @throws Exception
     */
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
                    .append("echo "+filePath+" copy data end ").append("\n");

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
}
