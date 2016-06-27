import data.core.Table;
import data.core.XmlDatasFactory;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        List<Table> tableList= XmlDatasFactory.getAllTableList("templateConfig.xml");
        System.out.println(tableList);
    }
}
