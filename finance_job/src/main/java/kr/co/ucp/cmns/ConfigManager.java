/**
 *---------------------------------------------------------------------------------- 
 * @File Name	: ConfigManager.java
 * @Description	: 외부에 설정한 파일을 관리 하기 위한 클래스이다.
 * @author		: seungJun
 * @since		: 2013. 7. 26.
 * @version		: 1.0
 *----------------------------------------------------------------------------------
 * @Copyright (c)2013 남양주, WideCUBE, All Rights Reserved.
 *----------------------------------------------------------------------------------
 * 수정일			수정자			수정내용
 *----------------------------------------------------------------------------------
 * 2013. 7. 26.		seungJun		최초작성
 *---------------------------------------------------------------------------------- 
 */
package kr.co.ucp.cmns;

import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigManager {
	static Logger logger = LoggerFactory.getLogger(ConfigManager.class);

	private ConfigManager() {}

	/**
	 * properties 클래스
	 */
	private static Properties prop = new Properties();
	
	/**
	 * 초기 로드 카운트 수
	 */
	private static int initCnt = 0;
	
	/**
	 * properties 파일에서 읽어들인다.
	 * 
	 * @param configFile properties 파일경로
	 * @param isSystemProperties 시스템 property 여부
	 * @throws Exception 예외처리
	 */
	public static void setProperties(String configFile, boolean isSystemProperties) throws Exception {

		try {

			if (initCnt == 0) {

				prop.load(ConfigManager.class.getResourceAsStream("/" + configFile));
				if (isSystemProperties) {
					setSystemProperties();
				}
				initCnt++;

//				if (LogManager.isDebugEnable()) {
//					LogManager.debug(ConfigManager.class, prop);
//				}
			} else {
				logger.info("configuration already load..");
//				LogManager.warn(ConfigManager.class, "configuration already load..");
			}
		} catch (Exception e) {
//			logger.info("configuration already load..");
			logger.error(" : configuration not found..{}",e.getMessage());
			throw e;
		}
	}

	/**
	 * <pre>
	 * 속성명에 해당되는 데이타를 되돌려준다.
	 * </pre>
	 * @param key - Properties 객체에 설정된 속성명
	 * @return 속성명에 해당되는 데이타를 되돌려준다.
	 */
	public static String getProperty(String key) {
	
		String value = prop.getProperty(key);
		return (value == null) ? value : value.trim();
	
	}

	/**
	 * 속성명에 해당되는 데이타를 int 형으로 변환해서 되돌려준다.
	 * @param key - Properties 객체에 설정된 속성명
	 * @return 속성명에 해당되는 데이타를 int 형으로 변환해서 되돌려준다.
	 */
	public static int getIntProperty(String key) {
		
		int value = -1;
		
		try {
		
			value = Integer.parseInt(getProperty(key));
		
		} catch (Exception e) {
		
			value = -1;
		}
		
		return value;
	}

	/**
	 * manager.properties 파일 정보를 파싱해서 System Properties 에 설정한다.
	 * @throws Exception - 예외처리
	 */
	private static void setSystemProperties() throws Exception {
	
		try {
		
			// 설정 정보를 System Property 에 등록한다.
			String key = "";
			String value = "";
			
			for (Enumeration<?> e = prop.keys(); e.hasMoreElements(); System.setProperty(key, value))
			{
				key = (String)e.nextElement();
				value = prop.getProperty(key);
			}
		
		} catch (Exception e) {
			logger.error(" **** system registration fail..{}",e.getMessage());
			throw e;
		}
	}

	/**
	 * properties 파일에 정의되어있는 property 이름을 반환한다.
	 * @return
	 * @throws Exception
	 */
	public static Enumeration<?> getPropertyNames() throws Exception {
		return prop.propertyNames();
	}

}
