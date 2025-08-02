/**
 * --------------------------------------------------------------------------------------------------------------
 * @Class Name : LgnLoginController.java
 * @Description : 일반 로그인을 처리하는 컨트롤러 클래스
 * @Version : 1.0
 * Copyright (c) 2016 by KR.CO.UCP.CNU All rights reserved.
 * @Modification Information
 * --------------------------------------------------------------------------------------------------------------
 * DATE			AUTHOR	  DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------
 * 2016. 11.08.	seungJun	최초작성
 * --------------------------------------------------------------------------------------------------------------
 */
package kr.co.ucp.wrks.lgn.web;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.google.common.base.Joiner;
import com.google.gson.Gson;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.EgovCryptoUtil;
import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.EgovUserDetailsHelper;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.SecurityAny;
import kr.co.ucp.cmm.service.CmmService;
import kr.co.ucp.cmm.service.CmmUserInfoService;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.ConfigUtil;
import kr.co.ucp.cmm.util.ScrtUtil;
import kr.co.ucp.mntr.cmm.service.ConfigureService;
import kr.co.ucp.mntr.cmm.service.ConfigureVO;
import kr.co.ucp.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.utl.sim.service.EgovFileScrty;
import kr.co.ucp.wrks.lgn.service.LgnLoginService;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.code.service.CodeDtcdService;
import kr.co.ucp.wrks.sstm.mpg.service.ConnIpService;
import kr.co.ucp.wrks.sstm.usr.service.UsrInfoService;

@Controller
@SessionAttributes("configure")
public class LgnLoginController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="config")
	private Properties config;

	@Resource(name="lgnLoginService")
	private LgnLoginService lgnLoginService;
	
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    @Resource(name="codeDtcdService")
    private CodeDtcdService codeDtcdService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	@Resource(name = "cmmService")
	private CmmService cmmService;

    @Resource(name = "configUtil")
    private ConfigUtil configUtil;

	@Resource(name="connIpService")
	private ConnIpService connIpService;

	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Resource(name="usrInfoService")
	private UsrInfoService usrInfoService;

	@Resource(name="configureService")
	private ConfigureService configureService;

	@Resource(name = "cmmUserInfoService")
	private CmmUserInfoService cmmUserInfoService;

	private WebSocketClient wsc;

	// 로그인 화면
	@RequestMapping(value="/wrks/lgn/login.do")
	//public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
	public String view(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		String ssoLoginTag = prprtsService.getString("SSO_LOGIN", "UCP");
		logger.debug("--> prprts >>>> {}:{}", "ssoLoginTag", ssoLoginTag);
		logger.debug("--> prprts >>>> {}:{}", "DSTRT_CD", prprtsService.getString("DSTRT_CD"));

		Map<String, String> args = new HashMap<>();
		args.put("dstrtCd", prprtsService.getString("DSTRT_CD"));
		//model.addAttribute("systemInfo", lgnLoginService.selectSystemInfo(args));

		//String rst = "wrks/lgn/login";
		//if ("UCP".equals(ssoLoginTag)) {
		//	rst = "wrks/lgn/login_v2";
		//} else {
		//	rst = "wrks/lgn/login_sso_" + ssoLoginTag;
		//}
		String rst = "wrks/lgn/login_v2";
		
		logger.debug("-->  /wrks/lgn/login.do >>> {}, {}", rst, ssoLoginTag);
		return rst;
	}

	@RequestMapping(value="/wrks/lgn/login.json")
	@ResponseBody
	public Map<String, Object> login(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = "";
		String pwd = "";
		String ssl = "";
		
		Map<String, String> args = new HashMap<String, String>();
		// RELOAD Y 인 경우 자동로그인 처리
		if ("Y".equals(request.getSession().getAttribute("RELOAD"))) {
			logger.info("--> login(), ======== 자동 로그인  ========");
			LoginVO lgnVO = (LoginVO) request.getSession().getAttribute("LoginVO");
			if ( lgnVO != null ) {
				userId = lgnVO.getUserId();
				args.put("LOGIN_DIRECT_YN", "Y");
				request.getSession().setAttribute("RELOAD", "N");
			} else {
				logger.info("--> login(), ======== 정상 로그인2  ========");
				userId = (String) request.getParameter("userId");
				pwd = (String) request.getParameter("pwd");
				if (null == pwd) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("msg", "비밀번호의 값이 없습니다.");
					return map;
				}
			}
		} else {
			logger.info("--> login(), ======== 정상 로그인  ========");
			userId = (String) request.getParameter("userId");
			pwd = (String) request.getParameter("pwd");
			ssl = (String) request.getParameter("ssl");
			
			if (null == pwd) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("msg", "비밀번호의 값이 없습니다.");
				return map;
			}
		}
		
