package kr.co.ucp.wrks.sstm.grp.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.grp.service.GrpAuthService;
import kr.co.ucp.wrks.sstm.menu.service.MenuGrpAuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 그룹별로 권한을 관리
 *
 * @author 대전도안 김정원
 * @version 1.00 2014-01-25
 * @since JDK 1.7.0_45(x64)
 * @revision
 */

/**
 * ----------------------------------------------------------
 *
 * @Class Name : GrpAuthController
 * @Description : 그룹별 권한레벨 관리
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
@Controller("grpAuthController")
public class GrpAuthController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="menuGrpAuthService")
    private MenuGrpAuthService menuGrpAuthService;

	@Resource(name="grpAuthService")
	private GrpAuthService grpAuthService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	/*
	 * 그룹별 권한레벨관리 리스트
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

//    	request.setAttribute("listCmDstrtCdMng", menuGrpAuthService.getCM_DSTRT_CD_MNG(null));

//		Map<String, Object> args = new HashMap<String, Object>();
//
//		args.put("cdGrpId", "USE_TY");
//		args.put("cdTy", "C");
//		args.put("orderBy", "ORDER BY CD_ID ASC");
//		request.setAttribute("useTyCd", codeCmcdService.grpList(args));
//
//		args.put("cdGrpId", "SEND_TY");
//		args.put("cdTy", "C");
//		args.put("orderBy", "ORDER BY CD_ID ASC");
//		request.setAttribute("sendTyCd", codeCmcdService.grpList(args));

		return "wrks/sstm/grp/auth";
	};

	/*
	 * 그룹별 권한레벨관리 조건검색
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth/list.json")
	@ResponseBody
	public Map<String, Object> list(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		String grpNmKo = (String) request.getParameter("grpNmKo");
		String dstrtCd = (String) request.getParameter("dstrtCd");
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();

		args.put("GRP_NM_KO", grpNmKo);
		args.put("DSTRT_CD", dstrtCd);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		List<Map<String, String>> resultRows = grpAuthService.list(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", resultRows);

		return map;
	}

	/*
	 * 그룹별 권한레벨관리 권한레벨 조건검색
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth/list_auth_lvl.json")
	@ResponseBody
	public Map<String, Object> authList(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		String grpId = (String) request.getParameter("grpId");
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();

		args.put("GRP_ID", grpId);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		List<Map<String, String>> resultRows = grpAuthService.authList(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", resultRows);

		return map;
	}

	/*
	 * 그룹별 권한레벨관리 권한레벨 입력
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/grp/auth/insert_auth_lvl.json")
	@ResponseBody
	public Map<String, Object> insert(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, String> map = new HashMap<String, String>();

		map.put("GRP_ID", (String) request.getParameter("grpId"));
		map.put("AUTH_LVL", (String) request.getParameter("authLvl"));
		map.put("AUTH_LVL_NM", (String) request.getParameter("authLvlNm"));
		map.put("AUTH_LVL_TOP", (String) request.getParameter("authLvlTop"));
		map.put("RGS_USER_ID", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		int insertResult;
		try {
			insertResult = grpAuthService.insert(map);
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
	 * 그룹별 권한레벨관리 권한레벨 수정
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/grp/auth/update_auth_lvl.json")
	@ResponseBody
	public Map<String, Object> update(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("GRP_ID", (String) request.getParameter("grpId"));
		map.put("AUTH_LVL", (String) request.getParameter("authLvl"));
		map.put("AUTH_LVL_NM", (String) request.getParameter("authLvlNm"));
		map.put("AUTH_LVL_TOP", (String) request.getParameter("authLvlTop"));
		map.put("UPD_USER_ID", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		int updateResult;
		try {
			updateResult = grpAuthService.update(map);
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
	 * 그룹별 권한레벨관리 권한레벨 삭제
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/grp/auth/delete_auth_lvl.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		// map.put("cdGrpId", (String)request.getParameter("cdGrpId"));
		map.put("GRP_ID", (String) request.getParameter("grpId"));
		map.put("AUTH_LVL", (String) request.getParameter("authLvl"));

		int ret = grpAuthService.delete(map);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		mapRet.put("session", 1);
		mapRet.put("msg", "삭제하였습니다.");

		return mapRet;
	}

	/*
	 * 그룹별 권한레벨관리 권한레벨 다중삭제
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/grp/auth/deleteMulti_auth_lvl.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		String[] grpIds = request.getParameterValues("grpId");
		String[] authLvl = request.getParameterValues("authLvl");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < grpIds.length; i++) {
			Map<String, Object> mapId = new HashMap<String, Object>();

			mapId.put("GRP_ID", grpIds[i]);
			mapId.put("AUTH_LVL", authLvl[i]);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = grpAuthService.deleteMulti(list);
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
