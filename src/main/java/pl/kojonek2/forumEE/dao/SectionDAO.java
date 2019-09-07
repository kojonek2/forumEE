package pl.kojonek2.forumEE.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import pl.kojonek2.forumEE.beans.Section;
import pl.kojonek2.forumEE.beans.User;
import pl.kojonek2.forumEE.utils.ConnectionProvider;

public class SectionDAO {
	
	public boolean create(Section section) throws SQLException {
		final String CREATE = "INSERT INTO sections(name, description, required_role) VALUES (?, ?, ?);";
		
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS)) {
			
				statement.setString(1, section.getName());
				statement.setString(2, section.getDescription());
				statement.setString(3, section.getRequiredRole());
				statement.executeUpdate();
				
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next())
						section.setId(generatedKeys.getInt("id"));
					else 
						throw new SQLException("No generated id was returned");
				}
				
				return true;
		}
	}
	
	public Section read(int id) throws SQLException {
		final String READ = "SELECT name, description, required_role FROM sections WHERE id=?;";
		Section section = new Section();
		
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement statement = connection.prepareStatement(READ)) {
			statement.setInt(1, id);
			
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					section.setId(id);
					section.setName(resultSet.getString("name"));
					section.setDescription(resultSet.getString("description"));
					section.setRequiredRole(resultSet.getString("required_role"));
				} else 
					return null; //no section found
			}
		}
		
		return section;
	}
	
	public List<Section> readForUser(User user) throws SQLException {
		List<Section> sections = new LinkedList<Section>();
		
		StringBuilder builder = new StringBuilder("SELECT id, name, description, required_role FROM sections WHERE required_role is null");
		if (user != null)
		{
			List<String> roles = user.getRoles();
			
			builder.append(" OR required_role IN ('");
			for (int i = 0; i < roles.size(); i++) {
				
				builder.append(roles.get(i));
				
				if (i != roles.size() - 1)
					builder.append("', '");
				else
					builder.append("')");
			}
		}
		builder.append(";");
		
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement statement = connection.prepareStatement(builder.toString())) {
			
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Section section = new Section();
					section.setId(resultSet.getInt("id"));
					section.setName(resultSet.getString("name"));
					section.setDescription(resultSet.getString("description"));
					section.setRequiredRole(resultSet.getString("required_role"));
					
					sections.add(section);
				}
			}
		}
		
		return sections;
	}
	
	public boolean update(Section section) throws SQLException {
		final String UPDATE = "UPDATE sections SET name=?, description=?, required_role=? WHERE id=?;";
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE)) {
			
			statement.setString(1, section.getName());
			statement.setString(2, section.getDescription());
			statement.setString(3, section.getRequiredRole());
			statement.setInt(4, section.getId());
			
			int rowsAffected = statement.executeUpdate();
			if (rowsAffected <= 0)
				return false;
			
			return true;
		}
	}
	
	public boolean delete(Section section) throws SQLException {
		final String DELETE = "DELETE FROM sections WHERE id=?;";
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE)) {
			
			statement.setInt(1, section.getId());
			int affectedRows = statement.executeUpdate();
			if (affectedRows <= 0)
				return false;
			
			return true;
		}
	}
}
