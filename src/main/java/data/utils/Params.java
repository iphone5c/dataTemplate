package data.utils;

import data.dbcache.DataCache;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by gene on 2016/6/27.
 */
public class Params {
    //数据库参数
    public static String DATA_SOURCE_IP;
    public static String DATA_SOURCE_PORT;
    public static String DATA_SOURCE_USER;
    public static String DATA_SOURCE_PASSWORD;
    public static String DATA_SOURCE_DB;
    public static String DATA_SOURCE_SCHEMA;
    public static String DATA_SOURCE_URL;
    public static String DATA_SOURCE_DRIVER;

    //用户部门缓存结果
    public static String queryUserSql = "select * from t_aty_user";
    public static String queryCropSql = "select * from t_aty_corp";
    public static String queryDeptSql = "select * from t_aty_dept";
    public static List<Map<String,Object>> userList;
    public static List<Map<String,Object>> cropList;
    public static List<Map<String,Object>> deptList;
    public static String sqlDir=System.getProperty("user.dir").replace("\\","/").concat("/resource/sql");
    public static LinkedBlockingDeque<Map<String,List<Map<String,Object>>>> blockingDeque;

    public static List<Map<String,Object>> tableList = new ArrayList<>();

    public static Integer insertSize=10000;

    //获取表结构("tableName","insert into tableName (a,b,c) values")
    public static Map tableMap = new HashMap<String,Object>();
    /**
     * 初始化数据库参数
     */
    public static void initParam(){
        try {
            Properties readProps = new Properties();
            String aboPath = System.getProperty("user.dir");
            FileInputStream inStream = new FileInputStream(aboPath + "/config.properties");
            readProps.load(inStream);
            DATA_SOURCE_IP = readProps.getProperty("dataSource.ip");
            DATA_SOURCE_PORT = readProps.getProperty("dataSource.port");
            DATA_SOURCE_USER = readProps.getProperty("dataSource.jdbc.user");
            DATA_SOURCE_PASSWORD = readProps.getProperty("dataSource.jdbc.password");
            DATA_SOURCE_DB = readProps.getProperty("dataSource.jdbc.dataBase");
            DATA_SOURCE_SCHEMA = readProps.getProperty("dataSource.jdbc.schema");
            DATA_SOURCE_URL = readProps.getProperty("dataSource.jdbc.Url");
            DATA_SOURCE_DRIVER = readProps.getProperty("dataSource.jdbc.driverClass");

            userList = DataCache.getDataList(queryUserSql);
            cropList = DataCache.getDataList(queryCropSql);
            deptList = DataCache.getDataList(queryDeptSql);
            blockingDeque=new LinkedBlockingDeque<>();
            File dir = new File(Params.sqlDir);
            if(!dir.exists()){
                //创建目录
                dir.mkdirs();
            }else {
                dir.delete();
            }
            System.out.println("--------初始化参数完毕-----------");
        }catch (Exception e){

        }
    }
}
