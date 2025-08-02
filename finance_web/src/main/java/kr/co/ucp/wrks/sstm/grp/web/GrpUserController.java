package kr.co.ucp.wrks.sstm.grp.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.wrks.sstm.grp.service.GrpUserService;

/**
 * 그룹별로 그룹에 속한 사용자를 조회
 *
 * @author 대전도안 김정원
 * @version 1.00 2014-01-25
 * @since JDK 1.7.0_45(x64)
 * @revision
 */

/**
 * ----------------------------------------------------------
 *
 * @Class Name : GrpUserController
 * @Description : 그룹별 사용자 조회
 * @Version : 1.0 Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 *
 *               <pre>
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-01-22   김정원       최초작성
 *
 * ----------------------------------------------------------
 *               </pre>
 */
@Controller
public class GrpUserController {

	@Resource(name="grpUserService")
	private GrpUserService grpUserService;

	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
//	@Resource(name="propertiesService")
//	protected EgovPropertyService propertiesService;

	/** TRACE */
	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	/*
	 * 그룹별 사용자조회 리스트
	 */
	@RequestMapping(value="/wrks/sstm/grp/user.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		/* 지구코드조회 */
		//request.setAttribute("listCmDstrtCdMng", grpUserService.getCM_DSTRT_CD_MNG(null));

		return "wrks/sstm/grp/user";
	}

	/*
	 * 그룹별 사용자조회 그룹 조건검색
	 */
	@RequestMapping(value="/wrks/sstm/grp/user/list_group.json")
	@ResponseBody
	public Map<String, Object> list_group(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		//String dstrtCd = (String) request.getParameter("dstrtCd");
		String grpId = (String) request.getParameter("grpId");
		String grpNm = (String) request.getParameter("grpNm");
		String pageNo = (String) request.getParameter("page");
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();

		//args.put("dstrtCd", dstrtCd);
		args.put("grpId", grpId);
		args.put("grpNm", grpNm);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = grpUserService.getCM_GROUP(args);

		map.put("page", pageNo);
		map.put("rows", resultRows);

		return map;
	}

	/*
	 * 그룹별 사용자조회 그룹에 따른 사용자 조건검색
	 */
	@RequestMapping(value="/wrks/sstm/grp/user/list_user.json")
	@ResponseBody
	public Map<String, Object> list_user(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		String grpId = (String) request.getParameter("grpId");
		String authLvl = (String) request.getParameter("authLvl");
		String userId = (String) request.getParameter("userId");
		String userNm = (String) request.getParameter("userNm");
		String userNmKo = (String) request.getParameter("userNmKo");
		String moblNo = (String) request.getParameter("moblNo");
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();

		args.put("grpId", grpId);
		args.put("authLvl", authLvl);
		args.put("userId", userId);
		args.put("userNm", userNm);
		args.put("userNmKo", userNmKo);
		args.put("moblNo", moblNo);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		List<Map<String, String>> resultRows = grpUserService.getCM_GRP_USER(args);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("rows", resultRows);
		return map;
	}
}