//		String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
		String ssoLoginTag = prprtsService.getString("SSO_LOGIN", "UCP");
		String dbEncryptTag = prprtsService.getString("DB_ENCRYPT", "UCP");

//		if ("UCP".equals(dbEncryptTag)) {
//			pwd = EgovFileScrty.encryptPassword(pwd, saltText);
//		}
        //pwd = cmmService.getPwd(pwd, "R", userId);

		String dstrtCd = prprtsService.getString("DSTRT_CD");
		args.put("dstrtCd", dstrtCd);
		
		args.put("userId", userId);
		args.put("userPw", pwd);
		args.put("ssl", ssl);
		args.put("ssoLoginTag", ssoLoginTag);
		args.put("dbEncryptTag", dbEncryptTag);
		args.put("gSysId", prprtsService.getString("G_SYS_ID"));

		args.put("dbName", EgovStringUtil.nullConvert(config.get("Globals.dbName")));
		LoginVO resultVO = lgnLoginService.selectLogin(args);

		Map<String, Object> map = new HashMap<String, Object>();

		if (null == resultVO || null == resultVO.getUserId() || "".equals(resultVO.getUserId())) {
			map.put("session", 1);
			map.put("ret", -1);
			map.put("msg", "로그인 할 수 없습니다.");
			return map;
		}

		/*
		if (null == resultVO.getSysId() || "".equals(resultVO.getSysId())) {
			map.put("session", 1);
			map.put("ret", -1);
			map.put("msg", "사용할 수 없는 사용자입니다.");
			return map;
		}

        // 비밀번호 5회 이상 입력 오류 확인
        String loginFail = checkLoginFail(userId, resultVO);
        if (!"".equals(loginFail)) {
            map.put("session", 1);
            map.put("ret", -1);
            map.put("msg", loginFail);
            return map;
        } else {
            SessionUtil.removeAttribute("LOGIN_FAIL");
        }
		*/
		
        /*
        String ipTyCd = resultVO.getIpTyCd(); String ipCd = resultVO.getIpCd();
        String ipAdres = resultVO.getIpAdres(); boolean checkIp =
        CommonUtil.checkIp(request, ipTyCd, ipCd, ipAdres); if (!checkIp) {
           map.put("session", 1); map.put("ret", -1); map.put("msg", "접속할 수 없는 IP입니다.");
           return map;
        }
        
		// 접근가능IP 및 권한 체크
		String authLvl = resultVO.getAuthLvl();
        logger.info("--> login(), AuthLvl  : {}", authLvl);
		if ( "1".equalsIgnoreCase(authLvl) || "2".equalsIgnoreCase(authLvl)) {		// 관리자 or 승인자일 때
	        String CONN_IP_USE_YN = prprtsService.getString("CONN_IP_USE_YN");
			logger.info("--> login(), CONN_IP_USE_YN : {}", CONN_IP_USE_YN);
	        if ("Y".equalsIgnoreCase(CONN_IP_USE_YN)) {
				String clientIp = configUtil.getClientIp(request).toString();
	    		logger.info("--> login(), clientIp : {}", clientIp);
	    		
	        	String re = "fail";
	    		Map<String, String> args3 = new HashMap<String, String>();
	    		args3.put("pageNo", "1");
	    		args3.put("rowsPerPage", "99999");
	    		args3.put("sidx", "CONN_IP");
	    		args3.put("sord", "asc");
	    		List<Map<String, String>> list = connIpService.selectConnIpInfoList(args3);
	    		
	    		for ( int i=0 ; i<list.size() ; i++ ) {
	    			if ( clientIp.equalsIgnoreCase(list.get(i).get("connIp").toString())) {
	    				if ( list.get(i).get("authLvl").toString().indexOf(authLvl) != -1 ) {
	    					re = "pass";
	    				}
	    			}
	    		}
	    		logger.info("--> login(), result : {}", re);
	    		if ( "fail".equalsIgnoreCase(re) ) {
	                map.put("session", 1);
	                map.put("ret", -1);
	                map.put("msg", "현재 권한으로는 접근할 수 없는 아이피입니다.");
	                return map;
	    		}	
	        }
		}

        // 사용자 승인 사용 시 미승인건 로그인 불가,  "승인 후 사용 가능합니다." 알림처리
        // userApproveTy : 0 - 미승인, 1 - 자동승인, 2 - 수동승인
        String userApproveYn = prprtsService.getString("USER_APPROVE_YN");
        String userApproveTy = resultVO.getUserApproveTy();
        if ("Y".equals(userApproveYn) && !("1".equals(userApproveTy) || "2".equals(userApproveTy))) {
            map.put("session", 1);
            map.put("ret", -1);
            map.put("msg", "승인 후 사용 가능합니다.");
            return map;
        }
        */

		resultVO = cmmUserInfoService.userConfigInfo(resultVO, args, request, model);
		
		if (resultVO != null) {
			String mainPage = resultVO.getMainPage();
			logger.debug("==================== 메인 페이지({}) ====================", mainPage);
			map.put("session", 1);
			map.put("ret", 1);
			map.put("msg", "");
			map.put("redirect", mainPage);
		}
		//logger.debug("--> main redirect >>>> {}", resultVO.getMainPage());
		return map;
	}

	@RequestMapping(value="/wrks/lgn/login_sso.do")
	public String view_sso(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		return "wrks/lgn/login_sso";
	}

	@RequestMapping(value="/wrks/lgn/redirect.do")
	public String view_redirect(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		logger.debug("******** redirect start ******** {}", "login");
		return "wrks/lgn/redirect";
	}

	@RequestMapping(value="/wrks/lgn/goHome.do")
	public String goHome(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		logger.debug("======== goHome ======== {}", "goHome");
		return "wrks/lgn/goHome";
	}

	// 내정보상세화면으로 들어간다
	@RequestMapping(value="/wrks/lgn/myinfo.do")
	public String myInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		// 사용자정보
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, String> args = new HashMap<String, String>();
		// 2016.06.20 추가 (윤법상)
		args.put("sysId", lgnVO.getSysId());
		args.put("USER_ID", sesUserId);
		args.put("USER_NM_KO", "");
		args.put("USE_TY_CD", "Y");
		args.put("MOBL_NO", "");
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "15");
		args.put("sidx"       , "USER_ID");
		args.put("sord"       , "ASC");

