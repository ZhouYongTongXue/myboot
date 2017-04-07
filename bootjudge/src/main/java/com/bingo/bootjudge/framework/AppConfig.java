package com.bingo.bootjudge.framework;

import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

/**
 * 系统配置类，用于不方便使用spring获取时手动获取的工具类
 * 
 * @author tornado.z.y
 * @date 2017年3月29日 上午9:52:47
 */
public class AppConfig {
	private static AppConfig instance;
	private Properties properties;
	private AppConfig() {
		YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
		bean.setResources(new ClassPathResource("application.yml"));
		properties = bean.getObject();
	}
	
	/**
	 * instance
	 * @return
	 */
	public static AppConfig getInstance() {
		if(instance == null){
			instance = new AppConfig();
		}
		return instance;
	}
	
	public String getProperty(String key){
		return properties.getProperty(key);
	}
	
	public String getProperty(String key,String defaultValue){
		return properties.getProperty(key,defaultValue);
	}
}
