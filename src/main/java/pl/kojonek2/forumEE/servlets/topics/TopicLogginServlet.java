package pl.kojonek2.forumEE.servlets.topics;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/topicLoggin")
public class TopicLogginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("id") == null) {
			response.sendError(400, "Id parameter not found!");
			return;
		}
		
		response.sendRedirect("/topic?id=" + request.getParameter("id"));
	}
}
