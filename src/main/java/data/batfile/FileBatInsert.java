package data.batfile;

import data.utils.Params;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.apache.tools.ant.types.EnumeratedAttribute;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wy on 2016/7/8.
 */
public class FileBatInsert {

    /**
     * 后台执行cmd文件
     * @param locationCmd
     * @throws Exception
     */
    public void  callCmd(String locationCmd) throws Exception{
        try {
            Process child = Runtime.getRuntime().exec("cmd.exe /C start "+locationCmd);
            child.waitFor();
            String result[] = doWaitFor(child);
            System.out.println("跑bat文件导入数据的结果:" + result[0]+result[1]+"----");
        } catch (Exception e) {
            System.out.println("=============跑bat文件出错：" + e.getMessage());
            e.printStackTrace();
        }
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

    /**
     * 获取该目录下的文件列表
     * @param filePath
     * @return
     */
    public static List<File> getFiles( String filePath )
    {
        File root = new File( filePath );
        File[] files = root.listFiles();
        return Arrays.asList(files);
    }
}
