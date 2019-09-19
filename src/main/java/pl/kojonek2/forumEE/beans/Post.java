package pl.kojonek2.forumEE.beans;

import java.sql.Timestamp;
import java.text.DateFormat;

public class Post {

	private int id = -1;
	private User author;
	private Timestamp postedTimestamp;
	private String content;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Timestamp getPostedTimestamp() {
		return postedTimestamp;
	}

	public void setPostedTimestamp(Timestamp postedTimestamp) {
		this.postedTimestamp = postedTimestamp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getPostTime() {
		return DateFormat.getDateTimeInstance().format(postedTimestamp);
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
		Post other = (Post) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
