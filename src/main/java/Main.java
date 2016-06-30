import data.core.ApplicationContext;
import data.core.ColunmDataService;
import data.core.Table;
import data.core.XmlDatasFactory;
import data.utils.DataUtils;
import data.utils.Factory;
import data.utils.Params;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {
        //初始化应用上下文
        ApplicationContext applicationContext=new ApplicationContext();

        Map<String,List<Map<String,Object>>> results = new HashMap<>();
        ColunmDataService colunmDataService=new ColunmDataService();
        long startTime = System.currentTimeMillis();
        System.out.println("数据组装开始，请稍后。。。");
        for (Table table:applicationContext.getTableList()){
            colunmDataService.getTable(table,table.getNum(),results,null,applicationContext);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("数据组装结束，耗费时间为："+(endTime-startTime)/1000+"秒");
        //todo 根据模版生成数据
//        System.out.println(results);
        //获取数据库连接
//        Connection connection = Factory.getConn(Params.DATA_SOURCE_IP, Params.DATA_SOURCE_PORT, Params.DATA_SOURCE_USER, Params.DATA_SOURCE_PASSWORD, Params.DATA_SOURCE_DB);
        //todo 插入数据
//        String info="[N_JBFY][N_AJXH]{#2006:1,2007:8,2008:1#}川刑更{SEQ}号";
//        String info="{#2006:1,2007:8,2008:1#}川刑更{SEQ}号";
//        System.out.println(DataUtils.getString(info,"(\\[[^\\]]+\\])"));
//        String info="[N_JBFY]";
//        System.out.println(info.substring(1,info.length()-1));
//        String info="[N_JBFY][N_AJXH]{#2006:1,2007:8,2008:1#}川刑更{SEQ}号";
//        System.out.println(DataUtils.getString(info,"(\\{[^\\}]+\\})"));

//        String qzInfo="2006:1,2007:8,2008:1";
//        List<String> datas=new ArrayList<>();
//        for (String a1:qzInfo.split(",")){
//            String[] a2=a1.split(":");
//            for (int i=0;i<Integer.parseInt(a2[1]);i++){
//                datas.add(a2[0]);
//            }
//        }
//        System.out.println(datas.get(DataUtils.getRanDom(0,datas.size())));
    }
}
