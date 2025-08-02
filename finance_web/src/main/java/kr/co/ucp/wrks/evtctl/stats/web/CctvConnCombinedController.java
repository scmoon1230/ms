/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : cctvConnCombined.jsp
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
import kr.co.ucp.wrks.evtctl.stats.service.CctvConnListService;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;

@Controller
public class CctvConnCombinedController {

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name = "codeCmcdService")
	private CodeCmcdService codeCmcdService;

	@Resource(name = "cctvConnListService")
	private CctvConnListService cctvConnListService;


	@RequestMapping(value = "/wrks/evtctl/stats/cctvConnCombined.do")
	public String view(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
	    //LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        LoginVO lgnVO = SessionUtil.getUserInfo();
        
	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */
	    request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
	    /* row per page 설정 끝 */
		return "/wrks/evtctl/stats/cctvConnCombined";
	}

	@RequestMapping(value = "/wrks/evtctl/stats/cctvConnCombined/list.json")
	@ResponseBody
	public Map<String, Object> getList(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();

		String searchYear = EgovStringUtil.nullConvert(request.getParameter("searchYear"));
		String searchMonth = EgovStringUtil.nullConvert(request.getParameter("searchMonth"));
		String searchUserNm = EgovStringUtil.nullConvert(request.getParameter("searchUserNm"));
		args.put("searchUserNm", searchUserNm);
		args.put("searchYear", searchYear);
		args.put("searchMonth", searchMonth);

		String pageNo = EgovStringUtil.nullConvert(request.getParameter("page"));
		String rowsPerPage = EgovStringUtil.nullConvert(request.getParameter("rows"));
		String sidx = EgovStringUtil.nullConvert(request.getParameter("sidx"));
		String sord = EgovStringUtil.nullConvert(request.getParameter("sord"));
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		List<Map<String, String>> list = cctvConnListService.getList(args);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", pageNo);
		return map;
	}

	@RequestMapping(value = "/wrks/evtctl/stats/cctvConnCombined/list.excel")
	public String getExcel(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String searchYear = EgovStringUtil.nullConvert(request.getParameter("searchYear"));
		String searchMonth = EgovStringUtil.nullConvert(request.getParameter("searchMonth"));
		String searchUserNm = EgovStringUtil.nullConvert(request.getParameter("searchUserNm"));

		Map<String, String> args = new HashMap<String, String>();
		args.put("searchUserNm", searchUserNm);
		args.put("searchYear", searchYear);
		args.put("searchMonth", searchMonth);

		List<Map<String, String>> list = cctvConnListService.getExcel(args);

		String titleKey = EgovStringUtil.nullConvert(request.getParameter("titleKey"));
		String titleHeader = EgovStringUtil.nullConvert(request.getParameter("titleHeader"));
		String search = "조회기간 : " + searchYear + searchMonth + ", 사용자명 : " + searchUserNm;
		String title = "영상접속통계(사용자/일)";
		
		if ("".equals(searchMonth)) {
			title = "영상접속통계(사용자/월)";
			if("".equalsIgnoreCase(searchUserNm)) {
				search = "조회기간 : "+ searchYear;
			}else {
				search = "조회기간 : " + searchYear  + ", 사용자명 : " + searchUserNm;
			}
		}else if("".equalsIgnoreCase(searchUserNm)){
			search = "조회기간 : " + searchYear + searchMonth;
		}else {
			search = "조회기간 : " + searchYear + searchMonth + ", 사용자명 : " + searchUserNm;
		}


		model.put("title", title);
		model.put("search",search);
		model.put("fileName", title + searchYear + searchMonth + "_" + "(" + EgovDateUtil.getToday() + ")" ) ;
		model.put("titleKey", titleKey);
		model.put("titleHeader", titleHeader);
		model.put("dataList", list);

		return "excelView";
	}
}
