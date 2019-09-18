package pl.kojonek2.forumEE.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import pl.kojonek2.forumEE.beans.Post;
import pl.kojonek2.forumEE.beans.Section;
import pl.kojonek2.forumEE.beans.Topic;
import pl.kojonek2.forumEE.beans.User;
import pl.kojonek2.forumEE.utils.ConnectionProvider;

public class TopicDAO {

	public boolean create(Topic topic) throws SQLException {
		final String CREATE = "INSERT INTO topics(title, section_id) VALUES (?, ?);";
		
		try (Connection connection = ConnectionProvider.getConnection()) {
			connection.setAutoCommit(false);
			
			try (PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS)) {
				
				statement.setString(1, topic.getTitle());
				statement.setInt(2, topic.getSection().getId());
				statement.executeUpdate();
				
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next())
						topic.setId(generatedKeys.getInt("id"));
					else 
						throw new SQLException("No generated id was returned");
				}
				
				createPosts(topic.getId(), topic.getPosts(), connection);
				
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
	
	private void createPosts(int topicId, List<Post> postsToCreate, Connection connection) throws SQLException {
		final String POST_CREATE = "INSERT INTO posts(author_id, posted_timestamp, content, topic_id) VALUES (?, ?, ?, ?);";
		try (PreparedStatement statement = connection.prepareStatement(POST_CREATE, PreparedStatement.RETURN_GENERATED_KEYS)) {
			
			statement.setInt(4, topicId);
			for (Post post : postsToCreate) {
				statement.setInt(1, post.getAuthor().getId());
				statement.setTimestamp(2, post.getPostedTimestamp());
				statement.setString(3, post.getContent());
				statement.addBatch();
			}
			
			statement.executeBatch();
			
			Iterator<Post> posts = postsToCreate.iterator();
			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				while (generatedKeys.next()) {
					if (!posts.hasNext())
						throw new SQLException("Generated more keys for posts than database should!");
					
					Post post = posts.next(); 
					post.setId(generatedKeys.getInt("id"));
				}
					
				if (posts.hasNext())
					throw new SQLException("Generated less keys for posts than databae should!");
			}
		}
	}
	
	public Topic read(int id) throws SQLException {
		final String READ = "SELECT topics.title, sections.id, sections.description, sections.name, sections.required_role FROM topics LEFT JOIN sections ON topics.section_id = sections.id WHERE topics.id=?;";
		Topic topic = new Topic();
		
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement statement = connection.prepareStatement(READ)) {
			statement.setInt(1, id);
			
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					topic.setId(id);
					topic.setTitle(resultSet.getString("title"));
					
					Section section = new Section(); 
					section.setId(resultSet.getInt("id"));
					section.setName(resultSet.getString("name"));
					section.setDescription(resultSet.getString("description"));
					section.setRequiredRole(resultSet.getString("required_role"));
					topic.setSection(section);
				} else 
					return null; //no topic found
			}
			
			topic.setPosts(readPosts(topic.getId(), connection, null));
		}
		
		return topic;
	}
	
	public List<Topic> read(Section section) throws SQLException {
		final String READ = "SELECT id, title FROM topics WHERE section_id=? ORDER BY id;";
		
		List<Topic> topics = new ArrayList<>();
		
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement statement = connection.prepareStatement(READ)) {
			statement.setInt(1, section.getId());
			
			try (ResultSet resultSet = statement.executeQuery()) {
				Hashtable<Integer, User> userCache = new Hashtable<>();
				
				while (resultSet.next()) {
					Topic topic = new Topic();
					topic.setId(resultSet.getInt("id"));
					topic.setTitle(resultSet.getString("title"));
					topic.setSection(section);
					
					topic.setPosts(readPosts(topic.getId(), connection, userCache));
					
					topics.add(topic);
				} 
			}
		}
		
		return topics;
	}
	
	private List<Post> readPosts(int topicID, Connection connection, Hashtable<Integer, User> userCache) throws SQLException {
		final String POST_READ = "SELECT posts.id, posts.posted_timestamp, posts.content, users.id user_id, users.username, users.password FROM posts LEFT JOIN users ON users.id = posts.author_id WHERE topic_id=? ORDER BY posts.posted_timestamp;";
		
		List<Post> posts = new ArrayList<>();
		
		if (userCache == null)
			userCache = new Hashtable<Integer, User>();
		
		try (PreparedStatement statement = connection.prepareStatement(POST_READ)) {
			statement.setInt(1, topicID);
			
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Post post = new Post();
					post.setId(resultSet.getInt("id"));
					post.setContent(resultSet.getString("content"));
					post.setPostedTimestamp(resultSet.getTimestamp("posted_timestamp"));
					
					int authorID = resultSet.getInt("user_id");
					User author;
					if (!userCache.containsKey(authorID)) {
						author = new User();
						author.setId(authorID);
						author.setUsername(resultSet.getString("username"));
						author.setPassword(resultSet.getString("password"));
						
						userCache.put(authorID, author);
					} else {
						author = userCache.get(authorID);
					}
					
					post.setAuthor(author);
					posts.add(post);
				}
					
			}
		}
		
		return posts;
	}
	
	public boolean update(Topic topic) throws SQLException {
		final String UPDATE = "UPDATE topics SET title=?, section_id=? WHERE id=?;";
		
		try (Connection connection = ConnectionProvider.getConnection()) {
			connection.setAutoCommit(false);
			
			try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
				statement.setString(1, topic.getTitle());
				statement.setInt(2, topic.getSection().getId());
				statement.setInt(3, topic.getId());
				
				int rowsAffected = statement.executeUpdate();
				if (rowsAffected <= 0)
					return false;
				
				updatePosts(topic, connection);
				
				return true;
				
			}  catch (SQLException e) {
				connection.rollback();
				throw e;
			} finally {
				connection.setAutoCommit(true);
			}
		}
	}
	
	private void updatePosts(Topic topic, Connection connection) throws SQLException {
		final String UPDATE_POST = "UPDATE posts SET author_id=?, posted_timestamp=?, content=? WHERE id=?;";
		
		List<Post> postsInDatabase = readPosts(topic.getId(), connection, null);
		List<Post> postsToDelete = postsInDatabase.stream().filter((post) -> !topic.getPosts().contains(post)).collect(Collectors.toList());
		List<Post> postsToAdd = topic.getPosts().stream().filter((post) -> !postsInDatabase.contains(post)).collect(Collectors.toList());
		List<Post> postsToUpdate = topic.getPosts().stream()
				.filter((post) -> {
					int indexInDatabaseList = postsInDatabase.indexOf(post);
					if (indexInDatabaseList < 0)
						return false;
					
					Post postInDatabase = postsInDatabase.get(indexInDatabaseList);
					if (!post.getAuthor().equals(postInDatabase.getAuthor()))
						return true;
					if (!post.getContent().equals(postInDatabase.getContent()))
						return true;
					if (!post.getPostedTimestamp().equals(postInDatabase.getPostedTimestamp()))
						return true;
						
					return false;
				}).collect(Collectors.toList());
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		
		try (PreparedStatement statement = connection.prepareStatement(UPDATE_POST)) {
			for (Post post : postsToUpdate) {
				statement.setInt(1, post.getAuthor().getId());
				statement.setTimestamp(2, post.getPostedTimestamp());
				statement.setString(3, post.getContent());
				statement.setInt(4, post.getId());
				System.out.println("UPDATE");
				statement.addBatch();
			}
			
			statement.executeBatch();
		}
		
		createPosts(topic.getId(), postsToAdd, connection);
		deletePosts(postsToDelete, connection);
	}
	
	private void deletePosts(List<Post> postsToDelete, Connection connection) throws SQLException {
		final String DELETE_POST = "DELETE FROM posts WHERE id=?;";
		
		try (PreparedStatement statement = connection.prepareStatement(DELETE_POST)) {
			for (Post post : postsToDelete) {
				statement.setInt(1, post.getId());
				statement.addBatch();
			}
			
			statement.executeBatch();
		}
	}
	
	public boolean delete(Topic topic) throws SQLException {
		final String DELETE = "DELETE FROM topics WHERE id=?;";
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE)) {
			
			statement.setInt(1, topic.getId());
			int affectedRows = statement.executeUpdate();
			if (affectedRows <= 0)
				return false;
			
			return true;
		}
	}
}
