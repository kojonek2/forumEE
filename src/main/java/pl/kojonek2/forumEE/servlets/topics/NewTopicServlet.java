package pl.kojonek2.forumEE.servlets.topics;

import java.io.IOException;
import java.sql.SQLException;

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

@WebServlet("/newTopic")
public class NewTopicServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null)
		{
			response.sendError(403, "Only logged in users can view this!");
			return;
		}
		
		if (request.getParameter("section") == null) {
			response.sendError(400, "Section parameter not found!");
			return;
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////
		
		SectionService sectionService = new SectionService();
		Section section;
		
		try {
			int sectionId = Integer.parseInt(request.getParameter("section"));
			section = sectionService.readSection(sectionId);
		} catch (NumberFormatException e) {
			response.sendError(400, "Section parameter is corrupted!");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
		
		if (section == null) {
			request.setAttribute("errorMessage", "Section can't be found in database!");
			request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
			return;
		}
		
		if (section.getRequiredRole() != null && !user.getRoles().contains(section.getRequiredRole())) {
			response.sendError(403, "Not authorised for this section!");
			return;
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
		request.setAttribute("section", section);
		request.getRequestDispatcher("/WEB-INF/webPages/topics/new_topic.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null)
		{
			response.sendError(403, "Only loged in users can use this!");
			return;
		}
		
		if (request.getParameter("title") == null || request.getParameter("post") == null || request.getParameter("section") == null) {
			response.sendError(400, "Parameters are not complete!");
			return;
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////
		
		SectionService sectionService = new SectionService();
		Section section;
		
		try {
			int sectionId = Integer.parseInt(request.getParameter("section"));
			section = sectionService.readSection(sectionId);
		} catch (NumberFormatException e) {
			response.sendError(400, "Section parameter is corrupted!");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
		
		if (section == null) {
			request.setAttribute("errorMessage", "Section can't be found in database!");
			request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
			return;
		}
		
		if (section.getRequiredRole() != null && !user.getRoles().contains(section.getRequiredRole())) {
			response.sendError(403, "Not authorised for this section!");
			return;
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String title = request.getParameter("title");
		String post = request.getParameter("post");
		
		if (title.length() > 100 || post.length() > 5000) {
			response.sendError(400, "Title length can't be longer than 100 and post length can't be longer than 5000!");
			return;
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		TopicService topicService = new TopicService();
		Topic topic;
		
		try {
			topic = topicService.createTopic(section, title, user, post);
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
		
		response.sendRedirect("/topic?id=" + topic.getId());
	}
}
