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
import kr.co.ucp.wrks.sstm.menu.service.MenuInfoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 프로그램정보를 관리
 *
 * @author 대전도안 김정원
 * @version 1.00 2014-01-25
 * @since JDK 1.7.0_45(x64)
 * @revision
 */

/**
 * ----------------------------------------------------------
 *
 * @Class Name : MenuInfoController
 * @Description : 프로그램정보
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
public class MenuInfoController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	/** TRACE */
	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	@Resource(name="menuInfoService")
	private MenuInfoService menuInfoService;

	/*
	 * 프로그램정보 리스트
	 */
	@RequestMapping(value="/wrks/sstm/menu/info.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();

		/* 프로그램 기능유형 설정 */
		args.put("CD_GRP_ID", "PGM_FNCT_TY");
		args.put("CD_TY", "C");
		args.put("orderBy", "ORDER BY CD_ID DESC");
		request.setAttribute("pgmFnctList", menuInfoService.fnctList(args));

	    request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/* 사용유무 설정 */

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		return "wrks/sstm/menu/info";
	}

	/*
	 * 프로그램정보 조건검색
	 */
	@RequestMapping(value="/wrks/sstm/menu/info/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		String userId = (String) request.getParameter("userId");
		String pageNo = (String) request.getParameter("page");
		String rowsPerPage = (String) request.getParameter("rows");
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");
		String pgmNm = (String) request.getParameter("pgmNm");
		String pgmUrl = (String) request.getParameter("pgmUrl");
		String useTyCd = (String) request.getParameter("useTyCd");
		String pgmDscrt = (String) request.getParameter("pgmDscrt");

		Map<String, String> args = new HashMap<String, String>();

		args.put("userId", userId);
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		args.put("PGM_NM", pgmNm);
		args.put("PGM_URL", pgmUrl);
		args.put("USE_TY_CD", useTyCd);
		args.put("PGM_DSCRT", pgmDscrt);

		List<Map<String, String>> list = menuInfoService.list(args);

		map.put("rows", list);
		map.put("page", pageNo);

		return map;
	}

	/*
	 * 프로그램정보 등록
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/menu/info/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("PGM_ID", (String) request.getParameter("pgmId"));
		map.put("PRNT_PGM_ID", (String) request.getParameter("prntPgmId"));
		map.put("PGM_FNCT_TY_CD", (String) request.getParameter("pgmFnctTyCd"));
		map.put("PGM_URL", (String) request.getParameter("pgmUrl"));
		map.put("PGM_NM", (String) request.getParameter("pgmNm"));
		map.put("USE_TY_CD", (String) request.getParameter("useTyCd"));
		map.put("PGM_DSCRT", (String) request.getParameter("pgmDscrt"));
		map.put("RGS_USER_ID", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		int insertResult;
		try {
			insertResult = menuInfoService.insert(map);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			if (e.getRootCause().getMessage().indexOf("UNIQUE constraint violation") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 코드입니다.");
				return mapRet;
			}
			else {
				logger.error(e.getCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}

		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 코드입니다.");
			}
			else {
				logger.error(e.getCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	/*
	 * 프로그램정보 수정
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/menu/info/update.json")
	@ResponseBody
	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("PGM_ID", (String) request.getParameter("pgmId"));
		map.put("PRNT_PGM_ID", (String) request.getParameter("prntPgmId"));
		map.put("PGM_FNCT_TY_CD", (String) request.getParameter("pgmFnctTyCd"));
		map.put("PGM_URL", (String) request.getParameter("pgmUrl"));
		map.put("PGM_NM", (String) request.getParameter("pgmNm"));
		map.put("USE_TY_CD", (String) request.getParameter("useTyCd"));
		map.put("PGM_DSCRT", (String) request.getParameter("pgmDscrt"));
		map.put("UPD_USER_ID", sesUserId);
		map.put("pgmIdBk", (String) request.getParameter("pgmIdBk"));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		int updateResult;
		try {
			updateResult = menuInfoService.update(map);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			logger.error(e.getCause().getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 코드입니다.");
			}
			else {
				logger.error(e.getCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	/*
	 * 프로그램정보 삭제
	 */
	@RequestMapping(value="/wrks/sstm/menu/info/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("PGM_ID", (String) request.getParameter("pgmIdBk"));
		map.put("UPD_USER_ID", sesUserId);

		int result = menuInfoService.delete(map);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (result > 0) {
			mapRet.put("session", 1);
			mapRet.put("msg", "삭제하였습니다.");
		}
		else {
			mapRet.put("session", 2);
			mapRet.put("msg", "삭제실패");
		}
		return mapRet;
	}

	/*
	 * 프로그램정보 다중삭제
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/menu/info/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		String[] pgmIdBk = request.getParameterValues("pgmIdBk");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < pgmIdBk.length; i++) {
			Map<String, Object> mapId = new HashMap<String, Object>();
			mapId.put("PGM_ID", pgmIdBk[i]);
			mapId.put("UPD_USER_ID", sesUserId);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();

		int ret;
		try {
			ret = menuInfoService.deleteMulti(list);
			mapRet.put("session", 1);
			mapRet.put("msg", "삭제하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			logger.error(e.getCause().getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 에러가 발생했습니다.");
			}
			else {
				logger.error(e.getCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}
}
