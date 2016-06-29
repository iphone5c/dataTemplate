package data.test;
import data.utils.Factory;
import data.utils.Params;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class MultCalc implements Callable<String> {

    private List<Map<String,Object>> list;
    private int thread;
    private static int batchSize = 1000;

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    @Override
    public String call(){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            //获取数据库连接
            conn = Factory.getConn(Params.DATA_SOURCE_IP, Params.DATA_SOURCE_PORT, Params.DATA_SOURCE_USER, Params.DATA_SOURCE_PASSWORD, Params.DATA_SOURCE_DB, Params.DATA_SOURCE_SCHEMA);
            conn.setAutoCommit(false);
            String sql = "insert into t_aty_cfgid (c_key,n_value,n_fldlength) values (?,?,? )";
            ps = conn.prepareStatement(sql);
            int count = 0;
            for(Map<String,Object> map : list){
                ps.setObject(1,map.get("c_key"));
                ps.setObject(2,map.get("n_value"));
                ps.setObject(3,map.get("n_fldlength"));
                ps.addBatch();
                if(++count % batchSize == 0){
                    ps.executeBatch();
                }
            }
            ps.executeBatch();
            conn.commit();
        }catch (Exception e){
            try{
                conn.rollback();
            }catch (Exception e1){
            }
        }finally {
            try{
                conn.close();
                ps.close();
            }catch (Exception e){
            }
        }
        System.out.println(new Date() + "----------线程" + thread + "----------执行完毕.....");
        return null;
    }
}
