package nl.ckarakoc.jellycash.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

	@GetMapping("/security-test")
	public String securityTest(){
		// Needs to be logged in
		return "Ok";
	}

	@GetMapping("/admin/security-test")
	public String adminSecurityTest(){
		// Needs to be admin
		return "Ok";
	}

	@GetMapping("/api/security-test")
	public String apiSecurityTest(){
		// Permitted for all
		return "Ok";
	}
}
