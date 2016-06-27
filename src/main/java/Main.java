import data.core.ApplicationContext;
import data.core.Table;
import data.core.XmlDatasFactory;
import data.utils.Factory;
import data.utils.Params;

import java.sql.Connection;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        //初始化应用上下文
        ApplicationContext applicationContext=new ApplicationContext();
        //解析xml模版
        List<Table> tableList= XmlDatasFactory.getAllTableList("templateConfig.xml");
        //todo 根据模版生成数据

        //获取数据库连接
        Connection connection = Factory.getConn(Params.DATA_SOURCE_IP, Params.DATA_SOURCE_PORT, Params.DATA_SOURCE_USER, Params.DATA_SOURCE_PASSWORD, Params.DATA_SOURCE_DB);
        //todo 插入数据
        System.out.println(tableList);
    }
}
