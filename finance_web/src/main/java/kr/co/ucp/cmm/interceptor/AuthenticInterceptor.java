package kr.co.ucp.cmm.interceptor;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
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
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.egov.com.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;

/**
 * 인증여부 체크 인터셉터
 *
 * @author 공통서비스 개발팀 서준식
 * @version 1.0
 * @see <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일	  수정자		  수정내용
 *  -------	--------	---------------------------
 *  2011.07.01  서준식		  최초 생성
 *  2011.09.07  서준식		  인증이 필요없는 URL을 패스하는 로직 추가
 *	  </pre>
 * @since 2011.07.01
 */
public class AuthenticInterceptor extends HandlerInterceptorAdapter {
	private final Logger logger = LoggerFactory.getLogger(AuthenticInterceptor.class);

	// private Set<String> permittedURL;
	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	public void setPermittedURL(Set<String> permittedURL) {
		// this.permittedURL = permittedURL;
		logger.debug("====================> setPermittedURL : [{}]", permittedURL);
	}

	// 세션에 계정정보(LoginVO)가 있는지 여부로 인증 여부를 체크한다.
	// 계정정보(LoginVO)가 없다면, 로그인 페이지로 이동한다.
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI(); // 요청 URI

		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		String childMenuId = EgovStringUtil.nullConvert(request.getParameter("child"));
		String topMenuId = EgovStringUtil.nullConvert(request.getParameter("top"));
		String leftMenuId = EgovStringUtil.nullConvert(request.getParameter("left"));
		String menuId = EgovStringUtil.nullConvert(request.getParameter("m"));
		boolean isEmptyMenuId = childMenuId.isEmpty() && topMenuId.isEmpty() && leftMenuId.isEmpty() && menuId.isEmpty();
		logger.debug(" ==== Request URL : {}, top={}, left={}, menu={}, child={}, isEmptyMenuId={}", requestURI, topMenuId, leftMenuId, menuId, childMenuId, isEmptyMenuId);

		if ("".equals(loginVO.getUserId())) {
			ModelAndView modelAndView = new ModelAndView("redirect:/wrks/lgn/login.do");
			throw new ModelAndViewDefiningException(modelAndView);
		} else if ("Y".equals(loginVO.getPwChgRequiredYn())) {
			ModelAndView modelAndView = new ModelAndView("redirect:/wrks/lgn/changepwd.do");
			throw new ModelAndViewDefiningException(modelAndView);
		}

		// 프로퍼티 수정시 reload처리
		if (request.getSession().getAttribute("LOAD_TIME").toString().compareTo(prprtsService.getString("LOAD_TIME")) < 0) {
			// session로드시간이 prprts로드시간보다 작은경우 session session reload 처리
			//			logger.debug(" ==== reload check load_time session < prprts >>>> Session:{}, prprts:{}"
			//						, request.getSession().getAttribute("LOAD_TIME").toString()
			//						, prprtsService.getString("LOAD_TIME")
			//						);
			request.getSession().setAttribute("RELOAD", "Y");
			//			prprtsService.reloadSession(request);
			ModelAndView modelAndView = new ModelAndView("redirect:/wrks/lgn/goHome.do");
			throw new ModelAndViewDefiningException(modelAndView);

			//		} else {
			//			logger.debug(" ==== reload check load_time session > prprts >>>> Session:{}, prprts:{}"
			//					, request.getSession().getAttribute("LOAD_TIME").toString()
			//					, prprtsService.getString("LOAD_TIME")
			//					);
		}

		List<Map<String, String>> topMenuList = loginVO.getTopMenuList();
		Map<String, Object> titleMenuMap = loginVO.getTitleMenuMap();
		if (requestURI.toUpperCase().contains("wrks/main/main".toUpperCase())) {
			request.setAttribute("topMenuList", topMenuList);
			request.setAttribute("titleMenuMap", titleMenuMap);
			return true;
		}
		// 상황관제
		if (requestURI.toUpperCase().contains("mntr/".toUpperCase())) {
			request.setAttribute("titleMenuMap", titleMenuMap);
			return true;
		}
		// 영상반출
		if (requestURI.toUpperCase().contains("tvo/".toUpperCase())) {
			request.setAttribute("titleMenuMap", titleMenuMap);
			return true;
		}
		// 빈페이지
		if (requestURI.toUpperCase().contains("blank/".toUpperCase())) {
			return true;
		}
		// 링크
		if (requestURI.toUpperCase().contains("link/".toUpperCase())) {
			return true;
		}

