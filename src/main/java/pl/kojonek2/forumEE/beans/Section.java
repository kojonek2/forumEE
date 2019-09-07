package pl.kojonek2.forumEE.beans;

public class Section {

	private int id = -1;
	private String name;
	private String description;
	private String requiredRole;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getRequiredRole() {
		return requiredRole;
	}
	
	public void setRequiredRole(String requiredRole) {
		this.requiredRole = requiredRole;
	}
}
