package pl.kojonek2.forumEE.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.kojonek2.forumEE.beans.User;
import pl.kojonek2.forumEE.services.UserService;
import pl.kojonek2.forumEE.utils.Utils;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/webPages/authentication/register.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserService userService = new UserService();
		
		try {
			User existingUser = userService.readUser(request.getParameter("username"));
			
			if (existingUser != null) {
				request.setAttribute("errorMessage", "User already exists!");	
				request.getRequestDispatcher("/WEB-INF/webPages/authentication/register.jsp").forward(request, response);
				return;
			}
		} catch (SQLException e1) {
			response.sendError(500, "Can't connect to database!");
			return;
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////
		
		String username = request.getParameter("username");
		if (username == null || username.length() <= 0) {
			response.sendError(400, "username parameter is invalid");
			return;
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////
		
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		
		if (password == null || !password.equals(confirmPassword)) {
			request.setAttribute("errorMessage", "Passwords are not the same!");	
			request.getRequestDispatcher("/WEB-INF/webPages/authentication/register.jsp").forward(request, response);
			return;
		}
		
		password = Utils.digestPassword(password);
		if (password == null) {
			response.sendError(500, "Can't digest password"); //should never occur in normal environment
			return;
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////
		
		try {
			userService.createUser(username, password);
			request.setAttribute("sucessMessage", "Account has been registered. You can log in.");	
			request.getRequestDispatcher("/WEB-INF/webPages/authentication/register_sucess.jsp").forward(request, response);
			return;
			
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(500, "Error occured while saving data!");
			return;
		}
	}

}
