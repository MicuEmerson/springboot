package com.example.demo;

import com.example.demo.domain.Manager;
import com.example.demo.repository.ManagerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private ManagerRepository managerRepository;

	@Test
	public void contextLoads() {
		managerRepository.save(new Manager("teofana", "1234"));

	}

}
