<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Login</title>
</head>

<body>
    <h1>登录页面---- </h1>
    <form action="${pageContext.request.contextPath }/login"
        method="post">
        用户名：<input name="username" />
        密码：<input name="password" />
        <button name="button">提交</button>
    </form>
</body>
</html>