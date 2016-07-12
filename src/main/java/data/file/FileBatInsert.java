package data.file;

import data.core.Table;
import data.utils.Factory;
import data.utils.Params;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.apache.tools.ant.types.EnumeratedAttribute;

import java.io.File;
import java.util.*;

/**
 * Created by wy on 2016/7/8.
 */
public class FileBatInsert {

    /**
     *执行脚本
     */
    public static void fileBatDeal(File file){
        try {
            SQLExec sqlExec = new SQLExec();
            //设置数据库参数
            sqlExec.setDriver(Params.DATA_SOURCE_DRIVER);
            sqlExec.setUrl(Params.DATA_SOURCE_URL);
            sqlExec.setUserid(Params.DATA_SOURCE_USER);
            sqlExec.setPassword(Params.DATA_SOURCE_PASSWORD);
            //要执行的脚本
            sqlExec.setSrc(file);
            //有出错的语句该如何处理
            sqlExec.setOnerror((SQLExec.OnError)(EnumeratedAttribute.getInstance(SQLExec.OnError.class, "abort")));
            sqlExec.setPrint(true); //设置是否输出
            //输出到文件 sql.out 中；不设置该属性，默认输出到控制台
//            sqlExec.setOutput(new File("resource/sql.out"));
            sqlExec.setProject(new Project()); // 要指定这个属性，不然会出错
            sqlExec.execute();
        } catch (Exception e) {
            System.out.print("执行文件："+file.getName()+"出错");
            e.printStackTrace();
        }
    }

    /**
     * 获取该目录下的文件列表
     * @param filePath
     * @return
     */
    public static List<File> getFiles( String filePath )
    {
        File root = new File( filePath );
        File[] files = root.listFiles();
        return Arrays.asList(files);
    }

   /* public static void insertData(Table table,List<Map<String,Object>> list){
        Connection conn = null;
        PreparedStatement ps = null;
        int result[];
        try{
            //获取数据库连接
            conn = Factory.getConn(Params.DATA_SOURCE_IP, Params.DATA_SOURCE_PORT, Params.DATA_SOURCE_USER, Params.DATA_SOURCE_PASSWORD, Params.DATA_SOURCE_DB, Params.DATA_SOURCE_SCHEMA);
//            conn.setAutoCommit(false);
            String sql = getInsertSql(table);
            ps = conn.prepareStatement(sql);
            int count = 0;
            for (int i=0;i<list.size();i++){
                for (int j=0;j<table.getColumns().size();j++){
                    ps.setObject(j+1,list.get(i).get(table.getColumns().get(j).getName()));
                }
                if(++count % batchSize == 0){
                    result = ps.executeBatch();
                    System.out.println("result:" + result);
                }
            }
            result = ps.executeBatch();
            System.out.println("result:" + result);
//            conn.commit();
        }catch (Exception e){
            e.printStackTrace();
            try{
                conn.rollback();
            }catch (Exception e1){
                e1.printStackTrace();
            }

        }finally {
            try{
                conn.close();
                ps.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println(new Date() + "----------线程" + Thread.currentThread().getId() + "----------执行完毕.....");
    }

    private static String getInsertSql(Table table){
        StringBuffer param = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        sb.append("insert into ").append(table.getName()).append("(");
        for (Column column: table.getColumns()){
            sb.append(column.getName()).append(",");
            param.append("?,");
        }
        sb.deleteCharAt(sb.length()-1);
        param.deleteCharAt(param.length()-1);
        sb.append(")").append(" values ").append("(").append(param).append(")");
        return sb.toString();
    }*/
}
