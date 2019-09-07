package pl.kojonek2.forumEE.filters;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import pl.kojonek2.forumEE.beans.User;
import pl.kojonek2.forumEE.dao.UserDAO;

@WebFilter("/*")
public class SessionUserFilter implements Filter {

	@Override
	public void destroy() { }
	
	@Override
	public void init(FilterConfig fConfig) throws ServletException { }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		if(httpRequest.getUserPrincipal() != null && httpRequest.getSession().getAttribute("user") == null) {
			UserDAO dao = new UserDAO();
			try {
				int userID = dao.readUserId(httpRequest.getUserPrincipal().getName());
				User user = dao.read(userID);
				
				if (user == null) {
					logOutUser(httpRequest, response);
					return;
				}
				
				httpRequest.getSession().setAttribute("user", user);
			} catch (SQLException e) {
				logOutUser(httpRequest, response);
				return;
			}
		}
		
		chain.doFilter(request, response);
	}
	
	private void logOutUser(HttpServletRequest httpRequest, ServletResponse response) throws ServletException, IOException
	{
		httpRequest.getSession().invalidate(); //log out user
		httpRequest.setAttribute("errorMessage", "Error occured! You have been loged out!");
		httpRequest.getRequestDispatcher("/WEB-INF/webPages/login.jsp").forward(httpRequest, response);
	}

}
