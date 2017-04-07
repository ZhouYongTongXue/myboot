package com.bingo.bootjudge.framework.shiro;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bingo.bootjudge.framework.CurrentUser;
import com.bingo.bootjudge.framework.SessionManager;


/**
 * 用户身份验证
 * @author junjie
 *
 */
public class UserRealm extends AuthorizingRealm {

	//@Autowired
	//private UserService userService;

	private static final Logger log = LoggerFactory.getLogger("user-error");

	/**
	 * 授权验证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// String username = (String) principals.getPrimaryPrincipal();
		// User user = userService.findByUsername(username);
		// SimpleAuthorizationInfo authorizationInfo = new
		// SimpleAuthorizationInfo();
		// authorizationInfo.setRoles(userService.findStringRoles(user));
		// authorizationInfo.setStringPermissions(userService.findStringPermissions(user));

		 System.err.println("开始权限校验");

//		CurrentUser user = SessionManager.getUserSession();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//		authorizationInfo.setRoles(user.getRoles());
		authorizationInfo.setStringPermissions(Stream.of("test".split(",")).collect(Collectors.toSet()));
		authorizationInfo.addRole("admdin");
		return authorizationInfo;
	}

	private static final String OR_OPERATOR = " or ";
	private static final String AND_OPERATOR = " and ";
	private static final String NOT_OPERATOR = "not ";

	/**
	 * 支持or and not 关键词 不支持and or混用
	 *
	 * @param principals
	 * @param permission
	 * @return boolean
	 */
	public boolean isPermitted(PrincipalCollection principals, String permission) {
		if (permission.contains(OR_OPERATOR)) {
			String[] permissions = permission.split(OR_OPERATOR);
			for (String orPermission : permissions) {
				if (isPermittedWithNotOperator(principals, orPermission)) {
					return true;
				}
			}
			return false;
		} else if (permission.contains(AND_OPERATOR)) {
			String[] permissions = permission.split(AND_OPERATOR);
			for (String orPermission : permissions) {
				if (!isPermittedWithNotOperator(principals, orPermission)) {
					return false;
				}
			}
			return true;
		} else {
			return isPermittedWithNotOperator(principals, permission);
		}
	}

	private boolean isPermittedWithNotOperator(PrincipalCollection principals,
			String permission) {
		if (permission.startsWith(NOT_OPERATOR)) {
			return !super.isPermitted(principals,
					permission.substring(NOT_OPERATOR.length()));
		} else {
			return super.isPermitted(principals, permission);
		}
	}

	/**
	 * 授权验证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws ServiceException {

		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		System.out.println(upToken.getUsername());
		String username = upToken.getUsername().trim();
		String password = "";
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username,
				password.toCharArray(), getName());
		System.out.println("doGetAuthenticationInfo");
		
		System.out.println(SecurityUtils.getSubject());
		return info;
	}

}
