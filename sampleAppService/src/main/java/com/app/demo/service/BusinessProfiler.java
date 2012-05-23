package com.app.demo.service;

import java.io.Serializable;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class BusinessProfiler implements Serializable {

	private static final long serialVersionUID = -4808959741677987310L;

	@Pointcut("execution(* com.app.demo.service.*.*(..))")
	public void businessMethods() {
	}

	@Around("businessMethods()")
	public Object profile(ProceedingJoinPoint pjp) throws Throwable {
		long start = System.currentTimeMillis();
		System.out.println("Going to call the method. : "
				+ pjp.getSignature().getName());
		Object output = pjp.proceed();
		// System.out.println("Method execution completed.");
		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println("   :>   Method execution time: " + elapsedTime
				+ " milliseconds.");
		return output;
	}

}
