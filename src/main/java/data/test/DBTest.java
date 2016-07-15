package data.test;

import java.io.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.*;

/**
 * Created by gene on 2016/7/5.
 */
public class DBTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        SQLExec sqlExec = new SQLExec();
        //设置数据库参数
        sqlExec.setDriver("org.postgresql.Driver");
        sqlExec.setUrl("jdbc:postgresql://127.0.0.1:5432/abase005?currentSchema=public");
        sqlExec.setUserid("postgres");
        sqlExec.setPassword("123456");
        //要执行的脚本
        sqlExec.setSrc(new File("E:/idea/dataTemplate/resource/sql/1468293679995_T_XS_ZKZM.sql"));
        //有出错的语句该如何处理
        sqlExec.setOnerror((SQLExec.OnError)(EnumeratedAttribute.getInstance(SQLExec.OnError.class, "abort")));
        sqlExec.setPrint(true); //设置是否输出
        //输出到文件 sql.out 中；不设置该属性，默认输出到控制台
        sqlExec.setOutput(new File("resource/sql.out"));
        sqlExec.setProject(new Project()); // 要指定这个属性，不然会出错
        sqlExec.execute();
    }
}
