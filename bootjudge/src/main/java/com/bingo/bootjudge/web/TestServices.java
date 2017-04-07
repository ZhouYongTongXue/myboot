package com.bingo.bootjudge.web;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TestServices {
	@Cacheable(value = "apiAccessTokenCache",key = "#name")
	public String getUser(String name){
		System.out.println("in");
		return "hehehe" + name;
	}
}
