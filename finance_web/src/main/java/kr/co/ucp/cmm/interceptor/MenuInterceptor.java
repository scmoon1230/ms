package kr.co.ucp.cmm.interceptor;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.egov.com.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.mntr.cmm.service.CmmCodeService;

public class MenuInterceptor extends HandlerInterceptorAdapter
{
	private final Logger logger = LoggerFactory.getLogger(MenuInterceptor.class);

	@Resource(name="config")
	private Properties config;

	@Resource(name = "cmmCodeService")
	private CmmCodeService cmmCodeService;

	//private Set<String> permittedURL;

	public void setPermittedURL(Set<String> permittedURL) {
		//this.permittedURL = permittedURL;
	}

	// 메뉴접근로그 저장
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI(); //요청 URI
		logger.info("==== requestURI : {}",requestURI);
		
		//String pgmMenuId = EgovStringUtil.nullConvert(request.getParameter("m"));
		//if (!"".equalsIgnoreCase(pgmMenuId)) {
		//	logger.info("==== pgmMenuId : {}",pgmMenuId);
		//}
		//if( null == pgmMenuId || "".equals(pgmMenuId) ) return true;
		
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String userId = loginVO.getUserId();
		if( null == userId || "".equals(userId)) {
			logger.info("preHandle(), 로그인 정보 없음 >>>> ");
			return true;
		}
		
		//List<Map<String, String>> list = loginVO.getMenuList();
		//logger.debug("==== menu >>>> {},{},{}",pgmMenuId, userId, requestURI);
/*		if (!loginVO.getMenuList().toString().contains(pgmMenuId)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter printwriter = response.getWriter();
            printwriter.println("<script>");
            printwriter.println("alert('접근 경로가 잘못되었습니다.')");
            printwriter.println("window.close();");
            printwriter.println("</script>");
            printwriter.flush();
            printwriter.close();
			return false;
		}
*/
		if ( requestURI.indexOf(".do") != -1) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dbName", EgovStringUtil.nullConvert(config.get("Globals.dbName")));
			map.put("userId"  , userId);
			map.put("progId"  , requestURI.substring(1));
			cmmCodeService.insertCmMuneUsedLog(map);
		}
		
		return true;
	}

}
