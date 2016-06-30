package data.dbcache;

import data.utils.Factory;
import data.utils.Params;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

/**
 * Created by gene on 2016/6/30.
 */
public class DataCache {

    public static List<Map<String,Object>> getDataList(String sql){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            //获取数据库连接
            conn = Factory.getConn(Params.DATA_SOURCE_IP, Params.DATA_SOURCE_PORT, Params.DATA_SOURCE_USER, Params.DATA_SOURCE_PASSWORD, Params.DATA_SOURCE_DB, Params.DATA_SOURCE_SCHEMA);
            ps = conn.prepareStatement(sql);
            ResultSet set = ps.executeQuery();
            return resultSetToList(set);
        }catch (Exception e){

        }finally {
            try{
                conn.close();
                ps.close();
            }catch (Exception e){
            }
        }
        return null;
    }

    public static List resultSetToList(ResultSet rs) throws java.sql.SQLException {
        if (rs == null)
            return null;
        ResultSetMetaData md = rs.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等
        int columnCount = md.getColumnCount(); //返回此 ResultSet 对象中的列数
        List list = new ArrayList();
        Map rowData = new HashMap();
        while (rs.next()) {
            rowData = new HashMap(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }
}
