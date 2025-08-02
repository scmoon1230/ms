/**
* --------------------------------------------------------------------------------------------------------------
* @Class Name : CalamityCctvController.java
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
package kr.co.ucp.wrks.info.state.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.EgovUserDetailsHelper;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.utl.fcc.service.EgovDateUtil;
import kr.co.ucp.wrks.info.state.service.CalamityCctvService;
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
public class CalamityCctvController 
{
	static Logger logger = LoggerFactory.getLogger(CalamityCctvController.class);

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="calamityCctvService")
	CalamityCctvService calamityCctvService;
	
	@Resource(name="codeCmcdService")
	CodeCmcdService codeCmcdService;
	
	// 재난번호별 CCTV목록 조회화면
	@RequestMapping(value="/wrks/info/state/calamityCctvList.do")
	public String cctvPtzHisList(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{
		Map<String, Object> args = new HashMap<String, Object>();
		
	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		// 날자조회 한달의 첫날 부터 현재 날자를 조회해 온다.
//		request.setAttribute("firstDay", EgovDateUtil.formatDate(EgovDateUtil.getFirstDate(), "-"));    // 그 달의 첫째날자 구하기
		// 2016.05.10 	EgovDateUtil 최신후 	getFirstDate 없어 아래와 같이 수정(윤법상)
		
		String toDate = EgovDateUtil.getToday();
		String startDay = EgovDateUtil.formatDate(EgovDateUtil.addYearMonthDay(toDate, 0, 0, -7), "-");
		request.setAttribute("firstDay", startDay);
						
		request.setAttribute("currentDay", EgovDateUtil.formatDate(EgovDateUtil.getToday(), "-"));      // 그 달의 오늘날자 구하기

		return "wrks/info/state/calamityCctvList";
	}
	
	// 재난번호별 CCTV목록 조회데이터
	@RequestMapping(value="/wrks/info/state/calamityCctvListData.json")
	@ResponseBody
	public Map<String, Object> calamityCctvListData(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{
		
		String pageNo = request.getParameter("page");
		String rowsPerPage	= request.getParameter("rows");
		String sidx = request.getParameter("sidx");
		String sord	= request.getParameter("sord");

		// 페이징 조건
		String clmtNo = request.getParameter("clmtNo");
		String cctvNm = request.getParameter("cctvNm");
		String strDateStart = request.getParameter("strDateStart");
		String strDateEnd = request.getParameter("strDateEnd");

		// 검색조건
		Map<String, String> args = new HashMap<String, String>();
		args.put("clmtNo", clmtNo);
		args.put("cctvNm", cctvNm);
		args.put("strDateStart", strDateStart.replace("-", ""));
		args.put("strDateEnd", strDateEnd.replace("-", ""));
		
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		
     	args.put("sysId", 	lgnVO.getSysId());

		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		List<Map<String, String>> list = calamityCctvService.calamityCctvListData(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", pageNo);

		return map;
	}

}
