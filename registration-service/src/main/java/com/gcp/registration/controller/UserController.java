package com.gcp.registration.controller;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gcp.registration.domain.User;
import com.gcp.registration.service.MapValidationErrorService;
import com.gcp.registration.service.UserService;

/**
 * @author Anuj Kumar
 * 
 * This class is rest controller which publish end point.
 */

@RestController
@RequestMapping("/api/register-user/user")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	/**
	 * This method is used to call the registerUser method of UserService to register the new user
	 * 
	 * @param user
	 * @param result
	 * @return ResponseEntity<?>
	 */
	@PostMapping("")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result ){
		logger.info("Start registerUser method: ", UserController.class.getName());
		logger.debug("User request: ", user.toString());
		System.out.println("User request: "+user.toString());
		
		//To validate the request
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap!=null) return errorMap;
        
		User registeredUser = userService.registerUser(user);
		logger.debug("User response:", registeredUser.toString());
		logger.info("End registerUser method:", UserController.class.getName());
		return new ResponseEntity<User>(registeredUser, HttpStatus.CREATED);		
	}
}
