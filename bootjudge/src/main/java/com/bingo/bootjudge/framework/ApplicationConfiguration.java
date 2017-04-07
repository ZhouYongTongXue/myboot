package com.bingo.bootjudge.framework;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.bingo.bootjudge.framework.shiro.SpringCacheManagerWrapper;
import com.bingo.bootjudge.framework.shiro.StatelessAuthcFilter;
import com.bingo.bootjudge.framework.shiro.UserRealm;

/**
 * 系统配置类
 * 
 * @author tornado.z.y
 * @date 2017年3月30日 上午10:17:08
 */
@Configuration
@EnableCaching
public class ApplicationConfiguration {
	
	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}

	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNonStringKeyAsString,
				SerializerFeature.WriteDateUseDateFormat);
		fastConverter.setFastJsonConfig(fastJsonConfig);
		HttpMessageConverter<?> converter = fastConverter;
		return new HttpMessageConverters(converter);
	}

	@Bean
	public AppConfig applicationConfig() {
		return AppConfig.getInstance();
	}

	@Bean(initMethod = "init", destroyMethod = "close")
	@ConfigurationProperties(prefix = "spring.dataSource")
	public DataSource dataSource() {
		return new DruidDataSource();
	}

	@Bean
	public ServletRegistrationBean statViewServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean();
		reg.setServlet(new StatViewServlet());
		reg.addUrlMappings("/druid/*");
		reg.addInitParameter("loginUsername", "admin");
		reg.addInitParameter("loginPassword", "123456");
		reg.addInitParameter("resetEnable", "false");
		return reg;
	}

	@Bean
	public FilterRegistrationBean druidWebStatFilter() {
		FilterRegistrationBean reg = new FilterRegistrationBean();
		reg.setFilter(new WebStatFilter());
		reg.addUrlPatterns("/*");
		reg.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
		return reg;
	}
	
	@Bean(name = "myRealm")
	public UserRealm myShiroRealm(SpringCacheManagerWrapper cacheManager) {
		UserRealm realm = new UserRealm();
		realm.setCachingEnabled(false);
		return realm;
	}
	
	@Bean
	public SpringCacheManagerWrapper SpringCacheManagerWrapper(CacheManager cacheManager) {
		SpringCacheManagerWrapper managerWrapper = new SpringCacheManagerWrapper();
		managerWrapper.setCacheManager(cacheManager);
		return managerWrapper;
	}

	
//	@Bean
//	public SessionValidationScheduler getSessionValidationScheduler(@Lazy DefaultWebSessionManager sessionManager){
//		QuartzSessionValidationScheduler quartz = new QuartzSessionValidationScheduler();
//		quartz.setSessionValidationInterval(1800000);
//		quartz.setSessionManager(sessionManager);
//		return quartz;
//	}
//	
//	@Bean
//	public EnterpriseCacheSessionDAO getEnterpriseCacheSessionDAO(){
//		EnterpriseCacheSessionDAO dao = new EnterpriseCacheSessionDAO();
//		dao.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
//		dao.setActiveSessionsCacheName("shiro-activeSessionCache");
//		return dao;
//	}
//	
//	@Bean("simpleCookie")
//	public SimpleCookie getSimpleCookie(){
//		SimpleCookie sc = new SimpleCookie("sid");
//		sc.setHttpOnly(true);
//		sc.setMaxAge(-1);
//		return sc;
//	}
//	
//	@Bean("rememberCookie")
//	public SimpleCookie getSimpleCookieRememberMeManager(){
//		SimpleCookie sc = new SimpleCookie("bb");
//		sc.setHttpOnly(true);
//		sc.setMaxAge(-1);
//		return sc;
//	}
	
//	@Bean
//	public DefaultWebSessionManager getDefaultWebSessionManager(SessionValidationScheduler sessionValidationScheduler,
//			EnterpriseCacheSessionDAO sessionDAO,@Qualifier("simpleCookie") SimpleCookie simpleCookie){
//		DefaultWebSessionManager m = new DefaultWebSessionManager();
//		m.setGlobalSessionTimeout(1800000);
//		m.setDeleteInvalidSessions(true);
//		m.setSessionValidationSchedulerEnabled(true);
//		m.setSessionValidationScheduler(sessionValidationScheduler);
//		m.setSessionDAO(sessionDAO);
//		m.setSessionIdCookieEnabled(true);
//		m.setSessionIdCookie(simpleCookie);
//		return m;
//	}
	
//	
//	@Bean
//	public CookieRememberMeManager getCookieRememberMeManager(@Qualifier("rememberCookie") SimpleCookie simpleCookie){
//		CookieRememberMeManager cm = new CookieRememberMeManager();
//		cm.setCipherKey(org.apache.shiro.codec.Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
//		cm.setCookie(simpleCookie);
//		return cm;
//	}

	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager(UserRealm myRealm,SpringCacheManagerWrapper cacheManager
			 ) {
		DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
		dwsm.setRealm(myRealm);
		dwsm.setCacheManager(cacheManager);
//		dwsm.setSessionManager(sessionManager);
//		dwsm.setRememberMeManager(rememberMeManager);
		return dwsm;
	}
	
	
	@Bean
	public MethodInvokingFactoryBean methodInvokingFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
		MethodInvokingFactoryBean m = new MethodInvokingFactoryBean();
		m.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
		m.setArguments(new Object[]{defaultWebSecurityManager});
		return m;
	}
	
	@Bean
	public ExceptionHandlerExceptionResolver getExceptionResolver(){
		MyExceptionHandlerExceptionResolver my = new MyExceptionHandlerExceptionResolver();
		FastJsonJsonView jsonView = new FastJsonJsonView();
		jsonView.getFastJsonConfig().setSerializerFeatures(SerializerFeature.WriteMapNullValue,
				SerializerFeature.QuoteFieldNames, SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.WriteDateUseDateFormat);
		my.setFastJsonView(jsonView);
		return my;
	}
	

	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(
			DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
		aasa.setSecurityManager(securityManager);
		return aasa;
	}

	@Bean("ajaxUserFilter")
	public StatelessAuthcFilter getFilter(){
		return new StatelessAuthcFilter();
	}
	
	private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		filterChainDefinitionMap.put("/**", "ajaxUserFilter");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
	}

	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager ) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		loadShiroFilterChain(shiroFilterFactoryBean);
		return shiroFilterFactoryBean;
	}

}
