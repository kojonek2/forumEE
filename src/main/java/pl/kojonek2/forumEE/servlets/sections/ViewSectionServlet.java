package pl.kojonek2.forumEE.servlets.sections;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.kojonek2.forumEE.beans.Section;
import pl.kojonek2.forumEE.beans.Topic;
import pl.kojonek2.forumEE.beans.User;
import pl.kojonek2.forumEE.services.SectionService;
import pl.kojonek2.forumEE.services.TopicService;

@WebServlet("/section")
public class ViewSectionServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("id") == null) {
			response.sendError(400, "Id parameter not found!");
			return;
		}
		
		Section section;
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			section = new SectionService().readSection(id);
		} catch (NumberFormatException e) {
			response.sendError(400, "Id was not an int");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate with database!");
			return;
		}
		
		if (section == null) {
			request.setAttribute("errorMessage", "Section not found!");
			request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
			return;
		}
		
		User user = (User) request.getSession().getAttribute("user");
		if (section.getRequiredRole() != null && user != null && !user.getRoles().contains(section.getRequiredRole())) {
			request.setAttribute("errorMessage", "You are not authorised to view this section!");
			request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
			return;
		}
		
		if (section.getRequiredRole() != null && user == null) {
			response.sendRedirect("/sectionLogin?id=" + section.getId());
			return;
		}
		
		List<Topic> topics;
		try {
			topics = new TopicService().readTopicsForSection(section);
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate with database!");
			return;
		}
		
		request.setAttribute("topics", topics);
		request.setAttribute("section", section);
		request.getRequestDispatcher("/WEB-INF/webPages/sections/view_section.jsp").forward(request, response);
		return;
	}

}
