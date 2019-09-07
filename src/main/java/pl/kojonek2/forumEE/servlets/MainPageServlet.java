package pl.kojonek2.forumEE.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.kojonek2.forumEE.beans.Section;
import pl.kojonek2.forumEE.beans.User;
import pl.kojonek2.forumEE.dao.SectionDAO;

@WebServlet("/mainPage")
public class MainPageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SectionDAO dao = new SectionDAO();
		List<Section> sections;
		
		try {
			sections = dao.readForUser((User) request.getSession().getAttribute("user"));
		} catch (SQLException e) {
			response.sendError(500, "Can't read data from database!");
			return;
		}
		
		request.setAttribute("sections", sections);
		
		//////////////////////////////////////////////////////////
		
		request.getRequestDispatcher("/WEB-INF/webPages/index.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request, response);
	}
}
