/**
 * -----------------------------------------------------------------
 *
 * @Class Name : GrpLevCctvAuthController.java
 * @Description :
 * @Version : 1.0 Copyright (c) 2018 by KR.CO.UCP All rights reserved.
 * @Modification Information
 *
 *               <pre>
 * -----------------------------------------------------------------
 * DATE           AUTHOR      DESCRIPTION
 * -----------------------------------------------------------------
 * 2018. 4. 6.   seungJun      New Write
 * -----------------------------------------------------------------
 *               </pre>
 */
package kr.co.ucp.wrks.sstm.grp.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.grp.service.GrpLevCctvAuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GrpLevCctvAuthController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="grpLevCctvAuthService")
	private GrpLevCctvAuthService grpLevCctvAuthService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	@RequestMapping(value="/wrks/sstm/grp/cctvAuthType.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("cdGrpId", "CCTV_AUTH_TY");
		args.put("cdTy", "C");
		args.put("orderBy", "ORDER BY CD_NM_KO ASC");
		request.setAttribute("cctvAuthList", codeCmcdService.grpList(args));

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */
	    
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		// grpLevCctvAuthService.grpAuthlist(args);

		return "wrks/sstm/grp/cctvAuthType";
	}

	@RequestMapping(value="/wrks/sstm/grp/cctvAuthType/list/grpList.json")
	@ResponseBody
	public HashMap<String, Object> grpList(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("result", grpLevCctvAuthService.grpList());
		return map;
	}

	@RequestMapping(value="/wrks/sstm/grp/cctvAuthType/list/grpAuthList.json")
	@ResponseBody
	public HashMap<String, Object> fn_grpAuthList(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		HashMap<String, String> args = new HashMap<String, String>();
		args.put("grpId", request.getParameter("grpId"));
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("result", grpLevCctvAuthService.grpAuthlist(args));
		return map;
	}

	@RequestMapping(value="/wrks/sstm/grp/cctvAuthType/list.json")
	@ResponseBody
	public Map<String, Object> list(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		// 조회조건
		String grpId = (String) request.getParameter("grpId");
		String grpNm = (String) request.getParameter("grpNm");
		// pageing 조건
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");
		String pageNo = (String) request.getParameter("page");
		String rowsPerPage = (String) request.getParameter("rows");

		HashMap<String, String> args = new HashMap<String, String>();
		args.put("grpId", grpId);
		args.put("grpNm", grpNm);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);

		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, String>> resultRows = grpLevCctvAuthService.list(args);

		map.put("page", pageNo);
		map.put("rows", resultRows);

		return map;
	}

	@RequestMapping(value="/wrks/sstm/grp/cctvAuthType/list/insert.json")
	@ResponseBody
	public Map<String, Object> insert(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String grpId = (String) request.getParameter("grpId");
		String authLvl = (String) request.getParameter("authLvl");
		String ptzCntrTy = (String) request.getParameter("ptzCntrTy");

		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("grpId", grpId);
		args.put("authLvl", authLvl);
		args.put("ptzCntrTy", ptzCntrTy);
		args.put("userId", lgnVO.getUserId());

		HashMap<String, Object> mapRet = new HashMap<String, Object>();
		try {
			grpLevCctvAuthService.insert(args);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.1");
		}
		catch (DataIntegrityViolationException e) {
			logger.debug(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 코드입니다.");
			}
			else {
				logger.debug(e.getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error("insert Exception : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	@RequestMapping(value="/wrks/sstm/grp/cctvAuthType/list/update.json")
	@ResponseBody
	public Map<String, Object> update(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String grpId = (String) request.getParameter("grpId");
		String authLvl = (String) request.getParameter("authLvl");
		String ptzCntrTy = (String) request.getParameter("ptzCntrTy");
		String oldPtzCntrTy = (String) request.getParameter("oldPtzCntrTy");

		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("grpId", grpId);
		args.put("authLvl", authLvl);
		args.put("ptzCntrTy", ptzCntrTy);
		args.put("oldPtzCntrTy", oldPtzCntrTy);
		args.put("userId", lgnVO.getUserId());

		HashMap<String, Object> mapRet = new HashMap<String, Object>();

		try {
			grpLevCctvAuthService.update(args);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.1");
		}
		catch (DataIntegrityViolationException e) {
			logger.debug(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 코드입니다.");
			}
			else {
				logger.debug(e.getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error("Update Exception : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}

		return mapRet;
	}

	@RequestMapping(value="/wrks/sstm/grp/cctvAuthType/list/delete.json")
	@ResponseBody
	public Map<String, Object> delete(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String grpId = (String) request.getParameter("grpId");
		String authLvl = (String) request.getParameter("authLvl");
		String ptzCntrTy = (String) request.getParameter("ptzCntrTy");

		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("grpId", grpId);
		args.put("authLvl", authLvl);
		args.put("ptzCntrTy", ptzCntrTy);

		HashMap<String, Object> mapRet = new HashMap<String, Object>();
		try {
			grpLevCctvAuthService.delete(args);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.1");
		}
		catch (DataIntegrityViolationException e) {
			logger.debug(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 코드입니다.");
			}
			else {
				logger.debug(e.getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error("delete Exception : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}
}