		return true;

/*
		if (isEmptyMenuId) {
			// 메뉴권한체크
			String authOk = "N";
			List<Map<String, String>> menuList = loginVO.getMenuList();
			for (Map<String, String> menuMap : menuList) {
			//	logger.info(" == "+requestURI + " == "+menuMap.get("pgmUrl3"));
			  if (requestURI.toUpperCase().contains(menuMap.get("pgmUrl3").toUpperCase())) {
				  authOk = "Y";
				  break;
			  }
			}
			if ("N".equals(authOk)) {
				logger.error(" ==== 권한이 없는 메뉴 접속 시도 >>>> {}", requestURI);
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

			for (Map<String, String> topMenuMap : topMenuList) {
				if (topMenuMap.get("seaAuthYn").equals("Y")) {
					request.setAttribute("topMenuList", topMenuList);
					request.setAttribute("leftMenuList", "");
					request.setAttribute("titleMenuMap", titleMenuMap);
					// logger.debug(" ==== topMenuId leftMenuId childMenuId ALL null >>>> {}", requestURI);
					return true;
				}
			}
		}

		Map<String, Object> leftMenuMap = loginVO.getLeftMenuMap();

		// 접근로그처리
		// leftMenuId 가 있으면 leftMenuId로 접근 없는경우 childMenuId로 접근 처리
		if (leftMenuId.isEmpty()) {
			if (!childMenuId.isEmpty()) {
				logger.debug(" ==== Current MenuId >>>> child={}", childMenuId);
			}
		} else {
			logger.debug(" ==== Current MenuId >>>> left={}", leftMenuId);
		}

		// for( String key : leftMenuMap.keySet() ) {
		// logger.debug("========= leftMenuMap :: {}", key);
		// List<Map<String, String>> leftMenuList = (List<Map<String, String>>) leftMenuMap.get(key);
		// for(int j = 0; j < leftMenuList.size(); j++) {
		// Map<String, String> menuItemMap = leftMenuList.get(j);
		// logger.debug(menuItemMap);
		// }
		// }

		for (String key : leftMenuMap.keySet()) {
			List<Map<String, String>> leftMenuList = (List<Map<String, String>>) leftMenuMap.get(key);

			// logger.debug("====================================================================================================");
			// for(int j = 0; j < leftMenuList.size(); j++) {
			// Map<String, String> menuItemMap = leftMenuList.get(j);
			// logger.debug(menuItemMap);
			// }
			// logger.debug("====================================================================================================");

			for (int j = 0; j < leftMenuList.size(); j++) {
				Map<String, String> menuItemMap = leftMenuList.get(j);

				if (isEmptyMenuId) {
					String url = EgovStringUtil.nullConvert(menuItemMap.get("pgmUrl3"));

					// 매핑URL비교 json인경우 매핑url과 동일한 path명 줄것
					if (requestURI.toUpperCase().contains(url.toUpperCase())) {
						// logger.debug("==== mapping url : >>>> {},SEA_AUTH_YN:{}", url,menuItemMap.get("SEA_AUTH_YN"));
						if (menuItemMap.get("seaAuthYn").equals("Y")) {
							request.setAttribute("topMenuList", topMenuList);
							request.setAttribute("leftMenuList", leftMenuList);
							request.setAttribute("titleMenuMap", titleMenuMap);
							return true;
						} else {
							ModelAndView modelAndView = new ModelAndView("redirect:/wrks/lgn/login.do");
							throw new ModelAndViewDefiningException(modelAndView);
						}
					} // if End
				} else if (leftMenuId.isEmpty()) {
					String url = EgovStringUtil.nullConvert(menuItemMap.get("pgmMenuId"));

					if (childMenuId.toUpperCase().contains(url.toUpperCase())) {

						if (menuItemMap.get("seaAuthYn").equals("Y")) {
							request.setAttribute("topMenuList", topMenuList);
							request.setAttribute("leftMenuList", leftMenuList);
							request.setAttribute("titleMenuMap", titleMenuMap);
							return true;
						} else {
							ModelAndView modelAndView = new ModelAndView("redirect:/wrks/lgn/login.do");
							throw new ModelAndViewDefiningException(modelAndView);
						}
					}
				} else {
					String url = EgovStringUtil.nullConvert(menuItemMap.get("pgmMenuId"));
					if (leftMenuId.toUpperCase().contains(url.toUpperCase())) {
						if (menuItemMap.get("seaAuthYn").equals("Y")) {
							request.setAttribute("topMenuList", topMenuList);
							request.setAttribute("leftMenuList", leftMenuList);
							request.setAttribute("titleMenuMap", titleMenuMap);
							return true;
						} else {
							ModelAndView modelAndView = new ModelAndView("redirect:/wrks/lgn/login.do");
							throw new ModelAndViewDefiningException(modelAndView);
						}
					} // if End
				} // else End
			} // leftMenuList for End
		} // leftMenuMap for End
		
		ModelAndView modelAndView = new ModelAndView("redirect:/wrks/lgn/login.do");
		throw new ModelAndViewDefiningException(modelAndView);
*/		
	}

}
