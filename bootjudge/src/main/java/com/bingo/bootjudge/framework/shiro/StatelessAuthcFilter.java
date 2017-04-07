package com.bingo.bootjudge.framework.shiro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

/**
 * 权限Filter.
 * 
 * @author xlsiek
 *
 */
public class StatelessAuthcFilter extends PathMatchingFilter {

	/**
	 * 缓存管理
	 */
	@Autowired
	private CacheManager cacheManager;

	/**
	 * 允许访问
	 */
	protected boolean onPreHandle(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse rep = (HttpServletResponse) response;
		if (Constants.isExcludeUrl(WebUtils.getPathWithinApplication(req))) {
			return true;
		}
		/*
		String token = req.getParameter("token");
		Cache<String, User> apiAccessTokenCache = cacheManager
				.getCache("apiAccessTokenCache");
		CommonResponse commonResponse = new CommonResponse();
		getWanNengToken(token, apiAccessTokenCache);
		// httpUserService.checkRemoteToken(token);
		if (apiAccessTokenCache.get(token) == null) {
			commonResponse.setCode(ErrorCode.UN_LOGIN_OR_LOGIN_EXPIRED
					.getCode());
			commonResponse.setData(null);
			commonResponse.setMessage("您的登录信息已失效，请重新登录");
			rep.setContentType("application/json;charset=utf-8");
			rep.getWriter().print(JSON.toJSONString(commonResponse));
			return false;
		} else {
			User user = apiAccessTokenCache.get(token);
			CurrentUser currentUser = SessionManager.getUserSession();
			if (currentUser == null
					|| !currentUser.getId().equals(user.getId())
					|| !currentUser.getUserToken().equals(token)) {
				currentUser = new CurrentUser();
				currentUser.setId(user.getId());
				currentUser.setIsAudit(user.getIsAudit());
				currentUser.setUsername(user.getUsername());
				currentUser.setAdmin(user.getIsAdmin());
				currentUser.setRealname(user.getRealName());
//				currentUser.setContactsEmail(user.getContactsEmail());
				currentUser.setEmail(user.getEmail());
				currentUser.setMobilePhone(user.getMobilePhone());
				currentUser.setCompanyRegisterNo(user.getCompanyRegisterNo());
				currentUser.setContacts(user.getContacts());
				currentUser.setCompanyName(user.getCompanyName());
//				if (!CollectionUtils.isEmpty(user.getRolesIdentiCash())) {
//					currentUser.setRoles(user.getRolesNameCash());
//					currentUser.setRolesIdenti(user.getRolesIdentiCash());
//				}
				currentUser.setUserType(getUserTypeByRole(user.getIsAdmin(),user.getRole()));
				currentUser.setUserToken(token);
				currentUser.setPermissions(user.getPermissions());
				currentUser.setUploadProductAuth(user.getIsUploadProductSpecialTime() != null && user.getIsUploadProductSpecialTime());
				currentUser.setBiddingPriceAuth(user.getIsOpenBiddingPriceSpecialTime() != null && user.getIsOpenBiddingPriceSpecialTime());
			}
			// 每次都让其登录。
			SessionManager.setUserSession(currentUser);
			Subject subject = getSubject(request, response);
			if (subject.getPrincipal() == null) {
				UsernamePasswordToken loginToken = new UsernamePasswordToken(
						user.getUsername(), "");
				try {
					subject.login(loginToken);
				} catch (Exception e) {
					return false;
				}
			}
//			req.setAttribute(Constants.CURRENT_USER, currentUser);
			// System.out.println("开始更新当前用户");
			return true;
		}
	*/
		

		Subject subject = SecurityUtils.getSubject();
		System.out.println("filter subject : " + subject);
/*		if (subject.getPrincipal() == null) {
			UsernamePasswordToken loginToken = new UsernamePasswordToken(
					"11111", "");
			try {
				subject.login(loginToken);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}*/
//		subject.hasRole("add");
		System.out.println(SecurityUtils.getSubject() + ":" + subject);
		System.out.println(subject.isAuthenticated() + "进入Filter" + WebUtils.getRequestUri((HttpServletRequest)request));
		return true;
	}



	/**
	 * 登录失败时默认返回401状态码
	 * @param response  response
	 * @throws IOException io异常
	 */
	private void onLoginFail(ServletResponse response) throws IOException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpResponse.getWriter().write("login error");
	}

	/**
	 * 测试用例
	 * @param token
	 * @param cash
	
	private void getWanNengToken(String token, Cache<String, User> cash) {
		if(ConfigManager.getProperty("use.wannengtoken") == null || !Boolean.valueOf(ConfigManager.getProperty("use.wannengtoken"))){
			return;
		}
		if ("12345678".equals(token) && cash.get(token) == null) {
			// init wanneng token
			User user = new User();
			user.setId(1l);
			user.setIsAdmin(true);
			user.setUsername("admin");
			user.setPassword("");
			user.setAreaId(1l);
			cash.put(token, user);
		}
	}
	
	
	private UserType getUserTypeByRole(boolean isSuperAdmin,String role){
		if("admin".equals(role) || isSuperAdmin){
			return UserType.ADMIN;
		}else if("company".equals(role)){
			return UserType.VENDER;
		}else if("expert".equals(role)){
			return UserType.EXPERT;
		}
		return UserType.FINANCE;
	}
	 */
	
	public static void main(String[] args) {
	}
}
