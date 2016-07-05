package data.test;

import data.utils.Factory;
import data.utils.Params;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by gene on 2016/6/27.
 */
public class TestSql {

    private static Integer dataSize = 50000;
    private static Integer threadSize = 10000;
    public static void insertData() throws Exception{
        System.out.println("开始----" + new Date());
        List<Map<String,Object>> list = getDataList();
//        batchDeal(list);
        fileBatchDeal("resource");
        System.out.println("结束--------" + new Date());
    }

    public static void batchDeal(List<Map<String,Object>> list) throws Exception{
        System.out.println("批量insert操作开始----" + new Date());
        ExecutorService service = Executors.newFixedThreadPool(2);
        ExecutorCompletionService<String> completion = new ExecutorCompletionService<String>(service);

        for (int i = 0; i < dataSize/threadSize; i++) {
            try{
                List<Map<String,Object>> batchList = new ArrayList<>();
                batchList = list.subList(i*threadSize,(i+1)*threadSize);
                MultCalc t = new MultCalc();
                t.setList(batchList);
                t.setThread(i+1);
                completion.submit(t);
            }catch (Exception e) {
                continue;
            }
        }

        //保证这些并发参数执行完毕后，再回到主线程。
        for (int i = 0; i < dataSize/threadSize; i++) {
            try {
                //获得的是CsjsThread.call()方法的返回值
                Future<String> a = completion.take();
                String b = a.get();
            } catch (Exception e) {
            }
        }
        service.shutdown();
    }

    public static void fileBatchDeal(String filePath){
        System.out.println("文件批量操作开始----" + new Date());
        List<File> fileList = getFiles(filePath);
        ExecutorService service = Executors.newFixedThreadPool(10);
        ExecutorCompletionService<String> completion = new ExecutorCompletionService<String>(service);

        for (int i = 0; i < fileList.size(); i++) {
            try{
                FileMultCalc t = new FileMultCalc();
                t.setTableStruct("t_aty_cfgid(c_key,n_value)");
                t.setFileName(fileList.get(i).getPath());
                t.setThread(i+1);
                completion.submit(t);
            }catch (Exception e) {
                continue;
            }
        }

        //保证这些并发参数执行完毕后，再回到主线程。
        for (int i = 0; i < threadSize; i++) {
            try {
                //获得的是CsjsThread.call()方法的返回值
                Future<String> a = completion.take();
                String b = a.get();
            } catch (Exception e) {
            }
        }
        service.shutdown();
    }

    public static List<File> getFiles( String filePath )
    {
        File root = new File( filePath );
        File[] files = root.listFiles();
       return Arrays.asList(files);
    }
    public static List<Map<String,Object>> getDataList(){
        List<Map<String,Object>> list = new ArrayList<>();
        for(int i=0; i<dataSize; i++){
            Map<String,Object> map = new HashMap<>();
            map.put("c_key",i);
            map.put("n_value",i);
            map.put("n_fldlength",i);
            list.add(map);
        }
        System.out.println("生成list数据结束----------"+new Date());
        return list;
    }
}
