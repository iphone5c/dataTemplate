package data.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SybaseConn {
	//private static final Logger logger = LoggerFactory.getLogger(SybaseConn.class);
	private static final String className = "org.postgresql.Driver";
	private static final String connFmt ="jdbc:postgresql://%s:%s/%s?currentSchema=%s";
	
	   public static Connection createConn(String ip, String port, String username, String password,String dataDB,String schema) throws SQLException
	    {
            Connection conn = null;
            try {
                Class.forName(className).newInstance();
            } catch (ClassNotFoundException e1) {
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            }
            String url = String.format(connFmt, ip, port,dataDB,schema);
            conn = DriverManager.getConnection(url, username, password);
            return conn;
	    }
}
