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
import pl.kojonek2.forumEE.services.TopicService;

@WebServlet("/topic")
public class ViewTopicServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		
		if (request.getParameter("id") == null) {
			response.sendError(400, "Id parameter not found!");
			return;
		}

		//////////////////////////////////////////////////////////////////////////////////////////////
		
		TopicService topicService = new TopicService();
		Topic topic;
		
		try {
			int topicId = Integer.parseInt(request.getParameter("id"));
			topic = topicService.readTopic(topicId);
		} catch (NumberFormatException e) {
			response.sendError(400, "Id parameter is corrupted!");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
		
		if (topic == null) {
			request.setAttribute("errorMessage", "Topic can't be found in database!");
			request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
			return;
		}
		
		if (topic.getSection().getRequiredRole() != null) {
			if (user == null) {
				response.sendRedirect("/topicLoggin?id=" + topic.getId());
				return;
			}
			
			if (!user.getRoles().contains(topic.getSection().getRequiredRole())) {
				response.sendError(403, "Not authorised for this section!");
				return;
			}
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		request.setAttribute("topic", topic);
		request.getRequestDispatcher("WEB-INF/webPages/topics/view_topic.jsp").forward(request, response);
	}
}