//		List<Map<String, String>> lst = usrInfoService.list(args);
//		request.setAttribute("myinfoList", lst);
//		request.setAttribute("myinfoListJson", new Gson().toJson(lst));
		Map<String, String> myinfo = usrInfoService.selectUserDtl(args);
		request.setAttribute("myinfo", myinfo);
		return "wrks/lgn/myinfo";
	}

	// 사이트맵
	@RequestMapping(value="/wrks/lgn/sitemap.do")
	public String siteMap(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("menuList", lgnVO);
		return "wrks/lgn/sitemap";
	}

	// 메뉴목록 조회
	@RequestMapping(value="/wrks/lgn/getmenu.json")
	@ResponseBody
	public Map<String, Object> getMenuList(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pwd = (String) request.getParameter("pwd");
		String userId = (String) request.getParameter("userId");
		String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
		String ssoLoginTag = prprtsService.getString("SSO_LOGIN", "UCP");
		String dbEncryptTag = prprtsService.getString("DB_ENCRYPT", "UCP");

		if ("UCP".equals(dbEncryptTag)) {
			pwd = EgovFileScrty.encryptPassword(pwd, saltText);
		}

		Map<String, String> args = new HashMap<String, String>();
		args.put("userId", userId);
		args.put("password", pwd);
		args.put("ssoLoginTag", ssoLoginTag);
		args.put("dbEncryptTag", dbEncryptTag);

		LoginVO loginVO = new LoginVO();
		loginVO = lgnLoginService.selectLogin(args);

		Map<String, Object> map = new HashMap<String, Object>();
		if (null == loginVO.getUserId() || loginVO.getUserId().equals("")) {
			map.put("session", 1);
			map.put("ret", -1);
			map.put("msg", "메뉴를 가져올수 없습니다.");

		}
		else {
//			String ipTyCd = loginVO.getIpTyCd();
//			String ipCd = loginVO.getIpCd();
//			String ipAdres = loginVO.getIpAdres();
//			boolean bbb = CommonUtil.checkIp(request, ipTyCd, ipCd, ipAdres);
//			if (!bbb) {
//				map.put("session", 1);
//				map.put("ret", -1);
//				map.put("msg", "접속할 수 없는 IP입니다.");
//				return map;
//			}

			List<Map<String, String>> topMenuList = new ArrayList<Map<String, String>>();
			Map<String, Object> leftMenuMap = new HashMap<String, Object>();

			args.put("sysCd", loginVO.getSysId());
			args.put("subTitle", loginVO.getMenuSysNm());
			List<Map<String, String>> list = lgnLoginService.getMenuList(args);

			String menuId = "";
			List<Map<String, String>> leftMenuList = null;
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> mpDb = list.get(i);

				if (mpDb.get("LVL").equals("1")) {
					if (!menuId.equals("") && leftMenuList != null && leftMenuList.size() > 0) {
						leftMenuMap.put(menuId, leftMenuList);
					}
					leftMenuList = new ArrayList<Map<String, String>>();
					menuId = mpDb.get("PGM_MENU_ID");
					if (!mpDb.get("PGM_ID").equals("0")) {
						leftMenuList.add(mpDb);
					}
					continue;
				} // End if

				leftMenuList.add(mpDb);
			} // End for

			if (!menuId.equals("") && leftMenuList != null && leftMenuList.size() > 0) {
				leftMenuMap.put(menuId, leftMenuList);
			}

			boolean lvlfirst = true;
			String title = "";
			Map<String, String> mp1Level = null;
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> mpDb = list.get(i);
				// 게시판 bbsId 파라미터 값예외 처리
				int pgmId = Integer.parseInt(mpDb.get("PGM_ID"));
				String bbsChk = "NO";
				String url = "";
				url = mpDb.get("PGM_URL");
				if (url.indexOf("=BBS_") > 0) {
					bbsChk = "BBS";
				}

				// 비어있으면 1레벨이므로 탑 메뉴이다.
				if (mpDb.get("LVL").equals("1")) {
					mp1Level = mpDb;
					lvlfirst = true;
				}
				if (mpDb.get("LVL").equals("1") && !mpDb.get("PGM_ID").equals("0") && lvlfirst) {
					mp1Level.put("CHILD_PGM_MENU_ID", mpDb.get("PGM_MENU_ID"));
					mp1Level.put("PGM_URL", mpDb.get("PGM_URL"));
					mp1Level.put("NEW_WDW_YN", mpDb.get("NEW_WDW_YN"));
					mp1Level.put("NEW_WIN_WIDTH", mpDb.get("NEW_WIN_WIDTH"));
					mp1Level.put("NEW_WIN_HEIGHT", mpDb.get("NEW_WIN_HEIGHT"));
					mp1Level.put("PGM_MENU_VISIBLE_YN", mpDb.get("PGM_MENU_VISIBLE_YN"));
					mp1Level.put("BBS_CHK", bbsChk);
					topMenuList.add(mp1Level);
					continue;
				}
				else if (!mpDb.get("PGM_ID").equals("0") && lvlfirst) {
					mp1Level.put("CHILD_PGM_MENU_ID", mpDb.get("PGM_MENU_ID"));
					mp1Level.remove("PGM_URL");
					mp1Level.put("PGM_URL", mpDb.get("PGM_URL"));
					mp1Level.put("NEW_WDW_YN", mpDb.get("NEW_WDW_YN"));
					mp1Level.put("NEW_WIN_WIDTH", mpDb.get("NEW_WIN_WIDTH"));
					mp1Level.put("NEW_WIN_HEIGHT", mpDb.get("NEW_WIN_HEIGHT"));
					mp1Level.put("PGM_MENU_VISIBLE_YN", mpDb.get("PGM_MENU_VISIBLE_YN"));
					mp1Level.put("BBS_CHK", bbsChk);
					topMenuList.add(mp1Level);
					lvlfirst = false;
					continue;
				}

				if (mpDb.get("LVL").equals("1")) {
					lvlfirst = true;
				}
			} // End for

			loginVO.setTopMenuList(topMenuList);
			loginVO.setLeftMenuMap(leftMenuMap);
			request.getSession().setAttribute("LoginVO", loginVO);
			map.put("session", 1);
			map.put("ret", 1);
			map.put("msg", "");
			map.put("redirect", "wrks/main/main.do");
		} // End else
		return map;
	}

	// 회원가입 신청 화면
	@RequestMapping(value = "/wrks/lgn/selfrgsuser.do")
	public String apply(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, String> args2 = new HashMap<>();
		args2.put("dstrtCd", prprtsService.getString("DSTRT_CD"));
		String repTelNo = lgnLoginService.selectRepTelNo(args2);
		model.addAttribute("repTelNo", repTelNo);

		Map<String, Object> args = new HashMap<>();
		
		/* 기관명 */
        args.clear();
		args.put("cdGrpId", "USER_INSTT_NM");
		args.put("prntGrpId", prprtsService.getString("DSTRT_CD"));
		args.put("cdTy", "C");
		args.put("orderBy", "ORDER BY CD_NM_KO ASC");
        List<Map<String, String>> userInsttList = codeCmcdService.grpList(args);
        request.setAttribute("userInsttList", userInsttList);

        //request.setAttribute("dstrtCd", prprtsService.getString("DSTRT_CD"));
        
		//args.put("sysId", prprtsService.getString("G_SYS_ID"));
		//args.put("dstrtCd", prprtsService.getString("DSTRT_CD"));
		//model.addAttribute("systemInfo", lgnLoginService.selectSystemInfo(args));
		
		return "wrks/lgn/selfrgsuser";
	}

	// 회원가입 신청
	@RequestMapping(value = "/wrks/lgn/apply.json")
	@ResponseBody
	public Map<String, Object> apply(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// DBEncrypt 적용 (Egov)
		String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
		String ssoLoginTag = prprtsService.getString("SSO_LOGIN", "UCP");
		String dbEncryptTag = prprtsService.getString("DB_ENCRYPT", "UCP");

		String dstrtCd = prprtsService.getString("DSTRT_CD");
		String userId = request.getParameter("userId");
		String password = (String) request.getParameter("password");
		String insttCd = (String) request.getParameter("insttCd");
		String insttNmInput = (String) request.getParameter("insttNmInput");

		Map<String, Object> mapRet = new HashMap<String, Object>();
		
		Map<String, Object> mapPara = new HashMap<String, Object>();
		mapPara.put("USER_ID" , userId);
		int userIdCnt = usrInfoService.selectUserIdCnt(mapPara);
		//System.out.println("userIdCnt => "+userIdCnt);
		if ( userIdCnt != 0 ) {	
			mapRet.put("session", 1);
			mapRet.put("ret", -1);
			mapRet.put("msg", "이미 등록된 아이디입니다.");
			//System.out.println("msg => "+mapRet.get("msg"));
			return mapRet;
		}

		if ( "".equalsIgnoreCase(insttCd)) {	// 소속기관을 선택하지 않았을 때
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("insttNm", insttNmInput);
			paraMap.put("dstrtCd", dstrtCd);
			Map<String, String> insttMap = usrInfoService.registerInsttInfo(paraMap);	// 소속기관을 등록하고 아이디를 가져온다.
			insttCd = insttMap.get("cdId").toString();
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DSTRT_CD", dstrtCd);
		map.put("USER_ID", userId);
		map.put("USER_NM_KO", (String)request.getParameter("userNmKo"));
		map.put("USER_NM_EN", "");
		map.put("USE_TY_CD", "Y");
		map.put("MOBL_NO", (String)request.getParameter("moblNo"));
		map.put("EMAIL", (String)request.getParameter("email"));
		map.put("OFFC_TEL_NO", (String)request.getParameter("offcTelNo"));
		//map.put("insttNm", insttNm);
		map.put("INSTT_CD", insttCd);
		map.put("DEPT_NM", (String)request.getParameter("deptNm"));
		map.put("RANK_NM", (String)request.getParameter("rankNm"));
		map.put("RPSB_WORK", (String)request.getParameter("rpsbWork"));
		map.put("REMARK", (String)request.getParameter("remark"));
		map.put("RGS_USER_ID", userId);

//		Map<String, String> para = new HashMap<String, String>();
//		para.put("CD_GRP_ID", "USER_INSTT_NM");
//		para.put("USE_TY_CD", "Y");
//		para.put("CD_NM_KO", insttNm);
//		para.put("pageNo", "1");
//		para.put("rowsPerPage", "1000");
//		para.put("sidx", "cd_id");
//		para.put("sord", "asc");
//		
//		List<Map<String, String>> insttList = codeDtcdService.dtcdList(para);
//		if ( insttList.size()==0) {		// 등록되지 않은 기관명일 때
//			mapRet.put("session", 1);
//			mapRet.put("ret", -1);
//			mapRet.put("msg", "등록되지 않은 소속기관입니다.\n관리자에게 소속기관 등록을 먼저 요청하세요.");
//			//System.out.println("msg => "+mapRet.get("msg"));
//			return mapRet;
//		} else {
//			map.put("INSTT_CD", insttList.get(0).get("cdId").toString());	// 소속기관아이디
//		}

		map.put("dbEncryptTag", dbEncryptTag);
		map.put("ssoLoginTag", ssoLoginTag);

//		// DBEncrypt 적용 (Egov)
//		if ("UCP".equals(dbEncryptTag)){
//			map.put("PASSWORD", EgovFileScrty.encryptPassword(password, saltText));
//		}else{
//			map.put("PASSWORD", password);
//		}
        String pwd = cmmService.getPwd(password, "C", "");
        map.put("PASSWORD", pwd);

		// play password
		String playPwd = new String(Base64.encodeBase64(password.getBytes()));		//System.out.println("== playPwd => "+playPwd);
		String playPwdDec = new String(Base64.decodeBase64(playPwd.getBytes()));	//System.out.println("== playPwdDec => "+playPwdDec);
		map.put("playPwd", playPwd);

        map.put("userApproveYn", prprtsService.getString("USER_APPROVE_YN")); // 사용자승인 사용여부

		if (!CommonUtil.checkDataFilterObj(map))
		{	mapRet.put("session", 1);
			mapRet.put("ret", -1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}
		
		
		
        // 그룹 및 레벨 지정
		List<Map<String, String>> grplist = new ArrayList<Map<String, String>>();
		Map<String, String> map_item_id = new HashMap<String, String>();
		map_item_id.put("DSTRT_CD", dstrtCd);
		map_item_id.put("GRP_ID", "PVEPOLICE");	// 경찰
		map_item_id.put("USER_ID", userId);
		map_item_id.put("USE_TY_CD", "Y");
		map_item_id.put("AUTH_LVL",  "3");		// 신청
		map_item_id.put("RGS_USER_ID", userId);
		grplist.add(map_item_id);

		Map<String, String> dstrtMap = new HashMap<String, String>();
		dstrtMap.put("DSTRT_CD", dstrtCd);
		dstrtMap.put("USER_ID", userId);
		dstrtMap.put("RGS_USER_ID", userId);


		// 사용자등록
		int insertResult = 0;
		insertResult = usrInfoService.insertUser(map, grplist, dstrtMap);

		if(insertResult == 0) {
			mapRet.put("session", 1);
			mapRet.put("ret", -1);
			mapRet.put("msg", "실패하였습니다.");
			return mapRet;
		} else {
			mapRet.put("session", 1);
			mapRet.put("ret", 1);
			mapRet.put("msg", "신청이 완료되었으니\n관리자에게 승인을 요청하시기 바랍니다.");
		}

		// 승인처리알림(영상반출-승인레벨 사용자) - 화면알림
		String approveNotifyTy = prprtsService.getString("APPROVE_NOTIFY_TY");	// 승인알림구분
		if ( "Y".equals(approveNotifyTy) ) {
			JSONObject event = new JSONObject();
			JSONObject data = new JSONObject();

			
			

			event.put("evtId", "APPROVE");
			event.put("data", data);
			//websocketClient(event.toString(), (String) request.getParameter("wsUrl"));
			websocketClient(event.toString());
		}
		
		return mapRet;
	}

	// 비밀번호 찾기 화면
	@RequestMapping(value = "/wrks/lgn/findpwd.do")
	public String findPwd(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		Map<String, String> args = new HashMap<>();
		args.put("sysId", prprtsService.getString("G_SYS_ID"));
		args.put("dstrtCd", prprtsService.getString("DSTRT_CD"));
		//model.addAttribute("systemInfo", lgnLoginService.selectSystemInfo(args));
		return "wrks/lgn/findpwd_v2";
	}

	// 비밀번호 찾기
	@RequestMapping(value = "/wrks/lgn/findpwd.json")
	@ResponseBody
	public Map<String, Object> findPwd(ModelMap model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String userId = request.getParameter("userId");
		String userNm = (String) request.getParameter("userNm");
		//String moblNo = (String) request.getParameter("moblNo").replaceAll("-", "");
		String moblNo = (String) request.getParameter("moblNo");

		Map<String, String> args = new HashMap<String, String>();
		args.put("userId", userId);
		args.put("userNm", userNm);
		args.put("moblNo", moblNo);

		LoginVO resultVO = lgnLoginService.findPwd(args);

		Map<String, Object> map = new HashMap<String, Object>();
		if (resultVO.getUserId() == null || "".equals(resultVO.getUserId())) {
			map.put("session", 1);
			map.put("ret", -1);
			map.put("msg", "사용자 정보를 찾을 수 없습니다.");
		} else {
			request.getSession().setAttribute("LoginVO", resultVO);
			String pw = EgovStringUtil.nullConvert(cmmService.cmmChgPw(userId).get("newPwd"));
			String smsMsg = userId + " password:" + pw;
			String socketOpt = prprtsService.getString("ADAPT_SMS").trim();
			List<String> rcvList = new ArrayList<String>();
			rcvList.add(moblNo);
			List<String> listRcvNm = new ArrayList<String>();
			listRcvNm.add(userNm);
			CommonUtil.smsSend(socketOpt, moblNo, smsMsg, rcvList, listRcvNm);
			map.put("session", 1);
			map.put("ret", 1);
			map.put("msg", "등록된 모바일번호로 임시발급한 비밀번호를 빌송하였으니\n로그인 하신 후 비밀번호를 변경하시기 바랍니다.");
		}
		return map;
	}

	// 비밀번호 변경화면
	@RequestMapping(value="/wrks/lgn/changepwd.do")
	public String changePwd(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		return "wrks/lgn/changepwd";
	}

	// 비밀번호 변경
	@RequestMapping(value="/wrks/lgn/changepwd.json")
	@ResponseBody
	public Map<String, Object> changePwd(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String pwdOld = (String) request.getParameter("pwdOld");
		String pwdNew = (String) request.getParameter("pwdNew");

		Map<String, String> args = new HashMap<String, String>();
		String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
		String ssoLoginTag = prprtsService.getString("SSO_LOGIN", "UCP");
		String dbEncryptTag = prprtsService.getString("DB_ENCRYPT", "UCP");

		if ("UCP".equals(dbEncryptTag)) {
			pwdOld = EgovFileScrty.encryptPassword(pwdOld, saltText);
		}

		args.put("userId", lgnVO.getUserId());
		args.put("password", pwdOld);
		//args.put("ucpSiteTag", ucpSiteTag);
		args.put("ssoLoginTag", ssoLoginTag);
		args.put("dbEncryptTag", dbEncryptTag);

		LoginVO loginVO = null;
		loginVO = lgnLoginService.selectLogin(args);
		Map<String, Object> map = new HashMap<String, Object>();
		if (null == loginVO.getUserId() || loginVO.getUserId().equals("")) {
			map.put("session", 1);
			map.put("ret", -1);
			map.put("msg", "이전 비밀번호가 틀렸습니다.");

		}
		else {
			args.clear();

			// player password
			//String playPwd = SecurityAny.encryptSHA256(pwdNew);
			String playPwd = new String(Base64.encodeBase64(pwdNew.getBytes()));
			//System.out.println("== playPwd => "+playPwd);
			String playPwdDec = new String(Base64.decodeBase64(playPwd.getBytes()));
			//System.out.println("== playPwdDec => "+playPwdDec);
			args.put("playPwd", playPwd);
			
			if ("UCP".equals(dbEncryptTag)) {
				pwdNew = EgovFileScrty.encryptPassword(pwdNew, saltText);
			}
			args.put("userId", lgnVO.getUserId());
			args.put("updUserId", lgnVO.getUserId());
			args.put("password", pwdNew);
			lgnLoginService.changePwd(args);

			map.put("session", 1);
			map.put("ret", 1);
			map.put("msg", "비밀번호를 수정하였습니다.");
		}

		return map;
	}

	// 로그아웃한다.
	@RequestMapping(value="/wrks/lgn/logout.do")
	public String actionLogout(HttpServletRequest request, ModelMap model) throws Exception {
		try {
			RequestContextHolder.getRequestAttributes().removeAttribute("LoginVO", RequestAttributes.SCOPE_SESSION);
		}
		catch (Exception e) {
			leaveaTrace.trace("fail.common.msg", this.getClass());
		}

		String ssoLoginTag = prprtsService.getString("SSO_LOGIN", "UCP");
		if (ssoLoginTag.equals("YES")) {
			logger.debug("******** {}  ********", "sso logout");
			return "wrks/lgn/logout_sso";
		}
		else {
			logger.debug("******** {}  ********", "logout");
			return "forward:/wrks/lgn/login.do";
		}
	}
	

	//private void websocketClient(String wsData, String wsUrl) throws Exception {
	private void websocketClient(String wsData) throws Exception {

        String tvoUrl = prprtsService.getString("PVE_URL");
		String uri = tvoUrl+"/ws/evt.do?page=prvdApprv";
		logger.info("--> websocketClient(), uri => {}", uri);
		
		wsc = new WebSocketClient(new URI(uri)) {
			@Override
			public void onOpen(ServerHandshake handshakedata) {
				logger.info("websocketClient.onOpen: {}", handshakedata);
				this.send(wsData);
			}

			@Override
			public void onMessage(String message) {
				logger.info("websocketClient.onMessage: {}", message);
			}

			@Override
			public void onClose(int code, String reason, boolean remote) {
				logger.info("websocketClient.onClose: {}", reason);
				wsc.close();
			}

			@Override
			public void onError(Exception ex) {
				logger.info("websocketClient.onError: {}", ex.getMessage());
				wsc.close();
			}
		};
		wsc.connect();
	}
	
}
