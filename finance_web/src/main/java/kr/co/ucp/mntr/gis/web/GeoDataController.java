/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : GeoDataController.java
 * @Description : 지도 데이터를 제어한다.
 * @Version : 1.0
 * Copyright (c) 2014 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2015. 10. 28. SaintJuny@ubolt.co.kr 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.gis.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.ConfigureService;
import kr.co.ucp.mntr.cmm.service.ConfigureVO;
import kr.co.ucp.mntr.cmm.service.FcltVO;
import kr.co.ucp.mntr.cmm.util.CommonUtil;
import kr.co.ucp.mntr.gis.service.GeoDataService;
import kr.co.ucp.mntr.gis.util.GisUtil;

@Controller
@SessionAttributes("configure")
public class GeoDataController {
	//private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="geoDataService")
	private GeoDataService geoDataService;

	@Resource(name="configureService")
	private ConfigureService configureService;

    @Resource(name = "prprtsService")
    private PrprtsService prprtsService;

	@Resource(name="commonUtil")
	private CommonUtil commonUtil;

	@Resource(name="gisUtil")
	private GisUtil gisUtil;

	/* 시설물 위치정보 데이터를 가져온다. */
	@RequestMapping(value="/mntr/fcltGeoData.json", method = RequestMethod.POST)
	//public @ResponseBody Map<String, Object> getFcltGeoData(@ModelAttribute FcltVO vo, @ModelAttribute("configure") ConfigureVO configure) throws Exception {
	public @ResponseBody Map<String, Object> getFcltGeoData(@ModelAttribute FcltVO vo, @ModelAttribute("map") EgovMap map) throws Exception {
		
		List<EgovMap> list = new ArrayList<EgovMap>();
		String searchIgnoreScaleYn = EgovStringUtil.nullConvert(vo.getSearchIgnoreScaleYn());
		
		if ("Y".equals(searchIgnoreScaleYn)) {
			list = geoDataService.selectFcltGeoData(vo);
			
		} else {
			//String gisFeatureViewScale = configure.getGisFeatureViewScale();
			String gisFeatureViewScale = prprtsService.getString("GIS_FEATURE_VIEW_SCALE");
			
			String mapScale = vo.getScale();
			boolean isNumberGisLabelViewScale = NumberUtils.isNumber(gisFeatureViewScale);
			boolean isNumberMapScale = NumberUtils.isNumber(mapScale);
			if (isNumberGisLabelViewScale && isNumberMapScale) {
				double nGisLabelViewScale = Double.parseDouble(gisFeatureViewScale);
				double nMapScale = Double.parseDouble(mapScale);
				if (nGisLabelViewScale > nMapScale) {
					list = geoDataService.selectFcltGeoData(vo);
				}
			}
		}
		return gisUtil.createGeoJson(list, "pointX", "pointY");
	}

	/* 카메라 방향각 데이터를 가져온다. */
	@RequestMapping(value="/mntr/fcltAngleGeoData.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getFcltAngleGeoData(@ModelAttribute FcltVO vo, @ModelAttribute("configure") ConfigureVO configure) throws Exception {

		String gisFeatureViewScale = configure.getGisFeatureViewScale();
		String mapScale = vo.getScale();

		List<EgovMap> list = new ArrayList<EgovMap>();
		boolean isNumberGisLabelViewScale = NumberUtils.isNumber(gisFeatureViewScale);
		boolean isNumberMapScale = NumberUtils.isNumber(mapScale);

		if(isNumberGisLabelViewScale && isNumberMapScale) {
			double nGisLabelViewScale = Double.parseDouble(gisFeatureViewScale);
			double nMapScale = Double.parseDouble(mapScale);
			if(nGisLabelViewScale > nMapScale) {
				list = geoDataService.selectFcltAngleGeoData(vo);
			}
		}
		return gisUtil.createGeoJson(list, "pointX", "pointY");
	}

	/* 시설물 아이디로 시설물정보 데이터를 가져온다. */
	@RequestMapping(value="/mntr/fcltById.json", method = RequestMethod.POST)
	public @ResponseBody EgovMap getFcltById(@ModelAttribute FcltVO vo) throws Exception {
		EgovMap map = geoDataService.selectFcltById(vo);
		return map;
	}

