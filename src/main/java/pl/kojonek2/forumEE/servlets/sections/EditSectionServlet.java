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

@WebServlet("/editSection")
public class EditSectionServlet extends HttpServlet {

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
		
		SectionService serviceSection = new SectionService();
		Section section;
		
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			section = serviceSection.readSection(id);
		} catch (NumberFormatException e) {
			response.sendError(400, "Id parameter is corrupted!");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
		
		if (section == null) {
			request.setAttribute("errorMessage", "Section wasn't found in database!");
			request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
			return;
		}
		
		request.setAttribute("section", section);
		request.getRequestDispatcher("/WEB-INF/webPages/sections/edit_section.jsp").forward(request, response);
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
		
		if (request.getParameter("id") == null || request.getParameter("name") == null
				|| request.getParameter("description") == null || request.getParameter("role") == null) {
			response.sendError(400, "Id parameter not found!");
			return;
		}
		
		SectionService sectionService = new SectionService();
		Section section;
		
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			section = sectionService.readSection(id);
		} catch (NumberFormatException e) {
			response.sendError(400, "Id parameter is corrupted!");
			return;
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
	
		if (section == null) {
			request.setAttribute("errorMessage", "Section wasn't found in database!");
			request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
			return;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////

		String name = request.getParameter("name");
		String description = request.getParameter("description");
		String role = request.getParameter("role");

		if (name == null || description == null || role == null) {
			response.sendError(400, "Improper parameters!");
			return;
		}

		if (name.length() > 30 || description.length() > 100) {
			response.sendError(400, "Name can't be longer than 30 letters and description can't be longer than 100 letters!");
			return;
		}

		if (role.equals("")) {
			role = null;
		} else if (!role.equals(Roles.USER.toString()) && !role.equals(Roles.ADMIN.toString())) {
			response.sendError(400, "Improper role!");
			return;
		}

		//////////////////////////////////////////////////////////////////////////////////////////////
		
		section.setName(name);
		section.setDescription(description);
		section.setRequiredRole(role);
		
		try {
			sectionService.updateSection(section);
		} catch (SQLException e) {
			response.sendError(500, "Can't communicate properly with database!");
			return;
		}
		
		request.setAttribute("successMessage", "Section was updated!");
		request.getRequestDispatcher("/WEB-INF/webPages/information.jsp").forward(request, response);
	}
}
