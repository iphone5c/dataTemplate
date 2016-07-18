import data.Thread.StartMain;
import data.core.ApplicationContext;
import data.core.ColunmDataService;
import data.core.Table;
import data.core.XmlDatasFactory;
import data.test.FileMultCalc;
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
//
        StartMain.start(10,applicationContext);
    }
}