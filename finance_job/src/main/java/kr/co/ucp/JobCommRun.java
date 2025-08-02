package kr.co.ucp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import kr.co.ucp.cmns.ConfigManager;

// 스케줄러 실행
public class JobCommRun {
	static Logger logger = LoggerFactory.getLogger(JobCommRun.class);
	public static ApplicationContext ac = new GenericXmlApplicationContext("conf/appContext.xml");
    public static void main(String[] args) {
    	try {

			ConfigManager.setProperties("service.prprts", true);

            String[] beanDefinitionNames = ac.getBeanDefinitionNames();
            for (String beanDefinitionName : beanDefinitionNames) {
                Object bean = ac.getBean(beanDefinitionName);
    			logger.debug(" ==== bean name >>>> {} ", beanDefinitionName);
            }

			new ClassPathXmlApplicationContext("conf/jobContext.xml");

    	} catch (Exception e) {
			logger.error(" ==== JobCommRun Exception >>>> {} ", e.getMessage());
		}
    }
}
