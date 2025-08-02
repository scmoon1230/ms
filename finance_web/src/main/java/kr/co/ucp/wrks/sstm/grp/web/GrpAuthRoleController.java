package kr.co.ucp.wrks.sstm.grp.web;

import java.net.URLDecoder;
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
import kr.co.ucp.wrks.sstm.grp.service.GrpAuthRoleService;
import kr.co.ucp.wrks.sstm.grp.service.GrpUserAccService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @author 대전도안 설준환
 * @version 1.00 2015-03-12
 * @since JDK 1.7.0_45(x64)
 * @revision
 */

/**
 * ----------------------------------------------------------
 *
 * @Class Name : GrpAuthRoleController
 * @Description : 그룹별권한레벨별롤
 * @Version : 1.0 Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 *
 *               <pre>
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-03-12   설준환       최초작성
 *
 * ----------------------------------------------------------
 *               </pre>
 */
@Controller
public class GrpAuthRoleController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="grpAuthRoleService")
	private GrpAuthRoleService grpAuthRoleService;

	@Resource(name="grpUserAccService")
	private GrpUserAccService grpUserAccService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

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
	 * 그룹별권한레벨별롤 리스트
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth_role.do")
	public String view(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		Map<String, Object> args = new HashMap<String, Object>();

		/* 지구코드 조회 */
		request.setAttribute("listCmDstrtCdMng", grpUserAccService.getCM_DSTRT_CD_MNG(null));

		request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/* 사용유형 */

		return "wrks/sstm/grp/auth_role";
	}

	/*
	 * 그룹별권한레벨별롤 그룹 조회
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth_role/list_grp.json")
	@ResponseBody
	public Map<String, Object> list_grp(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		String dstrtCd = request.getParameter("dstrtCd");
		String grpNmKo = request.getParameter("grpNmKo");
		String authLvlNm = request.getParameter("authLvlNm");
		String useTyCd = request.getParameter("useTyCd");
		String sidx = request.getParameter("sidx");
		String sord = request.getParameter("sord");
		Map<String, String> args = new HashMap<String, String>();

		args.put("dstrtCd", dstrtCd);
		args.put("grpNmKo", grpNmKo);
		args.put("authLvlNm", authLvlNm);
		args.put("useTyCd", useTyCd);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		List<Map<String, String>> resultRows = grpAuthRoleService.list_grp(args);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("rows", resultRows);

		return map;
	}

	/*
	 * 그룹별권한레벨별롤 롤 조회
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth_role/list_role.json")
	@ResponseBody
	public Map<String, Object> list_role(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		String grpId = request.getParameter("grpId");
		String authLvl = request.getParameter("authLvl");
		String sidx = request.getParameter("sidx");
		String sord = request.getParameter("sord");
		Map<String, String> args = new HashMap<String, String>();

		args.put("grpId", grpId);
		args.put("authLvl", authLvl);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		List<Map<String, String>> resultRows = grpAuthRoleService.list_role(args);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("rows", resultRows);

		return map;
	}

	/*
	 * 그룹별권한레벨별롤 롤 조회 팝업
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth_role/list_popup.json")
	@ResponseBody
	public Map<String, Object> list_popup(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		String grpId = request.getParameter("grpId");
		String authLvl = request.getParameter("authLvl");
		String sidx = request.getParameter("sidx");
		String sord = request.getParameter("sord");
		Map<String, String> args = new HashMap<String, String>();

		args.put("grpId", grpId);
		args.put("authLvl", authLvl);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		List<Map<String, String>> resultRows = grpAuthRoleService.list_popup(args);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("rows", resultRows);

		return map;
	}

	/*
	 * 그룹별권한레벨별롤 롤 추가
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/grp/auth_role/insert_role.json")
	@ResponseBody
	public Map<String, Object> insert_role(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> mapRet = new HashMap<String, Object>();
		try {

			String rgsUserId = lgnVO.getUserId();
			String grpId = URLDecoder.decode(request.getParameter("grpId"), "UTF-8");
			String authLvl = URLDecoder.decode(request.getParameter("authLvl"), "UTF-8");
			String[] roleIds = request.getParameterValues("roleId");

			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			for (int i = 0; i < roleIds.length; i++) {
				Map<String, String> mapId = new HashMap<String, String>();

				mapId.put("rgsUserId", rgsUserId);
				mapId.put("grpId", grpId);
				mapId.put("authLvl", authLvl);
				mapId.put("roleId", URLDecoder.decode(roleIds[i], "UTF-8"));

				list.add(mapId);
			}

			int ret = grpAuthRoleService.insert_role(list);
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
				mapRet.put("msg", "키 중복에러입니다.");
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
	 * 그룹별권한레벨별롤 롤 삭제
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth_role/delete.json")
	@ResponseBody
	public Map<String, Object> delete(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("grpId", URLDecoder.decode(request.getParameter("grpId"), "UTF-8"));
		map.put("authLvl", URLDecoder.decode(request.getParameter("authLvl"), "UTF-8"));
		map.put("roleId", URLDecoder.decode(request.getParameter("roleId"), "UTF-8"));

		int result = grpAuthRoleService.delete(map);

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
	 * 그룹별권한레벨별롤 롤 다중삭제
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth_role/delete_role.json")
	@ResponseBody
	public Map<String, Object> delete_rcv(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		try {

			String grpId = URLDecoder.decode(request.getParameter("grpId"), "UTF-8");
			String authLvl = URLDecoder.decode(request.getParameter("authLvl"), "UTF-8");
			String[] roleIds = request.getParameterValues("roleId");

			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			for (int i = 0; i < roleIds.length; i++) {
				Map<String, String> mapId = new HashMap<String, String>();

				mapId.put("grpId", grpId);
				mapId.put("authLvl", authLvl);
				mapId.put("roleId", URLDecoder.decode(roleIds[i], "UTF-8"));

				list.add(mapId);
			}

			grpAuthRoleService.delete_role(list);

			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		}
		catch (Exception e) {
			mapRet.put("session", 0);
			mapRet.put("msg", "알수없는 에러입니다."); // TODO: SQL오류 메세지 처리
		}
		return mapRet;
	}
}