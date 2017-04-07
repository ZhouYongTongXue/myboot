package com.bingo.bootjudge;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.bingo.bootjudge.framework.CustomConfigurationFactory;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		ConfigurationFactory.setConfigurationFactory(new CustomConfigurationFactory());
		super.onStartup(servletContext);
	}
	

	public static void main(String[] args) throws Exception {
		ConfigurationFactory.setConfigurationFactory(new CustomConfigurationFactory());
		SpringApplication.run(Application.class, args);
	}
}
