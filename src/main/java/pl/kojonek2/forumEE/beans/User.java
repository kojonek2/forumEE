package pl.kojonek2.forumEE.beans;

import java.util.ArrayList;
import java.util.List;

import pl.kojonek2.forumEE.enums.Roles;

public class User {

	private int id = -1;
	private String username;
	private String password;
	
	private List<String> roles = new ArrayList<>();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public boolean isAdmin() {
		return roles.contains(Roles.ADMIN.toString());
	}
}
