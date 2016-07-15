package data.test;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by gene on 2016/6/27.
 */
public class TestSql {

//    private static Integer dataSize = 50000;
//    private static Integer threadSize = 10000;
    private static Map<String,Object> tableStructMap = new HashMap<>();
    public static String sqlDir;
    public static String batDir;
    public static String mainBatFile;
    public static void insertData() throws Exception{
        System.out.println("开始----" + new Date());
//        List<Map<String,Object>> list = getDataList();
//        batchDeal(list);
        initMap();

        fileBatchDeal(sqlDir);
        System.out.println("结束--------" + new Date());
    }

    /**
     * 初始化map参数
     */
    public static void initMap(){
//        sqlDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "resource" +System.getProperty("file.separator") + "sql";
//        batDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "resource" +System.getProperty("file.separator") + "bat";
        sqlDir = "E:/idea/dataTemplate" +"/"+ "resource" +"/"+ "sql";
        batDir = "E:/idea/dataTemplate" + "/" + "resource" +"/" + "bat";
        mainBatFile=batDir+"/"+"mainBat.bat";
        tableStructMap.put("T_XS_AJ","T_XS_AJ ( C_BH,N_AJXH,N_AJBS,N_AJLB,N_JBFY,N_SPCX,N_SYCX,C_AH,C_SPZH,N_QSZZM,D_LARQ,N_LAR,N_LATS,D_JARQ,D_SXRQ,C_SSQQ )");
        tableStructMap.put("T_XS_YSQK","T_XS_YSQK ( C_BH,C_BH_AJ,N_AJLB,N_JBFY,N_CBSPT,C_AH,N_SPCX,D_LARQ,D_JARQ,N_JAFS )");
        tableStructMap.put("T_XS_BGR","T_XS_BGR ( C_BH,C_BH_AJ,C_MC,N_XB,D_CSRQ,N_NL,N_GJ,N_MZ,N_SFZJZL,C_SFZJHM,C_DZ,C_YZBM,C_LXDH,C_DZYX )");
        tableStructMap.put("T_XS_ZKZM","T_XS_ZKZM ( C_BH,C_BH_AJ,C_BH_BGR,N_ZM,C_ZMZS,N_ZZM,N_XH )");
    }


    /**
     * 开启线程操作bat
     * @param filePath
     */
    public static void fileBatchDeal(String filePath){
        System.out.println("文件批量操作开始----" + new Date());
        List<File> fileList = getFiles(filePath);
        ExecutorService service = Executors.newFixedThreadPool(2);
        ExecutorCompletionService<String> completion = new ExecutorCompletionService<String>(service);

        for (int i = 0; i < fileList.size(); i++) {
            try{
                FileMultCalc t = new FileMultCalc();
                File file = fileList.get(i);
                String fileName = file.getName();
                String tableName = fileName.substring(3,fileName.length()-4);
                t.setTableStruct(tableStructMap.get(tableName).toString());
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
        createMainBat();
        try{
            callCmd(mainBatFile);
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

    public static void createMainBat(){
        FileWriter fw=null;
        try {
            List<File> batFileList = getFiles(batDir);
            StringBuilder sb = new StringBuilder();
            sb.append("@echo off").append("\n");
            sb.append("set d=%date:~0,10%").append("\n")
                    .append("set t=%time:~0,8%").append("\n")
                    .append("echo %d% %t%  copy data begin").append("\n");
            if(batFileList != null && batFileList.size() != 0){
                for(File file : batFileList){
                    sb.append("call ").append(file.getAbsolutePath().replace("\\","/")).append("\n");
                }
            }
            sb.append("set d=%date:~0,10%").append("\n")
                    .append("set t=%time:~0,8%").append("\n")
                    .append("echo %d% %t%  copy data end");
            fw=new FileWriter(mainBatFile);
            BufferedWriter buffer = new BufferedWriter(fw);
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

    /**
     * 后台执行cmd文件
     * @param locationCmd
     * @throws Exception
     */
    public static void  callCmd(String locationCmd) throws Exception{
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

    public static String[] doWaitFor(Process p){
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
     * jdbc 批处理存数据
     * @param
     * @throws Exception
     */
  /*  public static void batchDeal(List<Map<String,Object>> list) throws Exception{
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
    }*/

    /**
     * 生成list数据
     * @return
     */
   /* public static List<Map<String,Object>> getDataList(){
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
    }*/
}
