package pl.kojonek2.forumEE.listeners;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import pl.kojonek2.forumEE.utils.ConnectionProvider;

@WebListener
public class ContextListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent sce)  { 
    }

    public void contextInitialized(ServletContextEvent sce)  { 
    	//initialize singleton to be sure that it is ready
    	try (Connection conn = ConnectionProvider.getConnection()) {
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
	
}
