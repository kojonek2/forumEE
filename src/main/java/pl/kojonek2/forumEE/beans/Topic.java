package pl.kojonek2.forumEE.beans;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class Topic {
	
	private int id = -1;
	private String title;
	private List<Post> posts = new ArrayList<>();
	private Section section;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<Post> getPosts() {
		return posts;
	}
	
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}
	
	public String getLastPostTime() {
		if (posts.size() <= 0)
			return "";
		
		Timestamp timestamp = posts.get(posts.size() - 1).getPostedTimestamp();
		return DateFormat.getDateTimeInstance().format(timestamp);
	}
	
	public String getLastPostAuthor() {
		if (posts.size() <= 0)
			return "";
		
		return posts.get(posts.size() - 1).getAuthor().getUsername();
	}
	
	public int getPostCount() {
		return posts.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (id < 0)
			return false;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Topic other = (Topic) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
