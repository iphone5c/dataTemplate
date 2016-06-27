import data.core.Table;
import data.core.XmlDatasFactory;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        List<Table> tableList= XmlDatasFactory.getAllTableList("E:\\dataTemplate\\src\\main\\resources\\templateConfig.xml");
        System.out.println(tableList);
    }
}
