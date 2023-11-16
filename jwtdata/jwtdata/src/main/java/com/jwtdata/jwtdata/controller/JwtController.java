package com.jwtdata.jwtdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jwtdata.jwtdata.helper.JwtUtil;
import com.jwtdata.jwtdata.model.JwtRequest;
import com.jwtdata.jwtdata.model.JwtResponse;
import com.jwtdata.jwtdata.service.CustomeUserDetailsService;

@RestController
public class JwtController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomeUserDetailsService customeUserDetailsService;

	@Autowired
	private JwtUtil jwtUtil;
	
	@RequestMapping(value = "/token", method = RequestMethod.POST)
	public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception{
		System.out.println(jwtRequest);	
		try
		{
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));
		}catch(UsernameNotFoundException e)
		{
			e.printStackTrace();
			throw new Exception("Bad credentials");
		}catch(BadCredentialsException e)
		{
			e.printStackTrace();
			throw new Exception("Bad credentials");
		}
		
		UserDetails userDetails = this.customeUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
		
		String token = this.jwtUtil.generateToken(userDetails);
		System.out.println("JWT"+token);
		
		
		return ResponseEntity.ok(new JwtResponse(token));
	}
	
}



//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.jwtdata.jwtdata.payload.JwtAuthRequest;
//import com.jwtdata.jwtdata.security.JwtAuthResponse;
//import com.jwtdata.jwtdata.security.JwtTokenHelper;
//
//
//@RestController
//@RequestMapping("/token")
//public class JwtController {
//
//	@Autowired
//	private JwtTokenHelper jwtTokenHelper;
//	
//	@Autowired
//	private UserDetailsService userDetailsService;
//
//	@Autowired
//	private AuthenticationManager authenticationManager;
//	
//	@PostMapping("/login")
//	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception
//	{
//		this.authenticate(request.getUsername(),request.getPassword());
//		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
//		String token = this.jwtTokenHelper.generateToken(userDetails);
//		JwtAuthResponse response = new JwtAuthResponse();
//		response.setToken(token);
//		return new ResponseEntity<JwtAuthResponse>(response,HttpStatus.OK);
//	}
//	
//	private void authenticate(String username, String password) throws Exception {
//		
//		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
//		try {
//		    this.authenticationManager.authenticate(authenticationToken);
//		}
//		catch(BadCredentialsException e){
//			System.out.println("invlid details !!");
//			throw new Exception("invalid username or password");
//		}
//		
//	}
//	
//}


