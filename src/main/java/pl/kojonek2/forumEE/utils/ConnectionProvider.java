package pl.kojonek2.forumEE.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionProvider {

	private static DataSource dataSource;
	
	public static Connection getConnection() throws SQLException {
		if (dataSource == null) 
			initializeDataSource();
		
		return dataSource.getConnection();
	}
	
	private static void initializeDataSource() {
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			dataSource = (DataSource)envContext.lookup("jdbc/postgres");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}
}
