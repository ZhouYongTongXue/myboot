package com.bingo.bootjudge.web;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TestServices {
	@Autowired
	HelloDao helloDao;
	@org.springframework.transaction.annotation.Transactional
	public String getUser(String name){
		
		System.out.println("in");
		helloDao.save(new Test());
		"".split(",")[6].toCharArray();
		return "hehehe" + name;
	}
}
