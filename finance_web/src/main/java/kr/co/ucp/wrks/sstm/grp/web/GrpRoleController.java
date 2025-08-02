package kr.co.ucp.wrks.sstm.grp.web;

import java.net.URLDecoder;
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
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.grp.service.GrpRoleService;

/**
 * 롤 현황을 관리
 *
 * @author 대전도안 설준환
 * @version 1.00 2014-02-16
 * @since JDK 1.7.0_45(x64)
 * @revision
 */

/**
 * ----------------------------------------------------------
 *
 * @Class Name : GrpRoleController
 * @Description : 롤관리
 * @Version : 1.0 Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 *
 *               <pre>
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-02-16   설준환       최초작성
 *
 * ----------------------------------------------------------
 *               </pre>
 */
@Controller
public class GrpRoleController {

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

	@Resource(name="grpRoleService")
	private GrpRoleService grpRoleService;

	/*
	 * 롤 리스트
	 */
	@RequestMapping(value="/wrks/sstm/grp/role.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();

	    request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/* 사용유형 */

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
		return "wrks/sstm/grp/role";
	}

	/*
	 * 롤 조건검색
	 */
	@RequestMapping(value="/wrks/sstm/grp/role/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		String pageNo = request.getParameter("page");
		String rowsPerPage = request.getParameter("rows");
		String sidx = request.getParameter("sidx");
		String sord = request.getParameter("sord");
		String roleNm = request.getParameter("roleNm");

		Map<String, String> args = new HashMap<String, String>();

		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		args.put("roleNm", roleNm);

		@SuppressWarnings("unchecked")
		List<Map<String, String>> list = grpRoleService.view(args);

		map.put("rows", list);
		map.put("page", pageNo);
		return map;
	}

	/*
	 * 롤 입력
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/grp/role/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("roleId", URLDecoder.decode(request.getParameter("roleId"), "UTF-8"));
			map.put("roleNm", URLDecoder.decode(request.getParameter("roleNm"), "UTF-8"));
			map.put("rolePttrn", URLDecoder.decode(request.getParameter("rolePttrn"), "UTF-8"));
			map.put("roleTyCd", URLDecoder.decode(request.getParameter("roleTyCd"), "UTF-8"));
			map.put("sortOrdr", URLDecoder.decode(request.getParameter("sortOrdr"), "UTF-8"));
			map.put("rgsUserId", sesUserId);
			int insertResult = grpRoleService.insert(map);
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
				mapRet.put("msg", "이미 등록된 아이디입니다.");
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
	 * 롤 수정
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/grp/role/update.json")
	@ResponseBody
	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		try {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("roleId", URLDecoder.decode(request.getParameter("roleId"), "UTF-8"));
			map.put("roleNm", URLDecoder.decode(request.getParameter("roleNm"), "UTF-8"));
			map.put("rolePttrn", URLDecoder.decode(request.getParameter("rolePttrn"), "UTF-8"));
			map.put("roleTyCd", URLDecoder.decode(request.getParameter("roleTyCd"), "UTF-8"));
			map.put("sortOrdr", URLDecoder.decode(request.getParameter("sortOrdr"), "UTF-8"));
			map.put("updUserId", sesUserId);

			int updateResult = grpRoleService.update(map);
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
				mapRet.put("msg", "이미 등록된 아이디입니다.");
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
	 * 롤 삭제
	 */
	@RequestMapping(value="/wrks/sstm/grp/role/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("roleId", request.getParameter("roleId"));

		int result = grpRoleService.delete(map);

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
	 * 롤 다중삭제
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/grp/role/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		String[] roleIdIds = request.getParameterValues("roleId");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < roleIdIds.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("roleId", roleIdIds[i]);
			list.add(mapId);
		}

		int ret;
		try {
			ret = grpRoleService.deleteMulti(list);
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
