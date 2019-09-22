package pl.kojonek2.forumEE.servlets.topics.posts;

import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.kojonek2.forumEE.beans.Post;
import pl.kojonek2.forumEE.beans.Topic;
import pl.kojonek2.forumEE.beans.User;
import pl.kojonek2.forumEE.services.TopicService;

@WebServlet("/editPost")
public class EditPostServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null)
		{
			response.sendError(403, "Not authorised to edit this post!");
			return;
		}
		
		if (request.getParameter("id") == null  || request.getParameter("topic") == null) {
			response.sendError(400, "Parameters are not complete!");
			return;
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////
		
		TopicService topicService = new TopicService();
		Topic topic;
		Post post;
		
		try {
			int topicId = Integer.parseInt(request.getParameter("topic"));
			int postId = Integer.parseInt(request.getParameter("id"));
			topic = topicService.readTopic(topicId);
			
			if (topic == null) {
				request.setAttribute("errorMessage", "Topic can't be found in database!");
				request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
				return;
			}
			
			post = topic.getPosts().stream().filter(p -> p.getId() == postId).findFirst().get();
				
		} catch (NumberFormatException e) {
			response.sendError(400, "Topic parameter is corrupted!");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		} catch (NoSuchElementException e) {
			request.setAttribute("errorMessage", "Post can't be found in topic!");
			request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
			return;
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		if (!user.isAdmin() && !post.getAuthor().equals(user)) {
			response.sendError(403, "Not authorised to edit this post!");
			return;
		}
		
		request.setAttribute("topic", topic);
		request.setAttribute("postToEdit", post);
		request.getRequestDispatcher("WEB-INF/webPages/topics/posts/edit_post.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null)
		{
			response.sendError(403, "Not authorised to edit this post!");
			return ;
		}
		
		if (request.getParameter("id") == null  || request.getParameter("topic") == null) {
			response.sendError(400, "Parameters are not complete!");
			return ;
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////
		
		TopicService topicService = new TopicService();
		Topic topic;
		Post post;
		
		try {
			int topicId = Integer.parseInt(request.getParameter("topic"));
			int postId = Integer.parseInt(request.getParameter("id"));
			topic = topicService.readTopic(topicId);
			
			if (topic == null) {
				request.setAttribute("errorMessage", "Topic can't be found in database!");
				request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
				return;
			}
			
			post = topic.getPosts().stream().filter(p -> p.getId() == postId).findFirst().get();
				
		} catch (NumberFormatException e) {
			response.sendError(400, "Topic parameter is corrupted!");
			return ;
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return ;
		} catch (NoSuchElementException e) {
			request.setAttribute("errorMessage", "Post can't be found in topic!");
			request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
			return ;
		}
		
		if (!user.isAdmin() && !post.getAuthor().equals(user)) {
			response.sendError(403, "Not authorised to edit this post!");
			return ;
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String postContent = request.getParameter("post");
		if (postContent == null) {
			response.sendError(400, "Parameters are not complete!");
			return;
		}
		
		if (postContent.length() > 5000) {
			response.sendError(400, "Post length can't exceed 5000 characters!");
			return;
		}
		
		post.setContent(postContent);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		
		try {
			topicService.updateTopic(topic);
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
		
		response.sendRedirect("/topic?id=" + topic.getId());
	}
	
}
