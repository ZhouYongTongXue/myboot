package com.bingo.bootjudge.framework;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;



/**
 * 登陆成功后会话信息的统一管理，
 *
 */
public class SessionManager {

	/** 用户session标识 **/
	public static String MEMBER_SESSION = "userSession";

	/**
	 * 获取用户会话信息。
	 * 
	 * @return 用户会话信息。如果用户没有验证登陆则返回null。
	 */
	public static CurrentUser getUserSession() {
		Subject subject = SecurityUtils.getSubject();
		return (CurrentUser) subject.getSession().getAttribute(MEMBER_SESSION);

	}// end getUserSession

	/**
	 * 设置用户会话信息。
	 * 
	 * @param currentUser
	 *            当前用户。
	 */
	public static void setUserSession(CurrentUser currentUser) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		session.setAttribute(MEMBER_SESSION, currentUser);
	}

	/**
	 * 移除用户会话信息。
	 * 
	 */
	public static void removeUserSession() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		session.removeAttribute(MEMBER_SESSION);
	}

	/**
	 * 移除项目中用到的所有会话信息。
	 * 
	 */
	public static void removeAllSessions() {
		removeUserSession();
	}

	public static boolean hasUser() {
		return getUserSession() != null;
	}
	
	 
}