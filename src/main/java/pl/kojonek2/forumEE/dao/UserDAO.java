package pl.kojonek2.forumEE.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pl.kojonek2.forumEE.beans.User;
import pl.kojonek2.forumEE.utils.ConnectionProvider;

public class UserDAO {
	
	public boolean create(User user) throws SQLException {
		final String CREATE = "INSERT INTO users(username, password) VALUES (?, ?);";
				
		try (Connection connection = ConnectionProvider.getConnection()) {
			connection.setAutoCommit(false);
			
			try (PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS)) {
				
				statement.setString(1, user.getUsername());
				statement.setString(2, user.getPassword());
				statement.executeUpdate();
				
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next())
						user.setId(generatedKeys.getInt("id"));
					else throw new SQLException("No generated id was returned");
				}
				
				createRoles(user, connection);
				
				connection.commit();
				return true;
			} catch (SQLException e) {
				connection.rollback();
				throw e;
			} finally {
				connection.setAutoCommit(true);
			}
			
		}
	}
	
	private void createRoles(User user, Connection conneciton) throws SQLException {
		final String ROLE_CREATE = "INSERT INTO user_roles(username, rolename) VALUES (?, ?);";
		try (PreparedStatement statement = conneciton.prepareStatement(ROLE_CREATE)) {
			
			statement.setString(1, user.getUsername());
			for (String role : user.getRoles()) {
				statement.setString(2, role);
				statement.addBatch();
			}
			
			statement.executeBatch();
		}
	}
	
	public User read(int id) throws SQLException {
		final String READ = "SELECT username, password FROM users WHERE id=?;";
		User user = new User();
		
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement statement = connection.prepareStatement(READ)) {
			statement.setInt(1, id);
			
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					user.setId(id);
					user.setUsername(resultSet.getString("username"));
					user.setPassword(resultSet.getString("password"));
				} else 
					return null; //no user found
			}
			
			user.setRoles(readRoles(user.getId(), connection));
		}
		
		return user;
	}
	
	private List<String> readRoles(int id, Connection connection) throws SQLException {
		final String ROLE_READ = "SELECT rolename FROM user_roles WHERE username=(SELECT username FROM users WHERE id=?);";
		List<String> roles = new ArrayList<>();
		
		try (PreparedStatement statement = connection.prepareStatement(ROLE_READ)) {
			statement.setInt(1, id);
			
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) 
					roles.add(resultSet.getString("rolename"));
			}
		}
		
		return roles;
	}
	
	public boolean update(User user) throws SQLException {
		final String UPDATE = "UPDATE users SET username=?, password=? WHERE id=?;";
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE)) {
			
			List<String> rolesInDatabase = readRoles(user.getId(), connection);
			List<String> rolesToDelete = rolesInDatabase.stream().filter((role) -> !user.getRoles().contains(role)).collect(Collectors.toList());
			List<String> rolesToAdd = user.getRoles().stream().filter((role) -> !rolesInDatabase.contains(role)).collect(Collectors.toList());
			
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPassword());
			statement.setInt(3, user.getId());
			
			int rowsAffected = statement.executeUpdate();
			if (rowsAffected <= 0)
				return false;
			
			updateRoles(user.getUsername(), rolesToDelete, rolesToAdd, connection);
			
			return true;
		}
	}
	
	private void updateRoles(String username, List<String> rolesToDelete, List<String> rolesToAdd, Connection connection) throws SQLException {
		final String INSERT_ROLE = "INSERT INTO user_roles(username, rolename) VALUES (?, ?);";
		final String DELETE_ROLE = "DELETE FROM user_roles WHERE username=? AND rolename=?;";
		
		if (rolesToDelete.size() > 0) {
			try (PreparedStatement statement = connection.prepareStatement(DELETE_ROLE)) {
				statement.setString(1, username);
				for (String role : rolesToDelete) {
					statement.setString(2, role);
					statement.addBatch();
				}
				statement.executeBatch();
			}
		}
		
		if (rolesToAdd.size() > 0) {
			try (PreparedStatement statement = connection.prepareStatement(INSERT_ROLE)) {
				statement.setString(1, username);
				for (String role : rolesToAdd) {
					statement.setString(2, role);
					statement.addBatch();
				}
				statement.executeBatch();
			}
		}
	}
	
	public boolean delete(User user) throws SQLException {
		final String DELETE = "DELETE FROM users WHERE id=?;";
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE)) {
			
			statement.setInt(1, user.getId());
			int affectedRows = statement.executeUpdate();
			if (affectedRows <= 0)
				return false;
			
			return true;
		}
	}
	
}
