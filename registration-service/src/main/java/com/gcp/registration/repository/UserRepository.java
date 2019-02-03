package com.gcp.registration.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.gcp.registration.domain.User;

/**
 * @author Anuj Kumar
 * 
 * This is user repository used to perform DB operations
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	
	public User findUserByLogonId(String logonId); 
}
