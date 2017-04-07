package com.bingo.bootjudge.framework.shiro;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {
    /**
     * 操作名称
     */
	public static String OP_NAME = "op";


    /**
     * 消息key
     */
    public static String MESSAGE = "message";

    /**
     * 错误key
     */
    public static String ERROR = "error";

    /**
     * 上个页面地址
     */
    public static String BACK_URL = "BackURL";

    public static String IGNORE_BACK_URL = "ignoreBackURL";

    /**
     * 当前请求的地址 带参数
     */
    public static String CURRENT_URL = "currentURL";

    /**
     * 当前请求的地址 不带参数
     */
    public static String NO_QUERYSTRING_CURRENT_URL = "noQueryStringCurrentURL";

    public static String CONTEXT_PATH = "ctx";

    /**
     * 当前登录的用户
     */
    public static String CURRENT_USER = "user";
    public static String CURRENT_USERNAME = "username";

    public static String ENCODING = "UTF-8";
    
    public static final String PARAM_TOKEN = "token";
    public static final String PARAM_USERNAME = "username";
    
    public static boolean isExcludeUrl(String url) {
    	return isApiExcludeUrl(url)||isResUrl(url);
    }
    
    public static boolean isResUrl(String url) {//资源文件不做拦截
		url = url.toLowerCase();
		return url.endsWith(".jsp")
			 ||url.endsWith(".js")
			 ||url.endsWith(".png")
			 ||url.endsWith(".ico")
			 ||url.endsWith(".exe")
			 ||url.endsWith(".xml")
			 ||url.endsWith(".mp4")
			 ||url.endsWith(".jpg")
			 ||url.endsWith(".gif")
			 ||url.endsWith(".css")
			 ||url.endsWith(".swf")
			 ||url.endsWith(".html")
			 ||url.endsWith(".docx")
			 ||url.endsWith(".doc")
			 ||url.endsWith(".xls")
			 ||url.endsWith(".txt");
	}
    
    private static boolean isApiExcludeUrl(String url) {
		return matches("/login", url)
			 ||matches("/loginByOpenId", url)
			 ||matches("/logout", url)
			 ||matches("/login/", url)
			 ||matches("/checkRegist", url)
			 ||matches("/logout/", url)
			 ||matches("^/druid/.*$", url);
//			 ||matches("^/api/media/.*$", url)||matches("^/api/queue/.*$", url)||matches("^/api/phone/.*$", url)
//			 ||matches("^/api/device/.*$", url)||matches("^/api/led/.*$", url)||matches("^/commons/errors/.*$", url);
	}
	
	private static boolean matches(String pattern, String source) {
        if (pattern == null) {
            throw new IllegalArgumentException("pattern argument cannot be null.");
        }
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);
        return m.matches();
    }
	
	public static void main(String[] args) {
		String s = "/api/media/led/findLED.xml";
		System.out.println(isApiExcludeUrl(s));
	}

}
