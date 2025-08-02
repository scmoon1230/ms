/**
 * ----------------------------------------------------------------------------------------------
/ * @Class Name : MngDisplayController.java
 * @Description :
 * @Version : 1.0
 * Copyright (c) 2018 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2018. 10. 04. SaintJuny 최초작성
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.mng.web;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.ConfigureService;
import kr.co.ucp.mntr.cmm.service.ConfigureVO;
import kr.co.ucp.mntr.cmm.util.CommonUtil;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.mntr.gis.service.GeoDataService;
import kr.co.ucp.mntr.mng.service.MngPrprtsService;

@Controller
@SessionAttributes("configure")
public class MngDisplayController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="commonUtil")
	private CommonUtil commonUtil;

	@Resource(name="configureService")
	private ConfigureService configureService;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name = "geoDataService")
	private GeoDataService geoDataService;

	@Resource(name="mngPrprtsService")
	private MngPrprtsService mngPrprtsService;

	//@Resource(name="propertiesService")
	//protected EgovPropertyService propertiesService;

//	// 사용자화면설정 열기
//	@RequestMapping(value="/mntr/mngDisplay.do")
//	public String viewMngDisplay(@ModelAttribute("configure") ConfigureVO configure, ModelMap model) throws Exception {
//		//logger.info("--> viewMngDisplay(): {}", configure.toString());
//		String[] commonData = { "사용자화면설정", "mng", "mngDisplay" };
//		
//		ConfigureVO umConfigInfo = configureService.getUmConfigure(configure);
//		model.addAttribute("configureVO", umConfigInfo);
//
//		commonUtil.setCommonVOData(umConfigInfo, 0, commonData);
//		model.addAttribute("common", umConfigInfo);
//		return "nomap/mng/mngDisplay";
//	}

//	// 사용자화면설정 저장
//	@RequestMapping(value="/mntr/saveMngDisplay.json")
//	public ModelAndView saveMngDisplay(@ModelAttribute("configure") ConfigureVO configure, ModelMap model, HttpServletRequest request) throws Exception {
//		ModelAndView mav = new ModelAndView("jsonView");
//		
////		configureService.updateUmConfigure(configure);
//		mav.addObject("result", "ok");
//		mav.addObject("status", "1");
//
//		ConfigureVO tmpConfigure = configureService.refresh(configure, SessionUtil.getUserInfo(), request);
//		model.addAttribute("configure", tmpConfigure);
//		return mav;
//	}

	// 공통화면설정 열기
	@RequestMapping(value="/mntr/mngCmmDisplay.do")
	public String viewMngCmmDisplay(@RequestParam Map<String, String> params, ModelMap model) throws Exception {
		//logger.info("--> viewMngCmmDisplay(): {}", configure.toString());
		//String[] commonData = { "공통화면설정", "mng", "mngDisplay" };
		
		//EgovMap umConfig = configureService.getUmConfigInfo();
		
		//umConfigInfo.put("gisEngine",prprtsService.getString("GIS_ENGINE"));
		//umConfigInfo.put("webSocketSoundUseYn",prprtsService.getString("WEB_SOCKET_SOUND_USE_YN"));
		//umConfigInfo.put("menuOrdrTy",prprtsService.getString("MENU_ORDR_TY"));
		//umConfigInfo.put("userApproveYn",prprtsService.getString("USER_APPROVE_YN"));

		//logger.debug("--> umConfig => "+umConfig.toString());
		
		//model.addAttribute("umConfig", umConfig);

		//commonUtil.setCommonVOData(tmpConfigure, 0, commonData);
		//model.addAttribute("common", tmpConfigure);
		
		return "nomap/mng/mngCmmDisplay";
	}

	// 공통화면설정 저장
	@RequestMapping(value="/mntr/saveMngCmmDisplay.json", method = RequestMethod.POST)
	public ModelAndView saveMngCmmDisplay(@RequestParam Map<String, String> params, ModelMap model, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		logger.debug("--> saveMngCmmDisplay(), params  => {}",params.toString());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("PRPRTS_ID", prprtsService.getString("DSTRT_CD"));
		map.put("PRPRTS_TY", "UM_CONFIG");

		for(Entry<String, String> elem : params.entrySet()){
			System.out.println("key : " + elem.getKey() + ", value : " + elem.getValue());
			map.put("PRPRTS_KEY", elem.getKey());
			map.put("PRPRTS_VAL", elem.getValue());
			int updateResult = mngPrprtsService.update(map);
			mav.addObject("result", "ok");
			mav.addObject("status", String.valueOf(updateResult));
		}

		prprtsService.reloadPrprts(request);

		//EgovMap umConfig = configureService.getUmConfigInfo();
		//logger.debug("--> umConfig => "+umConfig.toString());
		//request.getSession().setAttribute("umConfig", umConfig);
		
		request.getSession().setAttribute("RELOAD", "Y");
		
		return mav;
	}

	@RequestMapping("/mntr/saveMngCmmDisplayProjection.json")
	public ModelAndView saveMngCmmDisplayProjection(ConfigureVO configure, ModelMap model, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		String gisEngine = prprtsService.getString("GIS_ENGINE");
		String gisEngineConf = configure.getGisEngine();
		logger.info("--> saveMngCmmDisplayProjection() => gisEngine:{}, gisEngineConf:{}", gisEngine, gisEngineConf);

//		if (!"".equals(gisEngineConf) && !gisEngine.equals(gisEngineConf)) {
//			Map<String, Object> map = new HashMap<>();
//			//map.put("prprtsId", prprtsService.getGlobals("Globals.PrprtsId"));
//			map.put("prprtsId", prprtsService.getString("DSTRT_CD"));
//			
//			map.put("prprtsKey", "GIS_ENGINE");
//			map.put("prprtsVal", gisEngineConf);
//			prprtsService.updatePrprts(map);
//		}
		
		// projection point 업데이트 처리
		logger.info("{} 투사도 좌표 업데이트 ", gisEngine);
		geoDataService.updatePointProjection(gisEngine);
		
		mav.addObject("result", "ok");
		mav.addObject("session", 1);
		return mav;
	}

	public String getClientIp(HttpServletRequest request) {
		String clientIp = request.getHeader("X-Forwarded-For");

		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("Proxy-Client-IP");
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("WL-Proxy-Client-IP");
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("HTTP_CLIENT_IP");
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getRemoteAddr();
		}
		return clientIp;
	}

}
