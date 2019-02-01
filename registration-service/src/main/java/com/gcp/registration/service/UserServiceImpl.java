package com.gcp.registration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gcp.registration.domain.User;
import com.gcp.registration.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	protected UserRepository userRepository;

	@Override
	public User registerUser(User user) {
		System.out.println("user logon id: "+user.getLogonId());
			return userRepository.save(user);
	}
}
