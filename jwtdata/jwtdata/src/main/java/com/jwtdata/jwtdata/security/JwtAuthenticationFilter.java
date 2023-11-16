package com.jwtdata.jwtdata.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jwtdata.jwtdata.helper.JwtUtil;
import com.jwtdata.jwtdata.service.CustomeUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
@ComponentScan
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private CustomeUserDetailsService customeUserDetailsService;

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String requestTokenHeader = request.getHeader("Authorization");
		System.out.println(requestTokenHeader);
		String username = null;
		String jwtToken = null;
		
		if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer "))
		{
			jwtToken=requestTokenHeader.substring(7);
			try
			{
			username = this.jwtUtil.getUsernameFromToken(jwtToken);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			UserDetails userDetails = this.customeUserDetailsService.loadUserByUsername(username);

			if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
			{
				UsernamePasswordAuthenticationToken UsernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				UsernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(UsernamePasswordAuthenticationToken);

			}else 
			{
				System.out.println("token is not validated...");
			}
			
		 }
		
		filterChain.doFilter(request, response);		


		}
}





//package com.jwtdata.jwtdata.security;
//
//import java.io.IOException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.MalformedJwtException;
//
//@Component
//@ComponentScan
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//	@Autowired
//	private UserDetailsService userDetailsService;
//	
//	@Autowired
//	private JwtTokenHelper jwtTokenHelper;
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		
//		String requestToken = request.getHeader("Authorization");
//		System.out.println(requestToken);
//		String username = null;
//		String token = null;
//		
//		if(requestToken != null && requestToken.startsWith("Bearer"))
//		{
//			token = requestToken.substring(7);
//			try
//			{
//			username = this.jwtTokenHelper.getUsernameFromToken(token);
//			}
//			catch(IllegalArgumentException e)
//			{
//				System.out.println("unable to get jwt token");
//			}
//			catch(ExpiredJwtException e) {
//				System.out.println("jwt token has expired");
//			}
//			catch(MalformedJwtException e)
//			{
//				System.out.println("invalid jwt");
//			}
//		}else
//		{
//			System.out.println("jwt token does not begin with bearer");
//		}
//		
//		
//		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
//		{
//			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//			
//			if(this.jwtTokenHelper.validateToken(token, userDetails))
//			{
//				UsernamePasswordAuthenticationToken UsernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//				UsernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(UsernamePasswordAuthenticationToken);
//			}else
//			{
//				System.out.println("Invalid jwt token");
//			}	
//		}else 
//		{
//			System.out.println("username is null or context is not null");
//		}
//		
//		filterChain.doFilter(request, response);		
//	}
//	
//	public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtTokenHelper jwtTokenHelper)
//	{
//		this.userDetailsService = userDetailsService;
//		this.jwtTokenHelper = jwtTokenHelper;
//	}
//
//}






//package com.jwtdata.jwtdata.security;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.MalformedJwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//	@Autowired
//	private UserDetailsService userDetailsService;
//	
////	@Bean
////	public UserDetailsService userDetailsService() {
////	    return super.userDetailsService();
////	}
//
//	@Autowired
//	private JwtTokenHelper jwtTokenHelper;
//	
//
//
//	
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		
//		String requestToken = request.getHeader("Authorization");
//		System.out.println(requestToken);
//		String username = null;
//		String token = null;
//		
//		if(requestToken != null && requestToken.startsWith("Bearer"))
//		{
//			token = requestToken.substring(7);
//			try
//			{
//			username = this.jwtTokenHelper.getUsernameFromToken(token);
//			}
//			catch(IllegalArgumentException e)
//			{
//				System.out.println("unable to get jwt token");
//			}
//			catch(ExpiredJwtException e) {
//				System.out.println("jwt token has expired");
//			}
//			catch(MalformedJwtException e)
//			{
//				System.out.println("invalid jwt");
//			}
//		}else
//		{
//			System.out.println("jwt token does not begin with bearer");
//		}
//		
//		
//		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
//		{
//			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//			
//			if(this.jwtTokenHelper.validateToken(token, userDetails))
//			{
//				UsernamePasswordAuthenticationToken UsernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//				UsernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(UsernamePasswordAuthenticationToken);
//			}else
//			{
//				System.out.println("Invalid jwt token");
//			}	
//		}else 
//		{
//			System.out.println("username is null or context is not null");
//		}
//		
//		filterChain.doFilter(request, response);
//	}
//
//
//}
