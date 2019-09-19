package pl.kojonek2.forumEE.servlets.topics;

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
import pl.kojonek2.forumEE.enums.Roles;
import pl.kojonek2.forumEE.services.SectionService;
import pl.kojonek2.forumEE.services.TopicService;

@WebServlet("/editTopic")
public class EditTopicServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || !user.getRoles().contains(Roles.ADMIN.toString()))
		{
			response.sendError(403, "This page is only for admins!");
			return;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////
	
		if (request.getParameter("id") == null) {
			response.sendError(400, "Id parameter not found!");
			return;
		}
		
		TopicService topicService = new TopicService();
		Topic topic; 
		
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			topic = topicService.readTopic(id);
		} catch (NumberFormatException e) {
			response.sendError(400, "Id parameter is corrupted!");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
		
		if (topic == null) {
			request.setAttribute("errorMessage", "Topic wasn't found in database!");
			request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
			return;
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		SectionService sectionService = new SectionService();
		List<Section> otherSections;
		
		try {
			otherSections = sectionService.readSections(user);
			otherSections.remove(topic.getSection());
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		request.setAttribute("topic", topic);
		request.setAttribute("otherSections", otherSections);
		request.getRequestDispatcher("/WEB-INF/webPages/topics/edit_topic.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || !user.getRoles().contains(Roles.ADMIN.toString()))
		{
			response.sendError(403, "This page is only for admins!");
			return;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////
	
		if (request.getParameter("id") == null || request.getParameter("title") == null || request.getParameter("section") == null) {
			response.sendError(400, "Parameters are not complete!");
			return;
		}
		
		TopicService topicService = new TopicService();
		Topic topic; 
		
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			topic = topicService.readTopic(id);
		} catch (NumberFormatException e) {
			response.sendError(400, "Id parameter is corrupted!");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
		
		if (topic == null) {
			request.setAttribute("errorMessage", "Topic wasn't found in database!");
			request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
			return;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		SectionService sectionService = new SectionService();
		Section newSection;
		
		try {
			int sectionId = Integer.parseInt(request.getParameter("section"));
			newSection = sectionService.readSection(sectionId);
		} catch (NumberFormatException e) {
			response.sendError(400, "Section parameter is corrupted!");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
		
		if (newSection == null) {
			request.setAttribute("errorMessage", "New section was arleady deleted!");
			request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
			return;
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String title =  request.getParameter("title");
		
		if (title.length() > 100) {
			response.sendError(400, "Title length can't be longer than 100!");
			return;
		}
		
		topic.setSection(newSection);
		topic.setTitle(title);
		
		try {
			topicService.updateTopic(topic);
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
		
		request.setAttribute("successMessage", "Topic was updated!");
		request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
	}
}
