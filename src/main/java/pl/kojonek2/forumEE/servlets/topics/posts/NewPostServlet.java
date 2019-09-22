package pl.kojonek2.forumEE.servlets.topics.posts;

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

@WebServlet("/newPost")
public class NewPostServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || !user.getRoles().contains(Roles.USER.toString()))
		{
			response.sendError(403, "Not authorised to create new post!");
			return;
		}
		
		if (request.getParameter("topic") == null) {
			response.sendError(400, "Parameters are not complete!");
			return;
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////
		
		TopicService topicService = new TopicService();
		Topic topic;
		
		try {
			int topicId = Integer.parseInt(request.getParameter("topic"));
			topic = topicService.readTopic(topicId);
		} catch (NumberFormatException e) {
			response.sendError(400, "Topic parameter is corrupted!");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
		
		request.setAttribute("topic", topic);
		request.getRequestDispatcher("WEB-INF/webPages/topics/posts/new_post.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || !user.getRoles().contains(Roles.USER.toString()))
		{
			response.sendError(403, "Not authorised to create new post!");
			return;
		}
		
		if (request.getParameter("topic") == null || request.getParameter("post") == null) {
			response.sendError(400, "Parameters are not complete!");
			return;
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////
		
		TopicService topicService = new TopicService();
		Topic topic;
		
		try {
			int topicId = Integer.parseInt(request.getParameter("topic"));
			topic = topicService.readTopic(topicId);
		} catch (NumberFormatException e) {
			response.sendError(400, "Topic parameter is corrupted!");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////
		String post = request.getParameter("post");
		
		if (post.length() > 5000) {
			response.sendError(400, "Length of post can't be longer than 5000!");
			return;
		}
		
		try {
			topicService.addPost(topic, post, user);
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
		
		response.sendRedirect("/topic?id=" + topic.getId() + "#last");
	}
}
