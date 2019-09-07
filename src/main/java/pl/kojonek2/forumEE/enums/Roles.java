package pl.kojonek2.forumEE.enums;

public enum Roles {
	
	GUEST(null),
	USER("user"),
	ADMIN("admin");
	
	private String name;
	
	private Roles(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
