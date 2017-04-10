package com.bingo.bootjudge.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DelegatingSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bingo.bootjudge.framework.AppConfig;


@RestController
public class TestControl {
	
//	@Autowired
	private AppConfig config;
	
//	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private TestServices testService;
	
	@Autowired
	private HelloDao hello;
	
	
//	@Autowired
	org.springframework.cache.ehcache.EhCacheCacheManager d;
	
	@RequestMapping("/2test")
//	@RequiresPermissions("test")
//	@RequiresAuthentication
	@RequiresRoles("admdin")
	Map<String, Object> home(String name) {
		Map<String,Object> result = new HashMap<String, Object>();
		System.out.println(testService.getUser(name));;
		System.out.println("进入web方法");
		return result;
	}
	
	@RequestMapping("/testlogin")
	public String login(){
		 UsernamePasswordToken token = new UsernamePasswordToken("", "");
		 SecurityUtils.getSubject().login(token);
		return "OK";
	}
	
}

class c{
	String ac;

	public String getAc() {
		return ac;
	}

	public void setAc(String ac) {
		this.ac = ac;
	}
	
}