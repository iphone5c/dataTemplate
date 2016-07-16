package data.batfile;
import data.test.TestSql;
import data.utils.Params;

import java.io.*;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * 根据生成的表数据文件来写bat文件
 */
public class FileMultCalc implements Callable<String> {

    private int thread;
    private String fileName;
    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
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
            //执行bat文件
            Process child = Runtime.getRuntime().exec("cmd.exe /C start "+fileName);
            child.waitFor();
            String result[] = doWaitFor(child);
            if(result != null){
                System.out.print(result[0]);
            }
        }catch (Exception e){
            System.out.println("------创建bat文件ERROR:" + thread + e.getMessage());
        }finally {
        }
        System.out.println(new Date() + "----------执行bat线程" + thread + "----------执行完毕.....");
        return null;
    }

    /**
     * 等待程序运行结束
     * @param p
     * @return
     */
    public String[] doWaitFor(Process p){
        InputStream in = p.getInputStream();
        InputStream err = p.getErrorStream();
        boolean finished = false;
        StringBuilder outbuild = new StringBuilder();
        StringBuilder errbuild = new StringBuilder();
        try {
            while(!finished){
                try {
                    while(in.available() > 0){
                        Character c = new Character((char)in.read());
                        errbuild.append(c);
                    }
                    while(err.available() > 0){
                        Character c = new Character((char)err.read());
                        outbuild.append(c);
                    }
                    p.exitValue();
                    finished = true;
                } catch (IllegalThreadStateException  e) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[]{outbuild.toString(),errbuild.toString()};
    }
}
