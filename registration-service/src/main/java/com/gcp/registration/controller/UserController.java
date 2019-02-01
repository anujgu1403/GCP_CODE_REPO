package com.gcp.registration.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gcp.registration.domain.User;
import com.gcp.registration.service.UserService;

@RestController
@RequestMapping("/api/register-user/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result ){
		System.out.println("user logon id********: "+user.getLogonId());
		User registeredUser = userService.registerUser(user);
		return new ResponseEntity<User>(registeredUser, HttpStatus.CREATED);		
	}
}
