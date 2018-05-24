package com.vlad.dao;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.vlad.models.User;

@Component
public class UserDao {

	private static final String QUERY_INSERT = "INSERT INTO Users (id, name, lastName, age) VALUES (?,?,?, ?)";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;


	public void addUsers(List<User> users) {
		
		jdbcTemplate.batchUpdate(QUERY_INSERT, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(java.sql.PreparedStatement ps, int i) throws SQLException {
				User user = users.get(i);
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getLastName());
				ps.setInt(4, user.getAge());

			}

			@Override
			public int getBatchSize() {
				return users.size();
			}
		});
	}
}
