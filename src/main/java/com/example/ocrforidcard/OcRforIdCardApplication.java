package com.example.ocrforidcard;

import com.example.ocrforidcard.dao.entities.Role;
import com.example.ocrforidcard.dao.entities.User;
import com.example.ocrforidcard.services.UserService;
import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class OcRforIdCardApplication {

	private static final Logger logger = LoggerFactory.getLogger(OcRforIdCardApplication.class);

	private final UserService userService;

	@Autowired
	public OcRforIdCardApplication(UserService userService) {
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(OcRforIdCardApplication.class, args);

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		OpenCV.loadLocally();
		logger.info("OpenCV loaded successfully!");

		System.setProperty("java.library.path", "C:\\Users\\user\\Downloads\\opencv\\build\\java\\x64\\opencv_java490.dll");

		logger.info("Library path: {}", System.getProperty("java.library.path"));
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//@PostConstruct
	public void initialUsers() {
		userService.addRole(new Role(null, "ADMIN"));
		userService.addRole(new Role(null, "USER"));

		userService.saveUser(new User(null, "admin", "123", true, null, null));
		userService.saveUser(new User(null, "user", "123", true, null, null));

		userService.addRoleToUser("admin", "ADMIN");
		userService.addRoleToUser("user", "USER");
	}
}
