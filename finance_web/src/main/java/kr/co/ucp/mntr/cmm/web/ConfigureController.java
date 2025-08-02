/**
 * ----------------------------------------------------------------------------------------------
/ * @Class Name : ConfigureController.java
 * @Description :
 * @Version : 1.0
 * Copyright (c) 2014 by KR.CO.UCP.CNU All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2015. 10. 22. SaintJuny 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.cmm.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.CommonVO;
import kr.co.ucp.mntr.cmm.service.ConfigureService;
import kr.co.ucp.mntr.cmm.service.ConfigureVO;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.mntr.mng.service.MngPrprtsService;

@Controller
public class ConfigureController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="configureService")
	private ConfigureService configureService;

    @Resource(name = "prprtsService")
    private PrprtsService prprtsService;

	@Resource(name="mngPrprtsService")
	private MngPrprtsService mngPrprtsService;

	// 영상반출환경설정 열기
	@RequestMapping(value="/tvo/main/config.do")
	public String config(@ModelAttribute("loginVO") LoginVO loginVO, @ModelAttribute CommonVO vo, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		//관리자메인화면 환경설정
		//EgovMap tvoConfig = configureService.getTvoConfig();
		//logger.debug("--> "+tvoConfig.toString());
		//request.setAttribute("tvoConfig", tvoConfig);
		//model.addAttribute("tvoConfig", tvoConfig);

		return "tvo/config/config";
	}

	// 영상반출환경설정 저장
	@RequestMapping(value="/tvo/config/savePveConfig.json", method = RequestMethod.POST)
	public ModelAndView savePveConfig(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		logger.debug("--> savePveConfig(), params  => {}",params.toString());
		
		Map<String, String> result = new HashMap<String, String>();
		
//		result = configureService.updateConfig(params);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("PRPRTS_ID", prprtsService.getString("DSTRT_CD"));
		map.put("PRPRTS_TY", "TVO_CONFIG");

		for(Entry<String, String> elem : params.entrySet()){
			System.out.println("key : " + elem.getKey() + ", value : " + elem.getValue());
			map.put("PRPRTS_KEY", elem.getKey());
			map.put("PRPRTS_VAL", elem.getValue());
			int updateResult = mngPrprtsService.update(map);
			result.put("result", String.valueOf(updateResult));
		}
		
		prprtsService.reloadPrprts(request);

		//EgovMap tvoConfig = configureService.getTvoConfig();
		//logger.debug("--> tvoConfig => "+tvoConfig.toString());
		//request.getSession().setAttribute("tvoConfig", tvoConfig);
		
        request.getSession().setAttribute("RELOAD", "Y");
		
		mav.addObject("result", result);
		
		return mav;
	}

	
	
	
	
	
	
	
	@RequestMapping(value="/mntr/selectUmConfigInfo.json")
	public ModelAndView selectUmConfigInfo(@ModelAttribute("configure") ConfigureVO configure, ModelMap model, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		EgovMap tmpConfigure = configureService.getUmConfigInfo();
		
		model.remove("configure");
		mav.addObject("result", "ok");
		mav.addObject("status", "1");
		mav.addObject("configureVO", tmpConfigure);
		return mav;
	}
	
	
	
	
	
	
	
	
	
	
    @RequestMapping("/mntr/prprtsReload.json")
    public ModelAndView prprtsReload(@ModelAttribute("configure") ConfigureVO configure, ModelMap model, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("jsonView");
        String prprts = EgovStringUtil.nullConvert(request.getParameter("prprts"));
        logger.debug("--> prprts >>>> {}", prprts);
        if (!"".equals(prprts)) {
            prprtsService.reloadPrprts(request);
            logger.debug("--> 기본속성reload >>>> ");
        }
        mav.addObject("result", "ok");
        mav.addObject("session", 1);
        return mav;
    }

//    @RequestMapping("/updateLoginDplctn.json")
//    public ModelAndView updateLoginDplctn(@RequestParam Map<String, Object> params, ModelMap model) throws Exception {
//        LoginVO loginVO = SessionUtil.getUserInfo();
//        loginVO.setLoginDplctnYn(EgovStringUtil.nullConvert(params.get("loginDplctnYn")));
//        ModelAndView mav = new ModelAndView("jsonView");
//        int r = configureService.updateLoginDplctn(loginVO);
//        if (r == 1) {
//            mav.addObject("session", 1);
//            SessionUtil.setAttribute("LoginVO", loginVO);
//        } else {
//            mav.addObject("session", 0);
//        }
//        return mav;
//    }
    
    
    
    
    
}
