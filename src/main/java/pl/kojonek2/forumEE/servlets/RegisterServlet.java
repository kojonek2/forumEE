package pl.kojonek2.forumEE.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.kojonek2.forumEE.beans.User;
import pl.kojonek2.forumEE.dao.UserDAO;

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
//		User u = new User();
//		u.setPassword(request.getParameter("password"));
//		u.setUsername(request.getParameter("username"));
//		List<String> list = new ArrayList<String>();
//		list.add("role2");
//		list.add("role1");
//		
//		u.setRoles(list);
//		try {
//			dao.create(u);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
//		try {
//			User u = new User();
//			u.setId(4);
//			dao.delete(u);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
		try {
			User u = dao.read(1);
			List<String> roles = u.getRoles();
			roles.add("testowa");
			roles.add("testowaKolejna");
			roles.remove("user");
			dao.update(u);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
//		try {
//			User user = dao.read("gagasd");
//			System.out.println(user.getRoles().size());
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}

}
