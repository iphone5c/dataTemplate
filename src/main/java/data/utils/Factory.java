package data.utils;

import java.sql.Connection;
import java.sql.SQLException;
/**
 * 工厂类，根据数据库的类型确定选择哪一种类来连接数据库。
 *
 */
public class Factory {

	public static Connection getConn(String expip,
			String expport, String expusername, String exppassword,String dataDB,String schema) throws SQLException
	{
		    return SybaseConn.createConn(expip, expport, expusername, exppassword,dataDB,schema);
	}

}
