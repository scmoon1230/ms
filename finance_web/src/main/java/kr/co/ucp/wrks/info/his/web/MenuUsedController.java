/**
* --------------------------------------------------------------------------------------------------------------
* @Class Name : ConnHisController.java
* @Description :
* @Version : 1.0
* Copyright (c) 2015 by KR.CO.UCP.CNU All rights reserved.
* @Modification Information
* --------------------------------------------------------------------------------------------------------------
* DATE           AUTHOR     DESCRIPTION
* --------------------------------------------------------------------------------------------------------------
* 2015. 10. 1.   inhan29    최초작성
*
* --------------------------------------------------------------------------------------------------------------
*/
package kr.co.ucp.wrks.info.his.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.egov.com.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.egov.com.utl.fcc.service.EgovDateUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.wrks.info.his.service.ConnHisService;
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
public class MenuUsedController
{

	static Logger logger = LoggerFactory.getLogger(MenuUsedController.class);

	@Resource(name="connHisService")
	ConnHisService connHisService;

	@Resource(name="codeCmcdService")
	CodeCmcdService codeCmcdService;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	// 사용자별 메뉴 접속 현황
	@RequestMapping(value="/wrks/info/his/menuUsedList.do")
	public String menuUsedList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{
		String toDate = EgovDateUtil.getToday();
		String startDay = EgovDateUtil.formatDate(EgovDateUtil.addYearMonthDay(toDate, 0, 0, -7), "-");
		request.setAttribute("firstDay", startDay);
		request.setAttribute("currentDay", EgovDateUtil.formatDate(EgovDateUtil.getToday(), "-"));

		request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */
  		
  		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
  		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
  		/* row per page 설정 끝 */
		return "wrks/info/his/menuUsedList";
	}

	//사용자별 메뉴 접속 현황
	@RequestMapping(value="/wrks/info/his/menuUsedListData.json")
	@ResponseBody
	public Map<String, Object> menuUsedListData(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{

		String pageNo = EgovStringUtil.nullConvert(request.getParameter("page"));
		String rowsPerPage	= EgovStringUtil.nullConvert(request.getParameter("rows"));
		String sidx = EgovStringUtil.nullConvert(request.getParameter("sidx"));
		String sord	= EgovStringUtil.nullConvert(request.getParameter("sord"));

		// 페이징 조건
		String seachTy = EgovStringUtil.nullConvert(request.getParameter("seachTy"));
		String seachNm = EgovStringUtil.nullConvert(request.getParameter("seachNm"));
		String strDateStart = EgovStringUtil.nullConvert(request.getParameter("strDateStart"));
		String strDateEnd = EgovStringUtil.nullConvert(request.getParameter("strDateEnd"));

		// 검색조건
		Map<String, String> args = new HashMap<String, String>();
		if ("ID".equals(seachTy)) {
			args.put("searchUserId", seachNm);
		} else {
			args.put("searchUserNm", seachNm);
		}
		args.put("searchStartDate", strDateStart.replace("-", ""));
		args.put("searchEndDate", strDateEnd.replace("-", ""));

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

     	args.put("sysId", 	lgnVO.getSysId());

		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		List<Map<String, String>> list = connHisService.menuUsedListData(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", pageNo);

		return map;
	}
	@RequestMapping(value="/wrks/info/his/menuUsedListExcel.excel")
	public String menuUsedListExcel(ModelMap model ,HttpServletRequest request ,HttpServletResponse response ) throws Exception
	{
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String searchStartDate = EgovStringUtil.nullConvert(request.getParameter("searchStartDate").replaceAll("-", ""));
		String searchEndDate = EgovStringUtil.nullConvert(request.getParameter("searchEndDate").replaceAll("-", ""));
		String searchUserId = EgovStringUtil.nullConvert(request.getParameter("searchUserId"));
		String searchUserNm = EgovStringUtil.nullConvert(request.getParameter("searchUserNm"));

		Map<String, String> args = new HashMap<String, String>();

     	args.put("sysId", 	lgnVO.getSysId());
		args.put("searchStartDate", searchStartDate);
		args.put("searchEndDate", searchEndDate);
		args.put("searchUserId", searchUserId);
		args.put("searchUserNm", searchUserNm);
		List<Map<String, String>> resultRows = connHisService.menuUsedListExcel(args);

		String titleKey = "connectYmd|lastTime|menuNm|userNmKo|userId|grpNm|authLvlNm|insttNm|deptNm";
		String titleHeader = "일자|접속시간|메뉴명|이름|아이디|그룹명|그룹레벨명|기관|부서";
		// 제목 및 검색조건 추가
		model.put("title", "메뉴사용이력");
		model.put("search",  "조회기간 : " + searchStartDate + " ~ " + searchEndDate);
		model.put("fileName", searchStartDate + "_" + searchEndDate + "_메뉴사용이력" + "(" + EgovDateUtil.getToday());
		model.put("titleKey", titleKey);
		model.put("titleHeader", titleHeader);
		model.put("dataList", resultRows);

		return "excelView";
	}
}
