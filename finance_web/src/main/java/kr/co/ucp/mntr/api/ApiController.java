/**
* ----------------------------------------------------------------------------------------------
* @Class Name : ApiController.java
* @Description : Api와 관련된 요청을 처리한다.
* @Version : 1.0
* Copyright (c) 2017 by KR.CO.WIDECUBE All rights reserved.
* @Modification Information
* ----------------------------------------------------------------------------------------------
* DATE AUTHOR DESCRIPTION
* ----------------------------------------------------------------------------------------------
* 2017. 10. 25. saintjuny 최초작성
*
* ----------------------------------------------------------------------------------------------
*/
package kr.co.ucp.mntr.api;

import javax.annotation.Resource;

import kr.co.ucp.mntr.api.service.ApiService;
import kr.co.ucp.mntr.api.service.ApiSrchVO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ApiController {

	@Resource(name="apiService")
	private ApiService apiService;

	/**
	 * 좌표 > 주소 변환 API
	 *
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mntr/api/coordToAddr.json")
	public ModelAndView srchCoordToAddr(@ModelAttribute ApiSrchVO vo, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		model.remove("apiSrchVO");
		mav.addObject("result", apiService.selectCoordToAddrList(vo));
		return mav;
	}

	/**
	 * 주소 > 좌표 변환 API
	 *
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mntr/api/addrToCoord.json")
	public ModelAndView srchAddrToCoord(@ModelAttribute ApiSrchVO vo, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		model.remove("apiSrchVO");
		mav.addObject("result", apiService.selectAddrToCoordList(vo));
		return mav;
	}

	/**
	 * 키워드 > 좌표 변환 API
	 *
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mntr/api/keywordToCoord.json")
	public ModelAndView srchKeywordToCoord(@ModelAttribute ApiSrchVO vo, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		model.remove("apiSrchVO");
		mav.addObject("result", apiService.selectKeywordToCoordList(vo));
		return mav;
	}

	@RequestMapping(value = "/mntr/api/mapPopup.do")
	public String viewMapPopup(ModelMap model, ApiSrchVO vo) throws Exception {
		return "blank/popup/mapPopup";
	}

	/**
	 * PNU > 지번, 도로명 주소 목록 API
	 *
	 * @param vo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mntr/api/addrListByPnu.json")
	public ModelAndView srchAddrByPnu(@ModelAttribute ApiSrchVO vo, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		model.remove("apiSrchVO");
		mav.addObject("result", apiService.selectAddrListByPnu(vo));
		return mav;
	}
}
