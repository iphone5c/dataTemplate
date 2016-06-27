package data.test;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by gene on 2016/6/27.
 */
public class TestSql {

    public static void insertData(Connection conn) throws Exception{
        Statement st = null;
        try{
            conn.setAutoCommit(false);
            String sql="insert into t_aty_cfgid (c_key,n_value,n_fldlength) values ('1','1','1')";
            st = conn.createStatement();
            st.addBatch(sql);
            st.executeBatch();
            conn.commit();
        }catch (Exception e){
            conn.rollback();
        }finally {
            st.close();
            conn.close();
        }
    }
}
