package com.example.BookingHotel.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {
	
	@GetMapping()
	public String home() {		
		return "Welcome";
	}
	
	@GetMapping("/user")
	public String user() {		
		return "user";
	}
	
	@GetMapping("/admin")
	public String admin() {		
		return "admin";
	}
}