	/*
	 * 시설물 식별번호로 CCTV를 재생할 수있는 데이터를 가져온다.
	 */
	@RequestMapping(value="/mntr/fcltByUid.json", method = RequestMethod.POST)
	public ModelAndView getFcltByUid(@RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		EgovMap result = geoDataService.selectFcltByUid(params);
		mav.addObject("result", result);
		return mav;
	}

	/* 위경도 좌표로 근처의 CCTV정보 데이터를 하나 가져온다. */
	@RequestMapping(value="/mntr/nearestCctv.json", method = RequestMethod.POST)
	public @ResponseBody EgovMap getNearestCctv(@ModelAttribute FcltVO vo, HttpServletRequest request) throws Exception {

		return geoDataService.selectNearestCctv(vo);
	}

	/* 재난지점 주변 CCTV 목록 결과를 가져온다. */
	@RequestMapping(value="/mntr/nearestCctvList.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getNearestCctvList(@ModelAttribute FcltVO vo, ModelMap model, HttpServletRequest request) throws Exception {
		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
		int totCnt = geoDataService.selectNearestCctvListTotCnt(vo);
		paginationInfo.setTotalRecordCount(totCnt);

		List<EgovMap> list = geoDataService.selectNearestCctvList(vo);

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		resultMap.put("totalPages", paginationInfo.getTotalPageCount());
		resultMap.put("totalRows", totCnt);
		resultMap.put("rows", list);
		resultMap.put("page", vo.getPage());
		return resultMap;
	}

	@RequestMapping(value="/mntr/castNetCctvList.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getCastNetCctvList(@ModelAttribute FcltVO vo, ModelMap model, HttpServletRequest request) throws Exception {
		return geoDataService.selectCastNetCctvList(vo);
	}

	@RequestMapping(value="/mntr/configure/selectLayerMngList.json", method = RequestMethod.POST)
	public ModelAndView selectLayerMngList(@ModelAttribute ConfigureVO configureVO, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<EgovMap> layerMngList = configureService.selectLayerMngList(configureVO);
		List<String> layerGrpList = new ArrayList<String>();
		List<EgovMap> layerGrpMapList = new ArrayList<EgovMap>();

		if(layerMngList != null) {
			int size = layerMngList.size();
			for (int i = 0; i < size; i++) {
				EgovMap layerMngMap = layerMngList.get(i);
				String layerGrpId = EgovStringUtil.nullConvert(layerMngMap.get("layerGrpId"));
				String layerGrpNm = EgovStringUtil.nullConvert(layerMngMap.get("layerGrpNm"));

	            if (!layerGrpList.contains(layerGrpId)) {
	            	layerGrpList.add(layerGrpId);
	            	EgovMap layerGrpMap = new EgovMap();
	            	layerGrpMap.put("layerGrpId", layerGrpId);
	            	layerGrpMap.put("layerGrpNm", layerGrpNm);
	            	layerGrpMapList.add(layerGrpMap);
	            }
	        }
		}

		model.remove("configureVO");
		mav.addObject("layerList", layerMngList);
		mav.addObject("layerGrpList", layerGrpMapList);
		return mav;
	}

	@RequestMapping(value="/mntr/configure/updateLayerMng.json", method = RequestMethod.POST)
	public ModelAndView updateLayerMng(@ModelAttribute ConfigureVO configureVO, ModelMap model) throws Exception {
		model.remove("configureVO");
		ModelAndView mav = new ModelAndView("jsonView");
		configureService.updateLayerMng(configureVO);
		return mav;
	}

	@RequestMapping(value="/mntr/configure/updateAllLayerMng.json", method = RequestMethod.POST)
	public ModelAndView updateAllLayerMng(@ModelAttribute ConfigureVO configureVO, ModelMap model) throws Exception {
		model.remove("configureVO");
		ModelAndView mav = new ModelAndView("jsonView");
		configureService.updateAllLayerMng(configureVO);
		return mav;
	}

	/*
	 * 위치정보 데이터를 가져온다.
	 */
	@RequestMapping(value="/mntr/lcInfoGeoData.json", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getCarLcInfoGeoData(@RequestParam Map<String, String> params) throws Exception {
		List<EgovMap> list = geoDataService.selectLcInfoGeoData(params);
		return gisUtil.createGeoJson(list, "pointX", "pointY");
	}

	@RequestMapping(value="/mntr/mntrMapSearch.do")
	public String mntrMapSearch(ModelMap model) throws Exception {
		return "blank/srch/mntrMapSearch";
	}
}
