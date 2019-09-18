package pl.kojonek2.forumEE.servlets.topics;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.kojonek2.forumEE.beans.Topic;
import pl.kojonek2.forumEE.beans.User;
import pl.kojonek2.forumEE.enums.Roles;
import pl.kojonek2.forumEE.services.TopicService;

@WebServlet("/deleteTopic")
public class DeleteTopicServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || !user.getRoles().contains(Roles.ADMIN.toString()))
		{
			response.sendError(403, "This page is only for admins!");
			return;
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////
		
		if (request.getParameter("id") == null) {
			response.sendError(400, "Id parameter not found!");
			return;
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////////
		
		TopicService topicService = new TopicService();
		Topic topic;
		int id;
		try {
			id = Integer.parseInt(request.getParameter("id"));
			topic = topicService.readTopic(id);
			
		} catch (NumberFormatException e) {
			response.sendError(400, "Id parameter is not a number!");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Error when communicating with database!");
			return;
		}
		
		if (topic == null) {
			response.sendError(400, "Section with id" + id + " does not exist!");
			return;
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		
		request.setAttribute("topic", topic);
		request.getRequestDispatcher("/WEB-INF/webPages/topics/delete_topic.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || !user.getRoles().contains(Roles.ADMIN.toString()))
		{
			response.sendError(403, "This page is only for admins!");
			return;
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////////
		
		if (request.getParameter("id") == null || request.getParameter("action") == null) {
			response.sendError(400, "Improper parameters!");
			return;
		}
		
		if (request.getParameter("action").equals("cancel")) {
			response.sendRedirect("/");
			return;
		} else if (!request.getParameter("action").equals("delete")) {
			response.sendError(400, "Improper action!");
			return;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		TopicService topicService = new TopicService();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Topic topic = new Topic();
			topic.setId(id);
			topicService.deleteTopic(topic);
			
		} catch (NumberFormatException e) {
			response.sendError(400, "Id parameter is not a number!");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Error when communicating with database!");
			return;
		}
		
		request.setAttribute("successMessage", "Topic was deleted!");
		request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
	}
}
