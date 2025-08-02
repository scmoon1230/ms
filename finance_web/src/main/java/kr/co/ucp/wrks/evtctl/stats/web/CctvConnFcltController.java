/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : CctvConnFcltController.JAVA
 * @Description : 일별월별영상접속현황
 * @Version : 1.0
 * Copyright (c) 2020 by KR.CO.WIDECUBE All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE           AUTHOR      DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2020-11-05   SaintJuny       최초작성
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.wrks.evtctl.stats.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.service.PrprtsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.property.EgovPropertyService;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.egov.com.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.utl.fcc.service.EgovDateUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.wrks.evtctl.stats.service.CctvConnFcltService;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;

@Controller
public class CctvConnFcltController {

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name = "codeCmcdService")
	private CodeCmcdService codeCmcdService;

	@Resource(name = "cctvConnFcltService")
	private CctvConnFcltService cctvConnFcltService;

	// CCTV접속목록
	@RequestMapping(value = "/wrks/evtctl/stats/cctvConnFclt.do")
	public String view(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		//LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        LoginVO lgnVO = SessionUtil.getUserInfo();
		
  		request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
		/* row per page 설정 끝 */
		return "/wrks/evtctl/stats/cctvConnFclt";
	}

	@RequestMapping(value = "/wrks/evtctl/stats/cctvConnFclt/list.json")
	@ResponseBody
	public Map<String, Object> getList(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();

		String searchYear = EgovStringUtil.nullConvert(request.getParameter("searchYear"));
		String searchMonth = EgovStringUtil.nullConvert(request.getParameter("searchMonth"));
		String searchCctvNm = EgovStringUtil.nullConvert(request.getParameter("searchCctvNm"));
		args.put("searchYear", searchYear);
		args.put("searchMonth", searchMonth);
		args.put("searchCctvNm", searchCctvNm);

		// 페이징 조건
		String pageNo = EgovStringUtil.nullConvert(request.getParameter("page"));
		String rowsPerPage = EgovStringUtil.nullConvert(request.getParameter("rows"));
		String sidx = EgovStringUtil.nullConvert(request.getParameter("sidx"));
		String sord = EgovStringUtil.nullConvert(request.getParameter("sord"));
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		List<Map<String, String>> list = cctvConnFcltService.getList(args);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", pageNo);
		return map;
	}

	@RequestMapping(value = "/wrks/evtctl/stats/cctvConnFclt/list.excel")
	public String getExcel(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String searchYear = EgovStringUtil.nullConvert(request.getParameter("searchYear"));
		String searchMonth = EgovStringUtil.nullConvert(request.getParameter("searchMonth"));
		String searchCctvNm = EgovStringUtil.nullConvert(request.getParameter("searchCctvNm"));

		Map<String, String> args = new HashMap<String, String>();
		args.put("searchYear", searchYear);
		args.put("searchMonth", searchMonth);
		args.put("searchCctvNm", searchCctvNm);

		List<Map<String, String>> resultRows = cctvConnFcltService.getExcel(args);

		//제목 및 검색조건 추가
		String titleKey = EgovStringUtil.nullConvert(request.getParameter("titleKey"));
		String titleHeader = EgovStringUtil.nullConvert(request.getParameter("titleHeader"));
		String title = "영상접속통계(CCTV/일)";
		if ("".equals(searchMonth)) {
			title = "영상접속통계(CCTV/월)";
		}
		model.put("title", title);
		if ( "".equalsIgnoreCase(searchCctvNm)) {
			model.put("search", "조회기간 : " + searchYear + searchMonth);
		} else {
			model.put("search", "조회기간 : " + searchYear + searchMonth + ", CCTV명 : " + searchCctvNm);
		}
		model.put("fileName", title + searchYear + searchMonth + "_" + "(" + EgovDateUtil.getToday() + ")");
		model.put("titleKey", titleKey);
		model.put("titleHeader", titleHeader);
		model.put("dataList", resultRows);

		return "excelView";
	}
}
