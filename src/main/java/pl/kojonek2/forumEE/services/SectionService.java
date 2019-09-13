package pl.kojonek2.forumEE.services;

import java.sql.SQLException;
import java.util.List;

import pl.kojonek2.forumEE.beans.Section;
import pl.kojonek2.forumEE.beans.User;
import pl.kojonek2.forumEE.dao.SectionDAO;

public class SectionService {
	
	public Section createSection(String name, String description, String role) throws SQLException {
		SectionDAO sectionDAO = new SectionDAO();
		
		Section section = new Section();
		section.setName(name);
		section.setDescription(description);
		section.setRequiredRole(role);
		
		sectionDAO.create(section);
		
		return section;
	}
	
	public Section readSection(int id) throws SQLException {
		SectionDAO sectionDAO = new SectionDAO();
		return sectionDAO.read(id);
	}
	
	public List<Section> readSections(User user) throws SQLException {
		SectionDAO sectionDAO = new SectionDAO();
		return sectionDAO.readForUser(user);
	}
	
	public boolean updateSection(Section section) throws SQLException {
		SectionDAO sectionDAO = new SectionDAO();
		return sectionDAO.update(section);
	}
	
	public boolean deleteSection(Section section) throws SQLException {
		SectionDAO sectionDAO = new SectionDAO();
		return sectionDAO.delete(section);
	}
}
