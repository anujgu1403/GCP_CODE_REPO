package com.gcp.registration.controller;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gcp.registration.domain.UserDetail;
import com.gcp.registration.service.MapValidationErrorService;
import com.gcp.registration.service.UserService;

/**
 * @author Anuj Kumar
 * 
 * This class is rest controller which publish end point.
 */

@RestController
@RequestMapping("/api/login-service")
public class UserloginController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserloginController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	/**
	 * This method is used to call the findUserByLogonIdAndPassword method to authenticate the user login
	 * 
	 * @param userDetail
	 * @param result
	 * @return ResponseEntity<?>
	 */
	@PostMapping("")
	public ResponseEntity<?> authenticateUserLogin(@Valid @RequestBody UserDetail userDetail, BindingResult result ){
		logger.info("Start authenticateUserLogin method: ", UserloginController.class.getName());
		logger.debug("User request : ", userDetail);
		
		//To validate the request
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap!=null) return errorMap;
        
		String response = userService.findUserByLogonIdAndPassword(userDetail);		
		logger.debug("User response:", response);
		logger.info("End registerUser method:", UserloginController.class.getName());
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}	
}
