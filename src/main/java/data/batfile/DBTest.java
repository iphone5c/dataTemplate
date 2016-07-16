package data.batfile;

import data.utils.Params;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by gene on 2016/7/5.
 */
public class DBTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        String fileDir = Params.sqlDir  + "/bat";
        System.out.println("文件批量操作开始----" + new Date());
        List<File> fileList = getFiles(fileDir);
        ExecutorService service = Executors.newFixedThreadPool(5);
        ExecutorCompletionService<String> completion = new ExecutorCompletionService<String>(service);

        for (int i = 0; i < fileList.size(); i++) {
            try{
                FileMultCalc t = new FileMultCalc();
                File file = fileList.get(i);
                t.setFileName(file.getAbsolutePath().replace("\\","/"));
                t.setThread(i+1);
                completion.submit(t);
            }catch (Exception e) {
                continue;
            }
        }
        //保证这些并发参数执行完毕后，再回到主线程。
        for (int i = 0; i < fileList.size(); i++) {
            try {
                //获得的是CsjsThread.call()方法的返回值
                Future<String> a = completion.take();
                String b = a.get();
            } catch (Exception e) {
                System.out.println("==========判断线程是否完成错误============");
            }
        }
        service.shutdown();
        try{
        }catch (Exception e){
            System.out.println("----导入数据出错----"+e.getMessage());
        }
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
