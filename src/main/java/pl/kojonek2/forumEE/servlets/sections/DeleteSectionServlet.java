package pl.kojonek2.forumEE.servlets.sections;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.kojonek2.forumEE.beans.Section;
import pl.kojonek2.forumEE.beans.User;
import pl.kojonek2.forumEE.enums.Roles;
import pl.kojonek2.forumEE.services.SectionService;

@WebServlet("/deleteSection")
public class DeleteSectionServlet extends HttpServlet {

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
		
		SectionService sectionService = new SectionService();
		Section section;
		int id;
		try {
			id = Integer.parseInt(request.getParameter("id"));
			section = sectionService.readSection(id);
			
		} catch (NumberFormatException e) {
			response.sendError(400, "Id parameter is not a number!");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Error when communicating with database!");
			return;
		}
		
		if (section == null) {
			response.sendError(400, "Section with id" + id + " does not exist!");
			return;
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		
		request.setAttribute("section", section);
		request.getRequestDispatcher("/WEB-INF/webPages/sections/delete_section.jsp").forward(request, response);
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
		
		SectionService sectionService = new SectionService();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			
			Section section = new Section();
			section.setId(id);
			sectionService.deleteSection(section);
			
		} catch (NumberFormatException e) {
			response.sendError(400, "Id parameter is not a number!");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Error when communicating with database!");
			return;
		}
		
		request.setAttribute("successMessage", "Section was deleted!");
		request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
	}
}
