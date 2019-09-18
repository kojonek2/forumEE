package pl.kojonek2.forumEE.services;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import pl.kojonek2.forumEE.beans.Post;
import pl.kojonek2.forumEE.beans.Section;
import pl.kojonek2.forumEE.beans.Topic;
import pl.kojonek2.forumEE.beans.User;
import pl.kojonek2.forumEE.dao.TopicDAO;

public class TopicService {

	public Topic createTopic(Section section, String title, User creator, String firstPostContent) throws SQLException {
		if (section == null || creator == null) {
			System.err.println("tried to create topic without assigned section or author of first post!");
			return null;
		}
		
		Post firstPost = new Post();
		firstPost.setAuthor(creator);
		firstPost.setContent(firstPostContent);
		firstPost.setPostedTimestamp(new Timestamp(System.currentTimeMillis()));
		
		List<Post> posts = new ArrayList<>();
		posts.add(firstPost);
		
		Topic topic = new Topic();
		topic.setSection(section);
		topic.setTitle(title);
		topic.setPosts(posts);
		
		TopicDAO topicDAO = new TopicDAO();
		topicDAO.create(topic);
		
		return topic;
	}
	
	public Topic readTopic(int id) throws SQLException {
		TopicDAO topicDAO = new TopicDAO();
		return topicDAO.read(id);
	}
	
	public List<Topic> readTopicsForSection(Section section) throws SQLException {
		if (section == null) {
			System.err.println("Can't read topics for null section!");
			return new ArrayList<Topic>();
		}
		
		TopicDAO topicDAO = new TopicDAO();
		return topicDAO.read(section);
	}
	
	public boolean updateTopic(Topic topic) throws SQLException {
		TopicDAO topicDAO = new TopicDAO();
		return topicDAO.update(topic);
	}
	
	public boolean deleteTopic(Topic topic) throws SQLException {
		TopicDAO topicDAO = new TopicDAO();
		return topicDAO.delete(topic);
	}
}
