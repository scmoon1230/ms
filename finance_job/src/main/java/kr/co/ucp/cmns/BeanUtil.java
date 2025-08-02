package kr.co.ucp.cmns;

import org.springframework.context.ApplicationContext;

public class BeanUtil {
	public static Object getBean(String beanName) {
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		return applicationContext.getBean(beanName);
	}
	public static Object getBean(Class<?> classType) {
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		return applicationContext.getBean(classType);
	}
}
