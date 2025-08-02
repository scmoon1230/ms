/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : FcltController.java
 * @Description : 시설물 관리 관련 컨트롤러
 * @Version : 1.0
 * Copyright (c) 2018 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2018. 09. 03. SaintJuny 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.fclt.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.CommonVO;
import kr.co.ucp.mntr.cmm.util.CommonUtil;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.mntr.fclt.service.FcltService;
import kr.co.ucp.mntr.fclt.service.FcltSrchVO;
import kr.co.ucp.mntr.fclt.service.FcltVO;
import kr.co.ucp.mntr.fclt.service.UploadVO;
import kr.co.ucp.mntr.gis.service.GeoDataService;
import kr.co.ucp.mntr.gis.util.GisUtil;
import kr.co.ucp.utl.sim.service.EgovFileScrty;

@Controller
public class FcltController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="fcltService")
	private FcltService fcltService;

	@Resource(name="geoDataService")
	private GeoDataService geoDataService;

	@Resource(name="gisUtil")
	private GisUtil gisUtil;

	@Resource(name="commonUtil")
	private CommonUtil commonUtil;

	// 시설물 관리 화면으로 이동한다.
	@RequestMapping(value="/mntr/fclt.do")
	public String viewList(@ModelAttribute FcltSrchVO vo, ModelMap model) throws Exception {
		String[] commonData = { "시설물", "fclt", "fclt" };
		commonUtil.setCommonVOData(vo, 1, commonData);
		model.addAttribute("common", vo);
		
		return "mntr/fclt/fclt";
	}

	// 시설물 목록 그리드 데이터를 가져온다.
	@RequestMapping(value="/mntr/fcltList.json")
	public @ResponseBody Map<String, Object> getListData(@ModelAttribute FcltSrchVO vo, ModelMap model) throws Exception {
		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);

		List<EgovMap> list = fcltService.selectFcltList(vo);
		int totCnt = fcltService.selectFcltListTotCnt(vo);
		paginationInfo.setTotalRecordCount(totCnt);

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		resultMap.put("totalPages", paginationInfo.getTotalPageCount());
		resultMap.put("totalRows", totCnt);
		resultMap.put("rows", list);
		resultMap.put("page", vo.getPage());
		return resultMap;
	}

	/*
	 * 시설물 상세화면
	 */
	@RequestMapping(value="/mntr/fcltDetail.do")
	public String viewDetail(@ModelAttribute FcltSrchVO vo, ModelMap model) throws Exception {
		String[] commonData = { "시설물", "fclt", "fcltDetail" };
		commonUtil.setCommonVOData(vo, 2, commonData);
		model.addAttribute("common", vo);
		model.addAttribute("result", fcltService.selectFcltDetail(vo));
		return "mntr/fclt/fcltDetail";
	}

	/*
	 * 시설물 등록화면
	 */
	@RequestMapping(value="/mntr/fcltReg.do")
	public String viewReg(@ModelAttribute CommonVO vo, ModelMap model) throws Exception {
		String[] commonData = { "시설물", "fclt", "fcltReg" };
		commonUtil.setCommonVOData(vo, 3, commonData);
		model.addAttribute("common", vo);
		model.addAttribute("fcltVO", new FcltVO());
		return "mntr/fclt/fcltReg";
	}

	/*
	 * 시설물 등록 처리
	 */
	@RequestMapping(value="/mntr/fcltRegProc.json")
	public ModelAndView fcltRegProc(@ModelAttribute FcltVO vo, ModelAndView model, SessionStatus status) throws Exception {
		String fcltId = fcltService.insertFclt(vo);

		model.addObject("status", 1);
		model.addObject("fcltId", fcltId);
		model.setViewName("jsonView");
		return model;
	}

	/*
	 * 시설물 관리 수정화면으로 이동한다.
	 */
	@RequestMapping(value="/mntr/fcltUpd.do")
	public String viewUpdate(@ModelAttribute FcltSrchVO vo, ModelMap model) throws Exception {
		String[] commonData = { "시설물", "fclt", "fcltUpd" };
		commonUtil.setCommonVOData(vo, 4, commonData);
		model.addAttribute("common", vo);
		model.addAttribute("fcltVO", fcltService.selectFcltDetail(vo));
		return "mntr/fclt/fcltReg";
	}

	/*
	 * 시설물 관리 수정을 처리한다.
	 */
	@RequestMapping(value="/mntr/fcltUpdProc.json")
	public @ResponseBody ModelAndView fcltUpdProc(@ModelAttribute FcltVO vo, ModelAndView model, SessionStatus status) throws Exception {
		fcltService.updateFclt(vo);

		model.addObject("status", 1);
		model.setViewName("jsonView");
		return model;
	}

	/* 시설물 관리 삭제를 처리한다. */
	@RequestMapping(value="/mntr/fcltDelProc.json")
	public @ResponseBody ModelAndView fcltDelProc(@ModelAttribute FcltVO vo, ModelAndView model, SessionStatus status) throws Exception {
		vo.setUseTyCd("D");
		fcltService.updateFclt(vo);

		model.addObject("status", 1);
		model.setViewName("jsonView");
		return model;
	}

	/*
	 * 시설물 현황
	 */
	@RequestMapping(value="/mntr/fcltStatus.do")
	public String fcltStatusList(@ModelAttribute CommonVO vo, ModelMap model) throws Exception {
		//String[] commonData = { "시설물현황", "fclt", "fcltStatus" };
		//commonUtil.setCommonVOData(vo, 0, commonData);
		//model.addAttribute("common", vo);
		model.addAttribute("totCnt", fcltService.selectFcltStatusList());
		return "nomap/fclt/fcltStatus";
	}

	@RequestMapping(value="/mntr/fcltStatusPopup.do", method = RequestMethod.POST)
	public String viewPopupFcltStatus(@ModelAttribute FcltSrchVO vo, ModelMap model) {
		model.addAttribute("params", vo);
		return "blank/fclt/fcltStatusPopup";
	}

	/*
	 * 시설물 현황 엑셀 데이터를 가져온다.
	 */
	@RequestMapping(value="/mntr/fcltStatusExcel.do")
	public String downloadFcltStatusList(@ModelAttribute FcltSrchVO vo, HttpServletResponse response, ModelMap model) throws Exception {
		response = commonUtil.setExcelDownladHeader(response, ("시설물현황_" + vo.getSearchFcltKndCd() + "(" + vo.getSearchFcltSttus() + ")_" + new Date().getTime()).trim());
		List<EgovMap> list = fcltService.selectFcltStatus(vo);

		model.addAttribute("list", list);
		model.addAttribute("vo", vo);
		return "excel/fclt/fcltStatusExcel";
	}

	/*
	 * 시설물 현황 엑셀 데이터를 가져온다.
	 */
	@RequestMapping(value="/mntr/fcltExcelDownload.do")
	public String fcltExcelDownload(@ModelAttribute FcltSrchVO vo, HttpServletResponse response, ModelMap model) throws Exception {
		response = commonUtil.setExcelDownladHeader(response, ("시설물현황_" + vo.getSearchFcltKndCd() + "(" + vo.getSearchFcltSttus() + ")_" + new Date().getTime()).trim());
		List<EgovMap> list = fcltService.selectFcltStatus(vo);

		model.addAttribute("list", list);
		model.addAttribute("vo", vo);
		return "excel/fclt/fcltExcelDownload";
	}

	@RequestMapping(value="/mntr/fcltExcelPopup.do")
	public String fcltExcelPopup(@ModelAttribute FcltSrchVO vo, ModelMap model) {
		model.addAttribute("params", vo);
		return "blank/fclt/fcltExcelPopup";
	}

	@RequestMapping(value="/mntr/fcltExcelPopupUpload.do")
	public String fcltExcelPopupUpload() {
		return "blank/fclt/fcltExcelPopupUpload";
	}

	/* 2017 시설물 엑셀다운로드 : 필수로 작성할 시설물 기본항목 조회 */
	@RequestMapping(value="/mntr/fcltDefaultColumns.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getFcltDefaultColumns(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String columns = fcltService.selectFcltDefaultColumns();
		String[] columnList = columns.split(",");
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

		resultMap.put("columnList", columnList);

		return resultMap;
	}

	@RequestMapping(value="/mntr/fcltExcelColumnList.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getFcltColumnList(@ModelAttribute FcltSrchVO vo, ModelMap model, HttpServletRequest request) throws Exception {
		List<EgovMap> allList = fcltService.selectFcltColumnList(vo);

		String defaultColumns = fcltService.selectFcltDefaultColumns();
		String[] defaultList = defaultColumns.split(",");

		List<String> stringList = new ArrayList<String>();

		List<EgovMap> egovMapList = new ArrayList<EgovMap>();
		// JSONArray egovMapList = new JSONArray();

		// defaultList 배열에 담긴 데이터를 stringList로 add(기본 필수항목)
		for (int i = 0; i < defaultList.length; i++) {
			stringList.add(defaultList[i]);
		}

		String result = "";
		int eql = 0;

		// defaultList에 담긴 값을 제외하고 나머지 값을 stringList로 add(나머지 선택항목)
		for (int j = 0; j < allList.size(); j++) {
			result = (String) allList.get(j).getValue(1);

			for (int k = 0; k < stringList.size(); k++) {
				if (stringList.get(k).equals(result)) {
					eql = 1;
				}
			}

			if (eql != 1) {
				stringList.add(result);
			}
			eql = 0;
		}

		String result2 = "";
		// 새로운 egovMapList에 정렬한 순서대로 항목을 담음
		for (int a = 0; a < allList.size(); a++) {
			result2 = stringList.get(a);

			for (int b = 0; b < stringList.size(); b++) {
				if (allList.get(b).getValue(1).equals(result2)) {
					egovMapList.add(allList.get(b));
				}
			}
		}

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		resultMap.put("rows", egovMapList);
		return resultMap;
	}

	@RequestMapping(value="/mntr/fcltExcelPopupDownload.do")
	public String fcltExcelPopupDownload(FcltSrchVO searchVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		String userId = SessionUtil.getUserId();
		String excelDownTy = EgovStringUtil.nullConvert(request.getParameter("excelDownTy"));
		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
		//response = commonUtil.setExcelDownladHeader(response, ("(" + siteName + ")시설물마스터_다운로드_" + userId + "_" + formatter.format(today)));
		response = commonUtil.setExcelDownladHeader(response, ("시설물마스터_다운로드_" + userId + "_" + formatter.format(today)));

		String columnName = EgovStringUtil.nullConvert(searchVO.getColumnName());
		logger.info("columnName => {}", columnName);

		if (excelDownTy.equals("N")) {
			List<?> nullList = fcltService.excelDownNullList(searchVO); // 미입력항목 리스트
			model.addAttribute("list", nullList);
			model.addAttribute("searchVO", searchVO);
		} else {
			List<?> fcltList = fcltService.excelDownFcltList(searchVO); // 시설물정보 리스트
			model.addAttribute("list", fcltList);
			model.addAttribute("searchVO", searchVO);
		}
		return "blank/fclt/fcltExcelPopupDownload";
	}

	/*
	 * Excel Upload View Page
	 */
	@RequestMapping(value="/mntr/fcltExcelUpload.do")
	public String fcltExcelUpload(@ModelAttribute FcltSrchVO common, ModelMap model) throws Exception {
		String[] commonData =
		{ "시설물일괄등록", "fclt", "fcltExcelUpload" };
		commonUtil.setCommonVOData(common, 0, commonData);
		model.addAttribute("common", common);
		return "nomap/fclt/fcltExcelUpload";
	}

	/*
	 * CCTV 관리 일괄업로드 항목 적합성을 체크한다.
	 */
	@RequestMapping(value="/mntr/fcltExcelUploadValidation.json")
	public ModelAndView upload(MultipartHttpServletRequest req, HttpServletResponse res, ModelAndView model) throws Exception {
		Iterator<String> itr = req.getFileNames();
		MultipartFile mpf = req.getFile(itr.next());

		UploadVO vo = new UploadVO();
		vo.setUpdUserId(SessionUtil.getUserId());

		String result = fcltService.fcltExcelUploadValidation(vo, mpf);

		model.addObject("result", result);
		model.addObject("vo", vo);
		model.setViewName("jsonView");
		return model;
	}

	/* 시설물임시테이블 엑셀다운로드 */
	@RequestMapping(value="/mntr/fcltExcelUploadChk.do", method = RequestMethod.POST)
	public String fcltExcelUploadChk(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		FcltSrchVO searchVO = new FcltSrchVO();
		searchVO.setUserId(SessionUtil.getUserId());
		searchVO.setTitleNm(request.getParameter("titleNm"));
		searchVO.setTitleNmChk(request.getParameter("titleNmChk"));
		searchVO.setColumnName(request.getParameter("tabIds"));
		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
		response = commonUtil.setExcelDownladHeader(response, ("시설물임시_" + formatter.format(today)));

		List<EgovMap> fcltList = fcltService.selectFacilityUpchkList(searchVO); // 시설물임시 리스트
		model.addAttribute("list", fcltList);
		model.addAttribute("searchVO", searchVO);

		return "excel/fclt/fcltExcelUploadChk";
	}

	/* 2017 시설물 엑셀다운로드 : 사용자 비밀번호 체크 */
	@RequestMapping(value="/mntr/fcltExcelUserValidation.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> fcltExcelUserValidation(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String reVal = "";
		String userId = SessionUtil.getUserId();
		String pwd = EgovStringUtil.nullConvert(request.getParameter("pwd"));

		Map<String, Object> args = new HashMap<String, Object>();
		String dbEncryptTag = prprtsService.getString("DB_ENCRYPT", "UCP");
		String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");

		if ("UCP".equals(dbEncryptTag)) {
			pwd = EgovFileScrty.encryptPassword(pwd, saltText);
		}
		args.put("userId", userId);
		//args.put("siteName", siteName);
		args.put("dbEncryptTag", dbEncryptTag);
		args.put("password", pwd);
		reVal = fcltService.selectUserValidation(args);

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		resultMap.put("reVal", reVal); // (DB 결과값은 'O' 이거나 'X')

		return resultMap;
	}

	@RequestMapping(value="/mntr/fcltExcelUploadProc.json")
	public ModelAndView uploadProc(@ModelAttribute UploadVO vo) throws Exception {
		vo.setUpdUserId(SessionUtil.getUserId());

		String result = "";
		try {
			result = fcltService.fcltExcelUploadProc(vo);
		} catch (Exception e) {
			result = e.getCause().getMessage();
		}

		ModelAndView model = new ModelAndView();
		model.addObject("result", result);
		model.addObject("vo", vo);
		model.setViewName("jsonView");
		return model;
	}

	@RequestMapping(value="/mntr/downloadExcelUploadCode.do", method = RequestMethod.POST)
	public String downloadExcelUploadCode(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
		response = commonUtil.setExcelDownladHeader(response, ("시설물관련코드_" + formatter.format(today)));

		model.addAttribute("excelDownCodeList", fcltService.selectExcelUploadCode());
		return "excel/fclt/fcltExcelUploadCode";
	}

	@RequestMapping(value="/mntr/downloadExcelUploadForm.do")
	public void downloadExcelUploadForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		fcltService.downloadExcelUploadForm(request, response);
	}

	/*
	 * CCTV 관리 일괄업로드 오류목록을 다운로드한다.
	 */
	@RequestMapping(value="/mntr/downloadExcelErrorList.do")
	public String downloadExcelErrorList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		String getXlsFileName = EgovStringUtil.nullConvert(request.getParameter("xlsfilename"));
		String getXlsTitle = EgovStringUtil.nullConvert(request.getParameter("xlstitle"));
		String getXlsHeader = EgovStringUtil.nullConvert(request.getParameter("xlsheader"));
		String getXlsData = EgovStringUtil.nullConvert(request.getParameter("xlsdata"));

		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
		response = commonUtil.setExcelDownladHeader(response, (getXlsFileName + formatter.format(today)));

		model.addAttribute("xlsfilename", getXlsFileName);
		model.addAttribute("xlstitle", getXlsTitle);
		model.addAttribute("xlsheader", getXlsHeader);
		model.addAttribute("xlsdata", getXlsData);
		return "excel/fclt/fcltExcelErrorList";
	}

	/*
	 * 시설물등록이력 탭(FCLT 관리 화면)으로 이동한다.
	 */
	@RequestMapping(value="/mntr/fcltRegHis.do")
	public String fcltRegHis(@ModelAttribute FcltSrchVO vo, ModelMap model) throws Exception {
		
//		String[] commonData = { "시설물일괄등록이력", "fclt", "fcltRegHis" };
//		commonUtil.setCommonVOData(vo, 0, commonData);
//		model.addAttribute("common", vo);
		
		return "nomap/fclt/fcltRegHis";
	}

	/*
	 * 시설물등록이력 데이터를 가져온다.
	 */
	@RequestMapping(value="/mntr/fcltRegHisList.json")
	public @ResponseBody Map<String, Object> fcltRegHisData(@ModelAttribute FcltSrchVO vo, ModelMap model) throws Exception {

		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);

		List<?> list = fcltService.selectFcltRegHisList(vo);
		int totCnt = fcltService.selectFcltRegHisListTotCnt(vo);
		paginationInfo.setTotalRecordCount(totCnt);

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

		resultMap.put("totalPages", paginationInfo.getTotalPageCount());
		resultMap.put("totalRows", totCnt);
		resultMap.put("rows", list);
		resultMap.put("page", vo.getPage());
		return resultMap;
	}

	/*
	 * 시설물등록이력 탭(FCLT 관리 화면)으로 이동한다.
	 */
	@RequestMapping(value="/mntr/fcltRegExcel.do")
	public String fcltRegExcel(@ModelAttribute FcltSrchVO vo, ModelMap model) throws Exception {
		String[] commonData = { "시설물일괄등록", "fclt", "fcltRegExcel" };
		commonUtil.setCommonVOData(vo, 0, commonData);
		model.addAttribute("common", vo);
		return "nomap/fclt/fcltRegExcel";
	}

	/**
	 * 시설물(그룹) 좌표 수정
	 *
	 * @param Map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mntr/updateFcltPointProc.json", method = RequestMethod.POST)
	public ModelAndView updateFcltPointProc(@RequestParam Map<String, String> map) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		EgovMap result = fcltService.updateFcltPoint(map);
		mav.addObject("result", result);
		return mav;
	}

	/**
	 * 시설물 프리셋대역 수정
	 *
	 * @param Map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mntr/updatePresetBdwStartNumProc.json", method = RequestMethod.POST)
	public ModelAndView updatePresetBdwStartNumProc(@RequestParam Map<String, String> map) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		EgovMap result = fcltService.updatePresetBdwStartNum(map);
		mav.addObject("result", result);
		return mav;
	}

	
	

	/*
	 * 시설물용도별분포도 화면으로 이동한다.
	 */
	@RequestMapping(value="/mntr/fcltUsedDistributionMap.do")
	public String fcltUsedDistributionMapView(@ModelAttribute FcltSrchVO vo, ModelMap model) throws Exception {
		String[] commonData = { "시설물용도별분포도", "fclt", "fcltUsedDistributionMap" };
		commonUtil.setCommonVOData(vo, 0, commonData);
		model.addAttribute("common", vo);
		return "mntr/fclt/fcltUsedDistributionMap";
	}

	/*
	 * 시설물용도별분포 데이터를 가져온다.
	 */
	@RequestMapping(value="/mntr/selectFcltUsedDistributionFcltUsedTyList.json", method = RequestMethod.POST)
	public ModelAndView getFcltUsedDistributionFcltUsedTyList(@RequestParam Map<String, Object> params, ModelMap model) throws Exception {
		model.remove("common");
		ModelAndView mav = new ModelAndView("jsonView");
		List<EgovMap> layerList = fcltService.selectFcltUsedDistributionFcltUsedTyList(params);

		mav.addObject("layerList", layerList);
		return mav;
	}

	/*
	 * 시설물 위치정보 데이터를 가져온다.
	 */
	@RequestMapping(value="/mntr/fcltUsedDistributionGeoData.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getFcltUsedDistributionGeoData(@RequestParam Map<String, Object> map) throws Exception {
		List<EgovMap> list = fcltService.selectFcltUsedDistributionGeoData(map);
		return gisUtil.createGeoJson(list, "pointX", "pointY");
	}

	/*
	 * 시군구읍면동 범위 데이터를 가져온다.
	 */
	@RequestMapping(value="/mntr/sggEmdBoundList.json", method = RequestMethod.POST)
	public ModelAndView getSggEmdBoundList(@RequestParam Map<String, Object> map, ModelMap model) throws Exception {
		model.remove("common");
		ModelAndView mav = new ModelAndView("jsonView");
		List<EgovMap> list = fcltService.selectSggEmdBoundList(map);
		mav.addObject("list", list);
		return mav;
	}
}