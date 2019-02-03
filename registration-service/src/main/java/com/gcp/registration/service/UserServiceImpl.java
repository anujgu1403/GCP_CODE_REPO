package com.gcp.registration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gcp.registration.domain.User;
import com.gcp.registration.exceptions.LogonIdException;
import com.gcp.registration.repository.UserRepository;

/**
 * @author Anuj Kumar
 * 
 *         This class is service layer having implementation of UserService
 *         interface
 */
@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	protected UserRepository userRepository;

	/**
	 * This method is having implementation of registerUser method to interact with
	 * DB using user repository
	 * 
	 * @param user
	 * @return User
	 */
	@Override
	public User registerUser(User user) {
		logger.info("Start registerUser method:", UserServiceImpl.class.getName());
		logger.debug("User request: ", user.toString());
		try {
			//To set the logonId in upper case
			user.setLogonId(user.getLogonId().toUpperCase());
			return userRepository.save(user);
		} catch (Exception ex) {
			logger.error("Exception: ", ex.getMessage());
			throw new LogonIdException("User with logonId " + user.getLogonId() + " already exists.");			
		}
	}
	
//	public User findUserByLogonId(String logonId) {
//		logger.info("Start findUserByLogonId method:", UserServiceImpl.class.getName());
//		logger.debug("LogonId :", logonId);
//		
//		User user = userRepository.findUserByLogonId(logonId);
//		if(user == null) {
//			throw new LogonIdException("User with logonId "+logonId+ "doesn't exist.");
//		}
//		return user;
//	}
}
