package com.bingo.bootjudge.my;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bingo.bootjudge.BaseTest;
import com.bingo.bootjudge.web.TestServices;

public class MyTest extends BaseTest{

	@Autowired
	private TestServices test;
	
	@Test
	public void exampleTest2() {
//		String body = this.restTemplate.getForObject("/", String.class);
		System.out.println(test);
	}
}
