package com.rajat.caching;

import com.rajat.caching.entities.User;
import com.rajat.caching.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CachingJavaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CachingJavaApplication.class, args);
	}

	@Autowired
	UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		log.info("Inserting a few users into the temp database");

		User user1 = new User(1,"Rajat","Gupta", "rajatgupta828@gmail.com");
		User user2 = new User(2,"FN1","LN1", "fn1.ln1@gmail.com");
		User user3 = new User(3,"FN2","LN2", "fn2.ln2@gmail.com");
		User user4 = new User(4,"FN3","LN3", "fn3.ln3@gmail.com");
		User user5 = new User(5,"FN4","LN4", "fn4.ln4@gmail.com");

		userRepository.save(user1);
		userRepository.save(user2);
		userRepository.save(user3);
		userRepository.save(user4);
		userRepository.save(user5);
	}
}
