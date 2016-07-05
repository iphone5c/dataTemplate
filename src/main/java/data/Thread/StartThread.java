package data.Thread;

import data.core.ApplicationContext;
import data.core.ColunmDataService;
import data.core.Table;

/**
 * Created by wy on 2016/7/5.
 */
public class StartThread implements Runnable {

    private Table table;
    private ApplicationContext applicationContext;
    private ColunmDataService colunmDataService;
    private int num;

    public StartThread(Table table, int num ,ApplicationContext applicationContext) {
        this.table = table;
        this.applicationContext = applicationContext;
        this.colunmDataService = applicationContext.getColunmDataService();
        this.num = num;
    }

    @Override
    public void run() {
        colunmDataService.getTableData(table,num,null,applicationContext);
    }
}
