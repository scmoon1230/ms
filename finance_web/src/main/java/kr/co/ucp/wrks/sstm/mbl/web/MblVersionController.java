package kr.co.ucp.wrks.sstm.mbl.web;

import java.util.ArrayList;
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
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.mbl.service.MblVersionService;

/**
 * 모바일 기기의 앱버전정보를 관리
 *
 * @author 대전도안 김정원
 * @version 1.00 2014-01-25
 * @since JDK 1.7.0_45(x64)
 * @revision
 */

/**
 * ----------------------------------------------------------
 *
 * @Class Name : MblVersionController
 * @Description : 모바일 앱버전정보
 * @Version : 1.0 Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 *
 *               <pre>
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-01-22   김정원       최초작성
 * 2019-09-17   SaintJuny    시큐어 코딩 처리
 * ----------------------------------------------------------
 *               </pre>
 */
@Controller
public class MblVersionController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	/** TRACE */
	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Resource(name="mblVersionService")
	private MblVersionService mblVersionService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	/*
	 * 앱버전정보 리스트
	 */
	@RequestMapping(value="/wrks/sstm/mbl/version.do")
	public String loginUsrView(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		/* 모바일기기 종류 */
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("cdGrpId", "MOBL");
		args.put("cdTy", "C");
		args.put("orderBy", "ORDER BY CD_ID ASC");

		request.setAttribute("mppList", codeCmcdService.grpList(args));

		/* 모바일기기 OS유형 */
		args.clear();
		args.put("cdGrpId", "MOBL_OS");
		args.put("cdTy", "C");
		args.put("orderBy", "ORDER BY CD_ID ASC");
		request.setAttribute("mppOsList", codeCmcdService.grpList(args));

		/* 모바일기기 특성종류 */
		/*
		 * args.clear(); args.put("cdGrpId", "MOBL_CHARTR"); args.put("cdTy", "C"); args.put("orderBy", "ORDER BY CD_ID ASC"); request.setAttribute("mppChartrList", codeCmcdService.grpList(args));
		 */
		/* 모바일기기 통신유형 */
		/*
		 * args.clear(); args.put("cdGrpId", "MOBL_COMM"); args.put("cdTy", "C"); args.put("orderBy", "ORDER BY CD_ID ASC"); request.setAttribute("mppCommList", codeCmcdService.grpList(args));
		 */

		request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/* 사용유형 */

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
		/* row per page 설정 끝 */

		return "wrks/sstm/mbl/version";
	}

	@RequestMapping(value="/wrks/sstm/mbl/version/getUser.json")
	@ResponseBody
	public Map<String, String> getUser(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String userId = (String) request.getSession().getAttribute("userId");

		if (userId == null) {
			userId = "userId1";
		}

		Map<String, String> mapD = mblVersionService.getUser(userId);

		return mapD;
	}

	/*
	 * 앱버전정보 조건검색
	 */
	@RequestMapping(value="/wrks/sstm/mbl/version/list.json")
	@ResponseBody
	public Map<String, Object> getList(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String moblOs = (String) request.getParameter("moblOs");
		String moblKndCd = (String) request.getParameter("moblKndCd");
		String useTyCd = (String) request.getParameter("useTyCd");
		String pageNo = (String) request.getParameter("page");
		String rowsPerPage = (String) request.getParameter("rows");
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();

		args.put("moblOs", moblOs);
		args.put("moblKndCd", moblKndCd);
		args.put("useTyCd", useTyCd);
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = mblVersionService.getList(args);

		map.put("page", pageNo);
		map.put("rows", resultRows);

		return map;
	}

	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/version/listdetail.json")
	@ResponseBody
	public Map<String, Object> getListDetail(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String mppId = (String) request.getParameter("strMppId");

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = mblVersionService.getListDetail(mppId);

		return map;
	}

	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/version/listmaptype.json")
	@ResponseBody
	public Map<String, Object> getListMapType(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = mblVersionService.getListMapType();

		return map;
	}

	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/version/listostype.json")
	@ResponseBody
	public Map<String, Object> getListOsType(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = mblVersionService.getListOsType();

		return map;
	}

	/*
	 * 앱버전정보 입력
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/version/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("moblKndCd", (String) request.getParameter("moblKndCd"));
		map.put("moblOsTyCd", (String) request.getParameter("moblOsTyCd"));
		map.put("appVerNo", (String) request.getParameter("appVerNo"));
		map.put("appDwldUrl", (String) request.getParameter("appDwldUrl"));
		map.put("useTyCd", (String) request.getParameter("useTyCd"));
		map.put("rgsUserId", sesUserId);
		map.put("updUserId", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int ret = mblVersionService.insert(map);

		mapRet.put("session", 1);
		mapRet.put("msg", "저장하였습니다.");

		return mapRet;
	}

	/*
	 * 앱버전정보 수정
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/version/update.json")
	@ResponseBody
	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("moblAppId", (String) request.getParameter("moblAppId"));
		map.put("moblKndCd", (String) request.getParameter("moblKndCd"));
		map.put("moblOsTyCd", (String) request.getParameter("moblOsTyCd"));
		map.put("appVerNo", (String) request.getParameter("appVerNo"));
		map.put("appDwldUrl", (String) request.getParameter("appDwldUrl"));
		map.put("useTyCd", (String) request.getParameter("useTyCd"));
		map.put("rgsUserId", sesUserId);
		map.put("updUserId", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int ret = mblVersionService.update(map);

		mapRet.put("session", 1);
		mapRet.put("msg", "업데이트하였습니다.");

		return mapRet;
	}

	/*
	 * 앱버전정보 삭제
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/version/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("moblAppIdBak", (String) request.getParameter("moblAppId"));
		map.put("updUserIdBak", sesUserId);

		int ret = mblVersionService.delete(map);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		mapRet.put("session", 1);
		mapRet.put("msg", "삭제하였습니다.");

		return mapRet;
	}

	/*
	 * 앱버전정보 다중삭제
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/version/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		String[] moblAppIds = request.getParameterValues("moblAppId");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < moblAppIds.length; i++) {
			Map<String, Object> mapId = new HashMap<String, Object>();

			mapId.put("moblAppIdBak", moblAppIds[i]);
			mapId.put("updUserIdBak", sesUserId);

			list.add(mapId);
		}

		int ret = mblVersionService.deleteMulti(list);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		mapRet.put("session", 1);
		mapRet.put("msg", "삭제하였습니다.");

		return mapRet;
	}

}
