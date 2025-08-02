/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : MngPresetController.java
 * @Description :
 * @Version : 1.0
 * Copyright (c) 2018 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2018. 10. 19. SaintJuny 최초작성
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.mng.web;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.co.ucp.mntr.cmm.util.CommonUtil;
import kr.co.ucp.mntr.gis.service.GeoDataService;
import kr.co.ucp.mntr.mng.service.MngPresetService;
import kr.co.ucp.mntr.mng.service.MngSrchVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("mntr")
public class MngPresetController {

	@Resource(name = "mngPresetService")
	private MngPresetService mngPresetService;

	@Resource(name = "geoDataService")
	private GeoDataService geoDataService;

	@Resource(name = "commonUtil")
	private CommonUtil commonUtil;


	private final String[] commonData = { "프리셋 설정", "mng", "mngPreset" };

	/*
	 * 프리셋 설정 리스트 화면으로 이동.
	 */
	@RequestMapping("/mngPreset.do")
	public String mngPreset(@ModelAttribute MngSrchVO vo, ModelMap model) throws Exception {
		// 0: 공백, 1: 목록, 2: 상세조회, 3: 등록, 4:수정, 5: 삭제
		commonUtil.setCommonVOData(vo, 1, commonData);

		model.addAttribute("common", vo);
		return "mntr/mng/mngPreset";
	}

	@RequestMapping(value = "/selectMngPresetList.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> selectMngPresetDataList(@ModelAttribute MngSrchVO vo, ModelMap model) throws Exception {
		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);

		List<EgovMap> list = mngPresetService.selectMngPresetList(vo);

		int totCnt = commonUtil.setTotalRecordCount(list);
		paginationInfo.setTotalRecordCount(totCnt);

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		resultMap.put("totalPages", paginationInfo.getTotalPageCount());
		resultMap.put("totalRows", totCnt);
		resultMap.put("rows", list);
		resultMap.put("page", vo.getPage());
		return resultMap;
	}

	/*
	 * 프리셋 설정 화면으로 이동.
	 */
	@RequestMapping("/mngPresetReg.do")
	public String mngPresetReg(ModelMap model, @ModelAttribute MngSrchVO vo, HttpServletRequest request) throws Exception {
		commonUtil.setCommonVOData(vo, 3, commonData);

		EgovMap cctvInfo = geoDataService.selectFcltById(vo);

		model.addAttribute("cctvInfo", cctvInfo);
		model.addAttribute("common", vo);
		return "mntr/mng/mngPresetReg";
	}

	/**
	 * 프리셋 리스트
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectPresetList.json", method = RequestMethod.POST)
	public ModelAndView selectPresetList(@RequestParam Map<String, String> map) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		EgovMap result = mngPresetService.selectPresetList(map);
		mav.addObject("result", result);
		return mav;
	}

	/**
	 * 프리셋 저장
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/savePresetProc.json", method = RequestMethod.POST)
	public ModelAndView mergePresetProc(@RequestParam Map<String, String> map) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		EgovMap result = mngPresetService.mergePreset(map);
		mav.addObject("result", result);
		return mav;
	}


	/**
	 * 프리셋 삭제
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deletePresetProc.json", method = RequestMethod.POST)
	public ModelAndView deletePresetProc(@RequestParam Map<String, String> map) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		EgovMap result = mngPresetService.deletePreset(map);
		mav.addObject("result", result);
		return mav;
	}
}
