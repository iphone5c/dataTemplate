package data.jdbc;

import data.core.Column;
import data.core.Table;
import data.utils.Factory;
import data.utils.Params;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by wy on 2016/7/8.
 */
public class JdbcBatInsert {
    private static Integer batchSize = 10000;

    public static void insertData(Table table,List<Map<String,Object>> list){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            //获取数据库连接
            conn = Factory.getConn(Params.DATA_SOURCE_IP, Params.DATA_SOURCE_PORT, Params.DATA_SOURCE_USER, Params.DATA_SOURCE_PASSWORD, Params.DATA_SOURCE_DB, Params.DATA_SOURCE_SCHEMA);
            conn.setAutoCommit(false);
            String sql = getInsertSql(table);
            ps = conn.prepareStatement(sql);
            int count = 0;
            for (int i=0;i<list.size();i++){
                for (int j=0;j<table.getColumns().size();j++){
                    ps.setObject(j+1,list.get(i).get(table.getColumns().get(j).getName()));
                }
                if(++count % batchSize == 0){
                    ps.executeBatch();
                }
            }
            ps.executeBatch();
            System.out.println(ps);
            conn.commit();
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
    }
}
