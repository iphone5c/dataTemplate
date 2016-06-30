package data.utils;

import data.dbcache.DataCache;

import java.io.FileInputStream;
import java.util.*;

/**
 * Created by gene on 2016/6/27.
 */
public class Params {
    public static String DATA_SOURCE_IP;
    public static String DATA_SOURCE_PORT;
    public static String DATA_SOURCE_USER;
    public static String DATA_SOURCE_PASSWORD;
    public static String DATA_SOURCE_DB;
    public static String DATA_SOURCE_SCHEMA;

    public static String queryUserSql = "select * from t_aty_user";
    public static String queryCropSql = "select * from t_aty_corp";
    public static String queryDeptSql = "select * from t_aty_dept";
    public static List<Map<String,Object>> userList;
    public static List<Map<String,Object>> cropList;
    public static List<Map<String,Object>> deptList;

    public static List<Map<String,Object>> tableList = new ArrayList<>();

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

            userList = DataCache.getDataList(queryUserSql);
            cropList = DataCache.getDataList(queryCropSql);
            deptList = DataCache.getDataList(queryDeptSql);
            System.out.println("--------初始化参数完毕-----------");
        }catch (Exception e){

        }
    }
}
