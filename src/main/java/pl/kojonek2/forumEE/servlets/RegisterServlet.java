package pl.kojonek2.forumEE.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.kojonek2.forumEE.beans.User;
import pl.kojonek2.forumEE.dao.UserDAO;
import pl.kojonek2.forumEE.enums.Roles;
import pl.kojonek2.forumEE.utils.Utils;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/webPages/register.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserDAO dao = new UserDAO();
		
		try {
			int existingUser = dao.readUserId(request.getParameter("username"));
			
			if (existingUser >= 0) {
				request.setAttribute("errorMessage", "User already exists!");	
				request.getRequestDispatcher("/WEB-INF/webPages/register.jsp").forward(request, response);
				return;
			}
		} catch (SQLException e1) {
			response.sendError(500, "Can't connect to database!");
			return;
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////
		
		User u = new User();
		u.setUsername(request.getParameter("username"));
		
		//////////////////////////////////////////////////////////////////////////////////////////////
		
		List<String> roles = new ArrayList<String>();
		roles.add(Roles.USER.toString());
		u.setRoles(roles);
		
		//////////////////////////////////////////////////////////////////////////////////////////////
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		
		if (password == null || !password.equals(confirmPassword)) {
			request.setAttribute("errorMessage", "Passwords are not the same!");	
			request.getRequestDispatcher("/WEB-INF/webPages/register.jsp").forward(request, response);
			return;
		}
		
		password = Utils.digestPassword(password);
		if (password == null) {
			response.sendError(500, "Can't digest password"); //should never occur in normal environment
			return;
		}
		u.setPassword(password);
		//////////////////////////////////////////////////////////////////////////////////////////
		
		try {
			dao.create(u);
			request.setAttribute("sucessMessage", "Account has been registered. You can log in.");	
			request.getRequestDispatcher("/WEB-INF/webPages/register_sucess.jsp").forward(request, response);
			return;
			
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(500, "Error occured while saving data!");
			return;
		}
	}

}
