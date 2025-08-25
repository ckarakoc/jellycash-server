package nl.ckarakoc.jellycash.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

	@GetMapping("/security-test")
	public String securityTest(){
		return "Ok";
	}

	@GetMapping("/admin/security-test")
	public String adminSecurityTest(){
		return "Ok";
	}

	@GetMapping("/api/security-test")
	public String apiSecurityTest(){
		return "Ok";
	}
}
