package data.utils;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by gene on 2016/6/27.
 */
public class Params {
    public static String DATA_SOURCE_IP;
    public static String DATA_SOURCE_PORT;
    public static String DATA_SOURCE_USER;
    public static String DATA_SOURCE_PASSWORD;
    public static String DATA_SOURCE_DB;

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
            DATA_SOURCE_DB = readProps.getProperty("dataSource.jdbc.db");

        }catch (Exception e){

        }
    }
}
