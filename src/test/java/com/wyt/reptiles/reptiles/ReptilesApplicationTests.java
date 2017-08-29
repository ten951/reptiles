package com.wyt.reptiles.reptiles;

import com.wyt.reptiles.reptiles.service.IKkcService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReptilesApplicationTests {
	@Autowired
	private IKkcService kkcService;

	@Test
	public void contextLoads() {
		kkcService.kkcinit();
	}

}
