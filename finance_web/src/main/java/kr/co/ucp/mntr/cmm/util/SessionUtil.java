package kr.co.ucp.mntr.cmm.util;

import kr.co.ucp.cmm.LoginVO;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * session Util
 * - Spring에서 제공하는 RequestContextHolder 를 이용하여
 * request 객체를 service까지 전달하지 않고 사용할 수 있게 해줌
 *
 */
public class SessionUtil{

	/**
	 * attribute 값을 가져 오기 위한 method
	 *
	 * @param String  attribute key name
	 * @return Object attribute obj
	 */
	public static Object getAttribute(String name) throws Exception {
		return (Object)RequestContextHolder.getRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_SESSION);
	}

	/**
	 * attribute 설정 method
	 *
	 * @param String  attribute key name
	 * @param Object  attribute obj
	 * @return void
	 */
	public static void setAttribute(String name, Object object) throws Exception {
		RequestContextHolder.getRequestAttributes().setAttribute(name, object, RequestAttributes.SCOPE_SESSION);
	}

	/**
	 * 설정한 attribute 삭제
	 *
	 * @param String  attribute key name
	 * @return void
	 */
	public static void removeAttribute(String name) throws Exception {
		RequestContextHolder.getRequestAttributes().removeAttribute(name, RequestAttributes.SCOPE_SESSION);
	}

	/**
	 * session id
	 *
	 * @param void
	 * @return String SessionId 값
	 */
	public static String getSessionId() throws Exception  {
		return RequestContextHolder.getRequestAttributes().getSessionId();
	}


	/**
     * 로그인한 여부
     *
     * @param void
     * @return String SessionId 값
     */
	  public static boolean isLoggedIn() {
	    return SessionUtil.getUserInfo() == null ? false : true;
	  }

	  /**
	   * HttpSession에 로그인한 사용자 정보를 담은 VO를 가져 온다.
	   *
	   * @return LoginVO - 로그인한 사용자 정보를 담은 VO
	   */
	  public static LoginVO getUserInfo() {
	      return (LoginVO)RequestContextHolder.currentRequestAttributes().getAttribute("LoginVO", RequestAttributes.SCOPE_SESSION);
	  }


	  /**
	   * HttpSession에 로그인한 로그인 ID 를 가져 온다.
	   *
	   * @return String - 로그인한 로그인 ID
	   */
	  public static String getUserId() {
	      return  SessionUtil.isLoggedIn() ? SessionUtil.getUserInfo().getUserId() : "";
	  }

	  /**
	   * HttpSession에 로그인한 이름만 가져 온다.
	   *
	   * @return String - 로그인한 사용자의 이름
	   */
	  public static String getUserNm() {
	    return  SessionUtil.isLoggedIn() ? SessionUtil.getUserInfo().getUserNmKo() : "";
	  }
}



