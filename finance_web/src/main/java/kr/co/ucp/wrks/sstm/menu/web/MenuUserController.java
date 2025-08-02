package kr.co.ucp.wrks.sstm.menu.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.menu.service.MenuUserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 사용자별로 프로그램에대한 권한을 관리
 *
 * @author 대전도안 김정원
 * @version 1.00 2014-01-25
 * @since JDK 1.7.0_45(x64)
 * @revision
 */

/**
 * ----------------------------------------------------------
 *
 * @Class Name : MenuUserController
 * @Description : 사용자별 프로그램메뉴
 * @Version : 1.0 Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 *
 *               <pre>
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-01-22   김정원       최초작성
 * 2019-09-18   SaintJuny    시큐어 코딩 처리
 * ----------------------------------------------------------
 *               </pre>
 */
@Controller
public class MenuUserController {

	@Resource(name="menuUserService")
	private MenuUserService menuUserService;

	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	/** TRACE */
	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Resource(name="codeCmcdService")
	CodeCmcdService codeCmcdService;

	/*
	 * 사용자별 프로그램 메뉴 리스트
	 */
	@RequestMapping(value="/wrks/sstm/menu/user.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();

	    request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/* 사용유무 설정 */

		request.setAttribute("listCmDstrtCdMng", menuUserService.getCM_DSTRT_CD_MNG(null));

		return "wrks/sstm/menu/user";
	}

	/*
	 * 사용자별 프로그램 메뉴 사용자 리스트
	 */
	@RequestMapping(value="/wrks/sstm/menu/user/list_user.json")
	@ResponseBody
	public Map<String, Object> list_user(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String userNm = (String) request.getParameter("userNm");
		String userId = (String) request.getParameter("userId");
		String grpNm = (String) request.getParameter("grpNm");
		String dstrtCd = (String) request.getParameter("dstrtCd");
		String useTyCd = (String) request.getParameter("useTyCd");
		String pageNo = (String) request.getParameter("page");

		Map<String, String> args = new HashMap<String, String>();

		args.put("USER_ID", userId);
		args.put("USER_NM_KO", userNm);
		args.put("GRP_NM_KO", grpNm);
		args.put("DSTRT_CD", dstrtCd);
		args.put("USE_TY_CD", useTyCd);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = menuUserService.list_user(args);

		resultMap.put("page", pageNo);
		resultMap.put("rows", resultRows);

		return resultMap;
	}

	/*
	 * 사용자별 프로그램 메뉴 프로그램 리스트
	 */
	@RequestMapping(value="/wrks/sstm/menu/user/list.json")
	@ResponseBody
	public Map<String, Object> getList(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String userId = (String) request.getParameter("userId");
		String dstrtCd = (String) request.getParameter("dstrtCd");
		String pageNo = (String) request.getParameter("page");

		Map<String, String> args = new HashMap<String, String>();
		args.put("USER_ID", userId);
		args.put("DSTRT_CD", dstrtCd);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = menuUserService.list(args);

		resultMap.put("page", pageNo);
		resultMap.put("rows", resultRows);

		return resultMap;
	}

	/*
	 * 사용자별 프로그램 메뉴 수정
	 */
	@RequestMapping(value="/wrks/sstm/menu/user/update.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();
		String userId = (String) request.getParameter("userId");
		String dstrtCd = (String) request.getParameter("dstrtCd");
		String[] pgmMenuIds = request.getParameterValues("pgmMenuId");
		String[] values = request.getParameterValues("value");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < pgmMenuIds.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();

			mapId.put("USER_ID", userId);
			mapId.put("DSTRT_CD", dstrtCd);
			mapId.put("PGM_MENU_ID", pgmMenuIds[i]);
			mapId.put("RGS_AUTH_YN", values[i].substring(0, 1));
			mapId.put("SEA_AUTH_YN", values[i].substring(1, 2));
			mapId.put("UPD_AUTH_YN", values[i].substring(2, 3));
			mapId.put("DEL_AUTH_YN", values[i].substring(3, 4));
			mapId.put("RGS_USER_ID", sesUserId);
			mapId.put("UPD_USER_ID", sesUserId);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();

		menuUserService.update(list);

		mapRet.put("session", 1);
		mapRet.put("msg", "저장하였습니다.");

		return mapRet;
	}

	/*
	 * 사용자별 프로그램 메뉴 복사
	 */
	@RequestMapping(value="/wrks/sstm/menu/user/copy.json")
	@ResponseBody
	public Map<String, Object> copy(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();
		String userId = (String) request.getParameter("userId");
		String dstrtCd = (String) request.getParameter("dstrtCd");
		String userIdTo = (String) request.getParameter("userIdTo");
		String dstrtCdTo = (String) request.getParameter("dstrtCdTo");

		Map<String, String> args = new HashMap<String, String>();
		args.put("USER_ID", userId);
		args.put("DSTRT_CD", dstrtCd);
		args.put("USER_ID_TO", userIdTo);
		args.put("DSTRT_CD_TO", dstrtCdTo);
		args.put("RGS_USER_ID", sesUserId);
		args.put("UPD_USER_ID", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		menuUserService.copy(args);

		mapRet.put("session", 1);
		mapRet.put("msg", "저장하였습니다.");

		return mapRet;
	}
}
