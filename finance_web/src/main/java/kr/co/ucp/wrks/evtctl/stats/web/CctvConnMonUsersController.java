/**
 *---------------------------------------------------------------------------------- 
 * @File Name    : CctvConnMonUserController.java
 * @Description  : 긴급영상제공통계 CCTV접속현황(월/사용자)
 * @author       : seungJun
 * @since        : 2017. 2. 22.
 * @version      : 1.0
 *----------------------------------------------------------------------------------
 * @Copyright (c)2017 UCP, WideCUBE, All Rights Reserved.
 *----------------------------------------------------------------------------------
 * Data           Author      Description
 *----------------------------------------------------------------------------------
 * 2017. 2. 22.    seungJun    최초작성
 *---------------------------------------------------------------------------------- 
 */
package kr.co.ucp.wrks.evtctl.stats.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.utl.fcc.service.EgovDateUtil;
import kr.co.ucp.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.wrks.evtctl.stats.service.CctvConnCrtStateService;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class CctvConnMonUsersController
{

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	@Resource(name="cctvConnCrtStateService")
	private CctvConnCrtStateService cctvConnCrtStateService;

	// 재난구분별 CCTV 접속현황
	@RequestMapping(value="/wrks/evtctl/stats/cctvMonUsers.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		
		Map<String, Object> args = new HashMap<String, Object>();
		
	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */
		// 날자조회
		String toDate = EgovDateUtil.getToday();
		request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
		// 조회년도 조회
		request.setAttribute("yearList", cctvConnCrtStateService.getYearList()); 
		// 이벤트 목록 조회 통합관제 : 전체, 119, 112 ... : 해당 서비스만
		args.clear();
		String sysCd = "";
		String appType = EgovStringUtil.isNullToString(request.getParameter("sys"));
		if ( "wrks1".equals(appType) ) {
			sysCd = "";
		} else {
			sysCd = lgnVO.getSysCd();
		}
		args.put("sysCd", sysCd);
		request.setAttribute("eventList", cctvConnCrtStateService.getEventList(args));
		return "/wrks/evtctl/stats/cctvMonUsers";
	}

	@RequestMapping(value="/wrks/evtctl/stats/cctvMonUsers/list.json") 
	@ResponseBody
	public Map<String, Object> getList(ModelMap model ,HttpServletRequest request ,HttpServletResponse response ) throws Exception
	{
		Map<String, String> args = new HashMap<String, String>();
		// 페이징 조건
		args.put("pageNo"     , request.getParameter("page"));
		args.put("rowsPerPage", request.getParameter("rows"));
		args.put("sidx"       , request.getParameter("sidx"));
		args.put("sord"       , request.getParameter("sord"));
		args.put("searchYear", request.getParameter("searchYear"));
		args.put("searchSysCd", request.getParameter("searchSysCd"));
		List<Map<String, String>> resultRows = cctvConnCrtStateService.getMonthUserList(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", resultRows);
		map.put("rCnt", resultRows.size());
		return map;
	}

	@RequestMapping(value="/wrks/evtctl/stats/cctvMonUsers/list.excel") 
	public String getExcelList(ModelMap model ,HttpServletRequest request ,HttpServletResponse response ) throws Exception
	{
		String searchYear = request.getParameter("searchYear");
		String searchEvtNm = request.getParameter("searchEvtNm");

		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , request.getParameter("searchCurrPage"));
		args.put("rowsPerPage", request.getParameter("searchPageList"));
		args.put("searchYear", searchYear);
		args.put("searchSysCd", request.getParameter("searchSysCd"));
		List<Map<String, String>> resultRows = cctvConnCrtStateService.getMonthTypeExcel(args);

		String titleKey = "EVT_OCR_ITEM_DTL|M_01|M_02|M_03|M_04|M_05|M_06|M_07|M_08|M_09|M_10|M_11|M_12";
		String titleHeader = "유형구분|1월|2월|3월|4월|5월|6월|7월|8월|9월|10월|11월|12월";
		// 제목 및 검색조건 추가
		model.put("title", "CCTV접속현황(월/유형)");
		model.put("search", searchEvtNm + " 서비스 (조회기준일 : " + searchYear + ")");
		model.put("fileName", searchYear + "_" + searchEvtNm + "_CCTV접속현황(월/유형)" + "(" + EgovDateUtil.getToday());
		model.put("titleKey", titleKey); 
		model.put("titleHeader", titleHeader);
		model.put("dataList", resultRows);

		return "excelView";
	}
}
