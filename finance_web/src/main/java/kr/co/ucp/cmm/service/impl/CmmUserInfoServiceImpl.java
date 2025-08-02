package kr.co.ucp.cmm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.CmmUserInfoService;
import kr.co.ucp.cmm.service.PrprtsMapper;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommUtil;
import kr.co.ucp.cmm.util.ConfigUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.ConfigureService;
import kr.co.ucp.mntr.cmm.service.ConfigureVO;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.wrks.lgn.service.LgnLoginService;

@Service("cmmUserInfoService")
public class CmmUserInfoServiceImpl implements CmmUserInfoService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="config")
	private Properties config;

	@Resource(name = "prprtsMapper")
	private PrprtsMapper prprtsMapper;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name = "lgnLoginService")
	private LgnLoginService lgnLoginService;

	@Resource(name = "configureService")
	private ConfigureService configureService;

	@Resource(name = "configUtil")
	private ConfigUtil configUtil;

	// 사용자 로그인시 기본정보,설정정보, 메뉴정보등
	@Override
	public LoginVO userConfigInfo(LoginVO loginVO, Map<String, String> argMap, HttpServletRequest request, ModelMap model) throws Exception {

		LoginVO resultVO = loginVO;
		Map<String, String> args = argMap;
		String userId = CommUtil.objNullToVal(args.get("userId"), "");
		if ("".equals(userId)) {
			return resultVO;
		}

		// 접속이력 저장
		//resultVO.setProgId("login");
		//int r = lgnLoginService.insertConnectUserCnt(resultVO);

		// Configure
		//String sysId = resultVO.getSysId();
		//String dstrtCd = prprtsService.getString("DSTRT_CD");
		
		//ConfigureVO configure = configureService.getConfigure(resultVO.getUserId(), dstrtCd, sysId);
		//configure.setNetworkIp(configUtil.getClientIp(request));
		//EgovMap configure = configureService.getUmConfigInfo();
		//configure.put("networkIp",(configUtil.getClientIp(request).toString()));

		// IP Mapping
		//Map<String, String> ipMapping = configureService.getIpMapping(configure);
		//request.getSession().setAttribute("ipMapping", ipMapping);

		//EgovMap cmConfig = configureService.getCmConfig();
		//logger.debug("--> cmConfig => "+cmConfig.toString());
		//request.getSession().setAttribute("cmConfig", cmConfig);

//		EgovMap tvoConfig = configureService.getTvoConfig();
//		logger.debug("--> tvoConfig => "+tvoConfig.toString());
//		request.getSession().setAttribute("tvoConfig", tvoConfig);

		//String gisProxyYn = "Y";
		//if (cmConfig != null && cmConfig.containsKey("gisProxyYn")) {
		//	gisProxyYn = cmConfig.get("gisProxyYn").toString();
		//}
		//ipMapping.put("gisProxyYn", gisProxyYn);

		//Map<String, String> urlMapping = configUtil.urlMapping(ipMapping);
		//request.getSession().setAttribute("urlMapping", urlMapping);

		//String gSysId = prprtsService.getString("G_SYS_ID");
		//args.put("sysId", sysId);
		//args.put("sysCd", sysId);
		//args.put("dstrtCd", dstrtCd);
		//args.put("subTitle", resultVO.getMenuSysNm());
		//args.put("menuOrdrTy", prprtsService.getString("MENU_ORDR_TY"));
		//logger.debug("--> menu 처리 >>>> {},{}", sysId, gSysId);
		
//		args.put("dbName", EgovStringUtil.nullConvert(config.get("Globals.dbName")));
		
		List<Map<String, String>> list = lgnLoginService.getMenuList(args);
		
//		List<Map<String, String>> topMenuList = new ArrayList<Map<String, String>>();
//		Map<String, Object> leftMenuMap = new HashMap<String, Object>();
//		Map<String, Object> titleMenuMap = new HashMap<String, Object>();
//
//		String menuId = "";
//		String pgmTitle = "";
//		String pgmTit1 = "";
//		String pgmTit2 = "";
//		String pgmTit3 = "";
//		List<Map<String, String>> leftMenuList = null;
//		for (int i = 0; i < list.size(); i++) {
//			Map<String, String> mpDb = list.get(i);
//
//			menuId = mpDb.get("pgmMenuId");
//			pgmTitle = CommUtil.objNullToVal(mpDb.get("pgmTitle"), "");
//			pgmTit1 = CommUtil.objNullToVal(mpDb.get("pgmTit1"), "");
//			pgmTit2 = CommUtil.objNullToVal(mpDb.get("pgmTit2"), "");
//			pgmTit3 = CommUtil.objNullToVal(mpDb.get("pgmTit3"), "");
//
//			//	logger.info("--> menu >>>> {}", mpDb.toString());
//			titleMenuMap.put(menuId, pgmTitle);
//			titleMenuMap.put(menuId + "Tit1", pgmTit1);
//			titleMenuMap.put(menuId + "Tit2", pgmTit2);
//			titleMenuMap.put(menuId + "Tit3", pgmTit3);
//			//	logger.debug("--> menu >>>> title:{}, 1:{}, 2:{}, 3:{}"
//			//			, titleMenuMap.get(menuId).toString()
//			//			, titleMenuMap.get(menuId+"Tit1").toString()
//			//			, titleMenuMap.get(menuId+"Tit2").toString()
//			//			, titleMenuMap.get(menuId+"Tit3").toString()
//			//			);
//
//			if (mpDb.get("lvl").equals("1")) {
//				if (!menuId.equals("") && leftMenuList != null && leftMenuList.size() > 0) {
//					leftMenuMap.put(menuId, leftMenuList);
//				}
//				leftMenuList = new ArrayList<Map<String, String>>();
//				//menuId = mpDb.get("pgmMenuId");
//				if (!mpDb.get("pgmId").equals("0")) {
//					leftMenuList.add(mpDb);
//				}
//				continue;
//			} // End if
//			leftMenuList.add(mpDb);
//		} // End for
//
//		if (!menuId.equals("") && leftMenuList != null && leftMenuList.size() > 0) {
//			leftMenuMap.put(menuId, leftMenuList);
//		}
//
//		boolean lvlfirst = true;
//		String title = "";
//		Map<String, String> mp1Level = null;
//		for (int i = 0; i < list.size(); i++) {
//			Map<String, String> mpDb = list.get(i);
//
//			// 시작페이지
//			String prntPgmMenuId = EgovStringUtil.nullConvert(mpDb.get("prntPgmMenuId"));
//			String lvl = EgovStringUtil.nullConvert(mpDb.get("lvl"));
//			String pgmMenuVisibleYn = EgovStringUtil.nullConvert(mpDb.get("pgmMenuVisibleYn"));
//
//			if ("d5ae0026-86b9-965a-47f5b2c8".equals(prntPgmMenuId) && "2".equals(lvl) && "Y".equals(pgmMenuVisibleYn) && "".equals(resultVO.getMainPage())) {
//				String pgmUrl = EgovStringUtil.nullConvert(mpDb.get("pgmUrl"));
//				logger.debug("==================== 메뉴 정보({}, {}, {}, {}) ====================", prntPgmMenuId, lvl, pgmMenuVisibleYn, pgmUrl);
//				resultVO.setMainPage(pgmUrl);
//			}
//
//			// 게시판 bbsId 파라미터 값예외 처리
//			int pgmId = Integer.parseInt(mpDb.get("pgmId"));
//			String bbsChk = "NO";
//			String url = "";
//			url = mpDb.get("pgmUrl");
//			if (url.indexOf("=BBS_") > 0) {
//				bbsChk = "BBS";
//			}
//
//			// 비어있으면 1레벨이므로 탑 메뉴이다.
//			if (mpDb.get("lvl").equals("1")) {
//				mp1Level = mpDb;
//				lvlfirst = true;
//			}
//			if (mpDb.get("lvl").equals("1") && !mpDb.get("pgmId").equals("0") && lvlfirst) {
//				mp1Level.put("CHILD_PGM_MENU_ID", mpDb.get("pgmMenuId"));
//				mp1Level.put("PGM_URL", mpDb.get("pgmUrl"));
//				mp1Level.put("NEW_WDW_YN", mpDb.get("newWdwYn"));
//				mp1Level.put("NEW_WIN_WIDTH", mpDb.get("newWinWidth"));
//				mp1Level.put("NEW_WIN_HEIGHT", mpDb.get("newWinHeight"));
//				mp1Level.put("PGM_MENU_VISIBLE_YN", mpDb.get("pgmMenuVisibleYn"));
//
//				mp1Level.put("BBS_CHK", bbsChk);
//				topMenuList.add(mp1Level);
//				continue;
//			}
//			else if (!mpDb.get("pgmId").equals("0") && lvlfirst) {
//				mp1Level.put("CHILD_PGM_MENU_ID", mpDb.get("pgmMenuId"));
//				mp1Level.remove("PGM_URL");
//				mp1Level.put("PGM_URL", mpDb.get("pgmUrl"));
//				mp1Level.put("NEW_WDW_YN", mpDb.get("newWdwYn"));
//				mp1Level.put("NEW_WIN_WIDTH", mpDb.get("newWinWidth"));
//				mp1Level.put("NEW_WIN_HEIGHT", mpDb.get("newWinHeight"));
//				mp1Level.put("PGM_MENU_VISIBLE_YN", mpDb.get("pgmMenuVisibleYn"));
//				mp1Level.put("BBS_CHK", bbsChk);
//				topMenuList.add(mp1Level);
//				lvlfirst = false;
//				continue;
//			}
//
//			if (mpDb.get("lvl").equals("1")) {
//				lvlfirst = true;
//			}
//
//		} // End for
		resultVO.setMenuList(list);
//		resultVO.setTopMenuList(topMenuList);
//		resultVO.setLeftMenuMap(leftMenuMap);
//		resultVO.setTitleMenuMap(titleMenuMap);
//		//resultVO.setSsl(CommUtil.objNullToVal(args.get("ssl"), ""));
		

		for (int i = 0; i < list.size(); i++) {
			if ( "3".equalsIgnoreCase(list.get(i).get("menuLevel").toString())) {
				resultVO.setMainPage(list.get(i).get("runProgram").toString());
				break;
			}
		}
		
		
		
		
		
		
		
		// Configure
		//ConfigureVO configure = configureService.getConfigure(resultVO.getUserId(), dstrtCd, sysId);
		//configure = configureService.refresh(configure, resultVO, request);
		//configure = configureService.getUmConfigInfo();
		//model.addAttribute("configure", configure);
		
		Object objConfigure = SessionUtil.getAttribute("configure");
		//ConfigureVO configureVO = (ConfigureVO) objConfigure;
		EgovMap configureVO = (EgovMap) objConfigure;
		if ( configureVO != null ) {
			logger.info("--> userConfigInfo(), sessionConfigure.getNetworkIp => "+configureVO.get("networkIp"));
		} else {
			logger.info("--> userConfigInfo(), sessionConfigure is Null !!!");
		}

		request.getSession().setAttribute("LoginVO", resultVO);
		//request.getSession().setAttribute("CfgVO", configure);
		request.getSession().setAttribute("LOAD_TIME", CommUtil.getCurrentTime14());
		request.getSession().setAttribute("RELOAD", "N");

		//EgovMap tvoConfig = configureService.getTvoConfig();
		//logger.debug("--> userConfigInfo(), tvoConfig => "+tvoConfig.toString());
		//request.getSession().setAttribute("tvoConfig", tvoConfig);
		
		logger.debug("--> userConfigInfo(), user:{},sysId:{}", resultVO.getUserId(), resultVO.getSysId());

		return resultVO;
	}
}
