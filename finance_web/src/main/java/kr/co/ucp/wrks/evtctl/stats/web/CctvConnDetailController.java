/**
 *---------------------------------------------------------------------------------- 
 * @File Name    : CctvConnDetailController.java
 * @Description  : 긴급영상제공통계 CCTV접속상세
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
public class CctvConnDetailController
{

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	@Resource(name="cctvConnCrtStateService")
	private CctvConnCrtStateService cctvConnCrtStateService;

	// 재난구분별 CCTV 접속현황
	@RequestMapping(value="/wrks/evtctl/stats/cctvDetail.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		
		Map<String, Object> args = new HashMap<String, Object>();
		
	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		// 날자조회
		String toDate = EgovDateUtil.getToday();
		request.setAttribute("startDate", EgovDateUtil.formatDate(EgovDateUtil.addYearMonthDay(toDate, 0, 0, -7), "-"));
		request.setAttribute("endDate", EgovDateUtil.formatDate(EgovDateUtil.addYearMonthDay(toDate, 0, 0, -1), "-"));
		request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
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
		return "/wrks/evtctl/stats/cctvDetail";
	}

	@RequestMapping(value="/wrks/evtctl/stats/cctvDetail/list.json") 
	@ResponseBody
	public Map<String, Object> getList(ModelMap model ,HttpServletRequest request ,HttpServletResponse response ) throws Exception
	{
		Map<String, String> args = new HashMap<String, String>();
		// 페이징 조건
		args.put("pageNo"     , request.getParameter("page"));
		args.put("rowsPerPage", request.getParameter("rows"));
		args.put("sidx"       , request.getParameter("sidx"));
		args.put("sord"       , request.getParameter("sord"));
		args.put("searchStartDate", request.getParameter("searchStartDate").replaceAll("-", ""));
		args.put("searchEndDate", request.getParameter("searchEndDate").replaceAll("-", ""));
		args.put("searchSysCd", request.getParameter("searchSysCd"));
		args.put("searchOcrNo", EgovStringUtil.isNullToString(request.getParameter("searchOcrNo")));
		args.put("searchUserId", EgovStringUtil.isNullToString(request.getParameter("searchUserId")));
		List<Map<String, String>> resultRows = cctvConnCrtStateService.getConnDetailList(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", resultRows);
		map.put("rCnt", resultRows.size());
		return map;
	}

	@RequestMapping(value="/wrks/evtctl/stats/cctvDetail/list.excel") 
	public String getExcelList(ModelMap model ,HttpServletRequest request ,HttpServletResponse response ) throws Exception
	{
		String searchStartDate = request.getParameter("searchStartDate").replaceAll("-", "");
		String searchEndDate = request.getParameter("searchEndDate").replaceAll("-", "");
		String searchEvtNm = request.getParameter("searchEvtNm");

		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , request.getParameter("searchCurrPage"));
		args.put("rowsPerPage", request.getParameter("searchPageList"));

		args.put("searchStartDate", searchStartDate);
		args.put("searchEndDate", searchEndDate);
		args.put("searchSysCd", request.getParameter("searchSysCd"));
		args.put("searchOcrNo", EgovStringUtil.isNullToString(request.getParameter("searchOcrNo")));
		args.put("searchUserId", EgovStringUtil.isNullToString(request.getParameter("searchUserId")));
		List<Map<String, String>> resultRows = cctvConnCrtStateService.getConnDetailExcel(args);

		String titleKey = "VIEW_DATE|USER_NM|EVT_OCR_NO|EVT_OCR_ITEM_DTL|FCLT_LBL_NM|LOTNO_ADRES_NM|VIEW_CNT|PTZ_CNT";
		String titleHeader = "일시|사용자명|근거번호|유형|CCTV명|CCTV주소|접속수|제어수";
		// 제목 및 검색조건 추가
		model.put("title", "CCTV접속상세이력");
		model.put("search", searchEvtNm + " 서비스 (조회기간 : " + searchStartDate + "_" + searchEndDate + ")");
		model.put("fileName", searchStartDate + "_" + searchEndDate + "_" + searchEvtNm + "_CCTV접속상세이력" + "(" + EgovDateUtil.getToday());
		model.put("titleKey", titleKey); 
		model.put("titleHeader", titleHeader);
		model.put("dataList", resultRows);

		return "excelView";
	}
}
