package edu.sjsu.cloudbread.dao;

import java.util.List;

import edu.sjsu.cloudbread.model.User;

public interface UserDao {

	User getUserByNameAndPassowrd(String userName, String password) throws Exception;

	void signUpUser(User user) throws Exception;

	User getUserById(String id);
	
	User getUserByUserName(String userName) throws Exception;

	List<User> getPredictionEnabledUsers();

	List<User> getUserByRole(String role) throws Exception;
}
