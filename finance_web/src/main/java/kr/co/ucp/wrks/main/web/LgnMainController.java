/**
 * -----------------------------------------------------------------
 * @Class Name : LgnMainController.java
 * @Description : 로그인
 * @Version : 1.0
 * Copyright (c) 2016 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * -----------------------------------------------------------------
 * DATE             AUTHOR      DESCRIPTION
 * -----------------------------------------------------------------
 * 2016. 12. 5.    seungJun    최초작성
 * -----------------------------------------------------------------
 */

package kr.co.ucp.wrks.main.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.main.service.LgnMainService;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;

@Controller
public class LgnMainController
{

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	// EgovLoginService
	@Resource(name="lgnMainService")
	private LgnMainService lgnMainService;

	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

//	@Resource(name="propertiesService")
//	protected EgovPropertyService propertiesService;

	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@RequestMapping(value="/wrks/main/main.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, String> args = new HashMap<String, String>();
		args.put("USER_ID", sesUserId);
		List<Map<String, String>> lst = lgnMainService.getStatsList(args);
		request.setAttribute("mainList", lst);
		args.put("pgmMenuNmKo", "메신저");
		args.put("sysId", lgnVO.getSysId());
		request.setAttribute("messenger", lgnMainService.getMessengerId(args));

//		request.setAttribute("statsListJson", new Gson().toJson(lst));
//		Map<String, Object> args2 = new HashMap<String, Object>();
//		args2.put("cdGrpId", "T01");
//		args2.put("cdTy", "C");
//		args2.put("orderBy", "ORDER BY CD_ID ASC");
//		request.setAttribute("troubleGbList", codeCmcdService.grpList(args2));

		//System.out.println("main------------------------------------------------------");
		//System.out.println("HTTP_X_FORWARDED_FOR : "  + request.getHeader("HTTP_X_FORWARDED_FOR"));
		//System.out.println("REMOTE_ADDR : " + request.getHeader("REMOTE_ADDR"));
		//System.out.println("getRemoteAddr() : " + request.getRemoteAddr());

		request.setAttribute("HTTP_X_FORWARDED_FOR", request.getHeader("HTTP_X_FORWARDED_FOR"));
		request.setAttribute("REMOTE_ADDR", request.getHeader("REMOTE_ADDR"));
		request.setAttribute("getRemoteAddr", request.getRemoteAddr());

		return "wrks/main/main";
	}

	@RequestMapping(value="/wrks/main/main/list.json")
	@ResponseBody
	public Map<String, Object> getList(ModelMap model ,HttpServletRequest request ,HttpServletResponse response ) throws Exception
	{
		String pageNo =  (String)request.getParameter("page");

		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", request.getParameter("rows"));
		args.put("sidx"       , request.getParameter("sidx"));
		args.put("sord"       , request.getParameter("sord"));

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = lgnMainService.getList(args);

		map.put("page", pageNo);
		map.put("rows", resultRows);
		return map;
	}

	@RequestMapping(value="/wrks/main/main/chartcnt.do")
	public String getChartCnt(ModelMap model ,HttpServletRequest request ,HttpServletResponse response ) throws Exception
	{
		Map<String, Object> args2 = new HashMap<String, Object>();
		args2.put("cdGrpId", "T01");
		args2.put("cdTy", "C");
		args2.put("orderBy", "ORDER BY CD_ID ASC");
		request.setAttribute("troubleGbList", codeCmcdService.grpList(args2));

		return "wrks/main/main_chart_cnt";
	}

	@RequestMapping(value="/wrks/main/main/chartamt.do")
	public String getChartAmt(ModelMap model ,HttpServletRequest request ,HttpServletResponse response ) throws Exception
	{
		Map<String, Object> args2 = new HashMap<String, Object>();
		args2.put("cdGrpId", "T01");
		args2.put("cdTy", "C");
		args2.put("orderBy", "ORDER BY CD_ID ASC");
		request.setAttribute("troubleGbList", codeCmcdService.grpList(args2));

		return "wrks/main/main_chart_amt";
	}

}