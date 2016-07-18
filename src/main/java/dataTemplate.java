import data.Thread.StartMain;
import data.core.ApplicationContext;

/**
 * Created by wy on 2016/7/18.
 */
public class dataTemplate {
    public static void main(String args[]){
        //初始化应用上下文
        ApplicationContext applicationContext=new ApplicationContext();
        StartMain.start(10, applicationContext);
    }
}
