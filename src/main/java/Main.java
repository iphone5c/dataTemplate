import data.Thread.StartMain;
import data.core.ApplicationContext;
import data.core.ColunmDataService;
import data.core.Table;
import data.core.XmlDatasFactory;
import data.utils.DataUtils;
import data.utils.Factory;
import data.utils.Params;
import javafx.scene.control.Tab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {
        //初始化应用上下文
        ApplicationContext applicationContext=new ApplicationContext();

//        StartMain.start(1,applicationContext);
        //todo 根据模版生成数据
//        System.out.println(results);
        //获取数据库连接
//        Connection connection = Factory.getConn(Params.DATA_SOURCE_IP, Params.DATA_SOURCE_PORT, Params.DATA_SOURCE_USER, Params.DATA_SOURCE_PASSWORD, Params.DATA_SOURCE_DB);
        //todo 插入数据
    }
}