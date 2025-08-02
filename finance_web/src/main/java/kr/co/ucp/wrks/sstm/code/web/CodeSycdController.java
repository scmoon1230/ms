package kr.co.ucp.wrks.sstm.code.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.code.service.CodeSycdService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 시스템코드 현황을 관리
 *
 * @author 대전도안 김정원
 * @version 1.00 2014-01-25
 * @since JDK 1.7.0_45(x64)
 * @revision
 */
/**
 * @Class Name : CodeSycdController
 * @Description : 시스템코드
 * @Version : 1.0 Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 *
 *               <pre>
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-01-22   김정원       최초작성
 * ----------------------------------------------------------
 *               </pre>
 */
@Controller
public class CodeSycdController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	@Resource(name="codeSycdService")
	private CodeSycdService codeSycdService;

	/*
	 * 시스템코드 리스트
	 */
	@RequestMapping(value="/wrks/sstm/code/sycd.do")
	public String view(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();

	    request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/* 사용유형 */

		/* U-서비스 그룹 설정 */
		args.clear();
		args.put("cdGrpId", "USVC_GRP");
		args.put("cdTy", "C");
		request.setAttribute("uServiceGrp", codeCmcdService.grpList(args));

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "wrks/sstm/code/sycd";
	}

	/*
	 * 시스템코드 조건검색
	 */

	@RequestMapping(value="/wrks/sstm/code/sycd/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		String userId = (String) request.getParameter("userId");
		String pageNo = (String) request.getParameter("page");
		String rowsPerPage = (String) request.getParameter("rows");
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");
		String usvcGrp = (String) request.getParameter("usvcGrp");
		String sysNm = (String) request.getParameter("sysNm");
		String useTyCd = (String) request.getParameter("useTyCd");

		Map<String, String> args = new HashMap<String, String>();

		args.put("userId", userId);
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		args.put("USVC_GRP_CD", usvcGrp);
		args.put("SYS_NM_KO", sysNm);
		args.put("USE_TY_CD", useTyCd);

		List<Map<String, String>> list = codeSycdService.sycdList(args);

		map.put("rows", list);
		map.put("page", pageNo);

		return map;
	}

	/*
	 * 시스템코드 입력
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/sycd/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("SYS_CD", (String) request.getParameter("sysCd"));
		map.put("SYS_NM_KO", (String) request.getParameter("sysNmKo"));
		map.put("SYS_NM_EN", (String) request.getParameter("sysNmEn"));
		map.put("DSCRT", (String) request.getParameter("dscrt"));
		map.put("USVC_GRP_CD", (String) request.getParameter("usvcGrpCd"));
		map.put("USE_TY_CD", (String) request.getParameter("useTyCd"));
		map.put("RGS_USER_ID", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int insertResult;
		try {
			insertResult = codeSycdService.insert(map);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			if (e.getRootCause().getMessage().indexOf("UNIQUE constraint violation") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 코드입니다.");
			}
			else {
				logger.error(e.getRootCause().getMessage());
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
	 * 시스템코드 수정
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/sycd/update.json")
	@ResponseBody
	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("SYS_CD", (String) request.getParameter("sysCd"));
		map.put("sysCdBk", (String) request.getParameter("sysCdBk"));
		map.put("SYS_NM_KO", (String) request.getParameter("sysNmKo"));
		map.put("SYS_NM_EN", (String) request.getParameter("sysNmEn"));
		map.put("DSCRT", (String) request.getParameter("dscrt"));
		map.put("USVC_GRP_CD", (String) request.getParameter("usvcGrpCd"));
		map.put("USE_TY_CD", (String) request.getParameter("useTyCd"));
		map.put("UPD_USER_ID", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int updateResult;
		try {
			updateResult = codeSycdService.update(map);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			logger.error(e.getRootCause().getMessage());
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
	 * 시스템코드 삭제
	 */
	@RequestMapping(value="/wrks/sstm/code/sycd/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, String> map = new HashMap<String, String>();

		map.put("sysCdBk", (String) request.getParameter("sysCdBk"));
		map.put("UPD_USER_ID", sesUserId);

		int result = codeSycdService.delete(map);

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
	 * 시스템코드 다중삭제
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/sycd/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();
		String[] sysCdIds = request.getParameterValues("sysCdBk");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sysCdIds.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("sysCdBk", sysCdIds[i]);
			mapId.put("UPD_USER_ID", sesUserId);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = codeSycdService.deleteMulti(list);
			mapRet.put("session", 1);
			mapRet.put("msg", "삭제하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			logger.error(e.getRootCause().getMessage());
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
