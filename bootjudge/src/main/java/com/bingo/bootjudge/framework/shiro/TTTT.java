package com.bingo.bootjudge.framework.shiro;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TTTT {
	
	@Pointcut(value="execution(* com.bingo.bootjudge.web.TestControl.home(..))" )  
	public void beforePointcut() {}  
	@Before(value = "beforePointcut()" )
	public void beforeAdvice() {
		System.out.println("===========before advice param:" );
	}
}
  