import data.core.ApplicationContext;
import data.core.Table;
import data.core.XmlDatasFactory;
import data.utils.Factory;
import data.utils.Params;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {
        //初始化应用上下文
        ApplicationContext applicationContext=new ApplicationContext();
        //todo 根据模版生成数据

        //获取数据库连接
//        Connection connection = Factory.getConn(Params.DATA_SOURCE_IP, Params.DATA_SOURCE_PORT, Params.DATA_SOURCE_USER, Params.DATA_SOURCE_PASSWORD, Params.DATA_SOURCE_DB);
        //todo 插入数据

    }
}
