package pl.kojonek2.forumEE.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pl.kojonek2.forumEE.beans.User;
import pl.kojonek2.forumEE.dao.UserDAO;
import pl.kojonek2.forumEE.enums.Roles;

public class UserService {
	
	public User createUser(String username, String password) throws SQLException {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		
		List<String> roles = new ArrayList<>();
		roles.add(Roles.USER.toString());
		user.setRoles(roles);
		
		UserDAO userDAO = new UserDAO();
		userDAO.create(user);
		return user;
	}
	
	public User readUser(String username) throws SQLException {
		UserDAO userDAO = new UserDAO();
		int id = userDAO.readUserId(username);
		if (id < 0)
			return null;
		
		return userDAO.read(id);
	}
	
	public User readUser(int id) throws SQLException {
		UserDAO userDAO = new UserDAO();
		return userDAO.read(id);
	}
	
	public boolean updateUser(User user) throws SQLException {
		UserDAO userDAO = new UserDAO();
		return userDAO.update(user);
	}
	
	public boolean deleteUser(User user) throws SQLException {
		UserDAO userDAO = new UserDAO();
		return userDAO.delete(user);
	}
	
}
