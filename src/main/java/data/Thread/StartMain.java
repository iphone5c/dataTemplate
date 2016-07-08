package data.Thread;

import data.core.ApplicationContext;
import data.core.Table;

import java.util.List;
import java.util.Map;
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
//        LinkedBlockingDeque<Map<String,List<Map<String,Object>>>> blockingDeque=new LinkedBlockingDeque<>();
        ExecutorService exe = Executors.newFixedThreadPool(50);
        for (Table table:applicationContext.getTableList()){
            int num=table.getNum()/threadNum;
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
    }

}
