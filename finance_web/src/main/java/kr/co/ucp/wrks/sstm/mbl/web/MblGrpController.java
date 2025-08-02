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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
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
import kr.co.ucp.wrks.sstm.mbl.service.MblGrpService;

/**
 * 모바일 그룹을 관리
 *
 * @author 대전도안 김정원
 * @version 1.00 2014-01-25
 * @since JDK 1.7.0_45(x64)
 * @revision
 */

/**
 * ----------------------------------------------------------
 *
 * @Class Name : MblGrpController
 * @Description : 모바일 그룹관리
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
public class MblGrpController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	/** TRACE */
	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Resource(name="mblGrpService")
	private MblGrpService mblGrpService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	/*
	 * 모바일그룹관리 리스트
	 */
	@RequestMapping(value="/wrks/sstm/mbl/grp.do")
	public String loginUsrView(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();
		
		request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/* 사용유형 */

		/* 그룹명 */
		request.setAttribute("grpList", mblGrpService.grpList(args));
		/* 그룹명 끝 */

		/* 모바일번호 */
		request.setAttribute("moblNoList", mblGrpService.moblNoList(args));
		/* 모바일번호 */

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
		/* row per page 설정 끝 */

		return "wrks/sstm/mbl/grp";
	}

	/*
	 * 모바일그룹관리 조건검색
	 */
	@RequestMapping(value="/wrks/sstm/mbl/grp/list.json")
	@ResponseBody
	public Map<String, Object> getList(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String grpId = (String) request.getParameter("grpId");
		String useTyCd = (String) request.getParameter("useTyCd");
		String pageNo = (String) request.getParameter("page");
		String rowsPerPage = (String) request.getParameter("rows");
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();

		args.put("GRP_ID", grpId);
		args.put("USE_TY_CD", useTyCd);
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = mblGrpService.getList(args);

		map.put("page", pageNo);
		map.put("rows", resultRows);

		return map;
	}

	/*
	 * 모바일그룹관리 입력
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/grp/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("GRP_ID", (String) request.getParameter("grpId"));
		map.put("MOBL_ID", (String) request.getParameter("moblId"));
		map.put("AUTH_LVL", (String) request.getParameter("authLvl"));
		map.put("USE_TY_CD", (String) request.getParameter("useTyCd"));
		map.put("RGS_USER_ID", sesUserId);
		map.put("UPD_USER_ID", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int ret;
		try {
			ret = mblGrpService.insert(map);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			logger.error(e.getRootCause().getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		catch (UncategorizedSQLException e) {
			// dup error
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "그룹에 등록된 번호입니다.");
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
	 * 모바일그룹관리 수정
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/grp/update.json")
	@ResponseBody
	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("GRP_ID", (String) request.getParameter("grpId"));
		map.put("MOBL_ID", (String) request.getParameter("moblId"));
		map.put("AUTH_LVL", (String) request.getParameter("authLvl"));
		map.put("USE_TY_CD", (String) request.getParameter("useTyCd"));
		map.put("GRP_ID_BAK", (String) request.getParameter("grpIdBak"));
		map.put("MOBL_ID_BAK", (String) request.getParameter("moblIdBak"));
		map.put("UPD_USER_ID", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int ret;
		try {
			ret = mblGrpService.update(map);
			mapRet.put("session", 1);
			mapRet.put("msg", "업데이트하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			logger.error(e.getRootCause().getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "그룹에 등록된 번호입니다.");
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
	 * 모바일그룹관리 삭제
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/grp/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("GRP_ID_BAK", (String) request.getParameter("grpIdBak"));
		map.put("MOBL_ID_BAK", (String) request.getParameter("moblIdBak"));
		map.put("UPD_USER_ID", sesUserId);

		int ret = mblGrpService.delete(map);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		mapRet.put("session", 1);
		mapRet.put("msg", "삭제하였습니다.");

		return mapRet;
	}

	/*
	 * 모바일그룹관리 다중삭제
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/grp/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		String[] grpIds = request.getParameterValues("grpId");
		String[] moblIds = request.getParameterValues("moblId");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < moblIds.length; i++) {
			Map<String, Object> mapId = new HashMap<String, Object>();

			mapId.put("GRP_ID_BAK", grpIds[i]);
			mapId.put("MOBL_ID_BAK", moblIds[i]);
			mapId.put("UPD_USER_ID", sesUserId);

			list.add(mapId);
		}

		int ret = mblGrpService.deleteMulti(list);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		mapRet.put("session", 1);
		mapRet.put("msg", "삭제하였습니다.");

		return mapRet;
	}
}
