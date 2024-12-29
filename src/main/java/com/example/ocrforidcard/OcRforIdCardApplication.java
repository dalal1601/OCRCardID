package com.example.ocrforidcard;

import com.example.ocrforidcard.dao.entities.Role;
import com.example.ocrforidcard.dao.entities.User;
import com.example.ocrforidcard.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class OcRforIdCardApplication {
	@Autowired
	UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(OcRforIdCardApplication.class, args);
	}
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() { return new BCryptPasswordEncoder(); }
	//@PostConstruct
	void initial_users(){
		userService.addRole(new Role(null,"ADMIN"));
		userService.addRole(new Role(null,"USER"));


		userService.saveUser(new User(null ,"admin","123",true,null,null));
		userService.saveUser(new User(null ,"user","123",true,null,null));


		userService.addRoleToUser("admin", "ADMIN");
		userService.addRoleToUser("user", "USER");
	}

}
