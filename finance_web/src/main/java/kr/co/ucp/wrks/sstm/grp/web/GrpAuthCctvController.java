package kr.co.ucp.wrks.sstm.grp.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.mntr.cmm.service.ConfigureService;
import kr.co.ucp.mntr.cmm.service.ConfigureVO;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.grp.service.GrpAuthCctvService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * 그룹별로 이벤트, 사용자권한레벨, 모바일기기권한레벨을 관리
 *
 * @author 대전도안 김정원
 * @version 1.00 2014-01-25
 * @since JDK 1.7.0_45(x64)
 * @revision
 */

/**
 * ----------------------------------------------------------
 *
 * @Class Name : GrpUserAccController
 * @Description : 그룹별이벤트
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
@SessionAttributes("configure")
public class GrpAuthCctvController {
	static Logger logger = LoggerFactory.getLogger(GrpAuthCctvController.class);

	@Resource(name="grpAuthCctvService")
	private GrpAuthCctvService grpAuthCctvService;

	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
//	@Resource(name="propertiesService")
//	protected EgovPropertyService propertiesService;

	/** TRACE */
	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	@Resource(name="configureService")
	private ConfigureService configureService;

	/*
	 * 그룹별이벤트 리스트
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth_cctv.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();

//		args.put("cdGrpId", "USE_TY");
//		args.put("cdTy", "C");
//		args.put("orderBy", "ORDER BY CD_ID ASC");
//		request.setAttribute("useTyCd", codeCmcdService.grpList(args));

//		args.put("cdGrpId", "SEND_TY");
//		args.put("cdTy", "C");
//		args.put("orderBy", "ORDER BY CD_ID ASC");
//		request.setAttribute("sendTyCd", codeCmcdService.grpList(args));

		/* 지구코드조회 */
		//request.setAttribute("listCmDstrtCdMng", grpAuthCctvService.getCM_DSTRT_CD_MNG(null));

		return "wrks/sstm/grp/auth_cctv";
	}

	/*
	 * 그룹리스트
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth_cctv/list_group.json")
	@ResponseBody
	public Map<String, Object> list_group(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		//String dstrtCd = (String) request.getParameter("dstrtCd");
		String grpNm = (String) request.getParameter("grpNm");
		String pageNo = (String) request.getParameter("page");
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();

		//args.put("DSTRT_CD", dstrtCd);
		args.put("GRP_NM", grpNm);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = grpAuthCctvService.getGroupList(args);

		map.put("page", pageNo);
		map.put("rows", resultRows);

		return map;
	}

	/*
	 * 그룹별 사용자
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth_cctv/list_user.json")
	@ResponseBody
	public Map<String, Object> list_user_acc(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		String grpId = (String) request.getParameter("grpId");
		String authLvl = (String) request.getParameter("authLvl");
		String pageNo = (String) request.getParameter("page");
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();

		args.put("GRP_ID", grpId);
		args.put("AUTH_LVL", authLvl);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (!"".equalsIgnoreCase(authLvl)) {
			List<Map<String, String>> resultRows = grpAuthCctvService.getUserList(args);
			map.put("page", pageNo);
			map.put("rows", resultRows);
		}

		return map;
	}

	/*
	 * 그룹별 사용유형
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth_cctv/list_cctv_used.json")
	@ResponseBody
	public Map<String, Object> list_cctv_used(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		String grpId = (String) request.getParameter("grpId");
		String authLvl = (String) request.getParameter("authLvl");
		String pageNo = (String) request.getParameter("page");
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();

		args.put("GRP_ID", grpId);
		args.put("AUTH_LVL", authLvl);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		//logger.debug("== list_cctv_used : {}", args);

		Map<String, Object> map = new HashMap<String, Object>();
		
		if (!"".equalsIgnoreCase(authLvl)) {
			List<Map<String, String>> resultRows = grpAuthCctvService.getCctvUsedList(args);
			map.put("page", pageNo);
			map.put("rows", resultRows);
		}

		return map;
	}

	/*
	 * 그룹별 사용유형
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth_cctv/list_cctv_auth.json")
	@ResponseBody
	public Map<String, Object> list_cctv_auth(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		String grpId = (String) request.getParameter("grpId");
		String authLvl = (String) request.getParameter("authLvl");
		String pageNo = (String) request.getParameter("page");
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();

		args.put("GRP_ID", grpId);
		args.put("AUTH_LVL", authLvl);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		Map<String, Object> map = new HashMap<String, Object>();
		
		//if (!"".equalsIgnoreCase(authLvl)) {
			List<Map<String, String>> resultRows = grpAuthCctvService.getCctvAuthList(args);
			map.put("page", pageNo);
			map.put("rows", resultRows);
		//}
		
		return map;
	}

	/*
	 * 사용유형 팝업
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth_cctv/list_cctv_used_popup.json")
	@ResponseBody
	public Map<String, Object> list_fclt_use(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		String grpId = (String) request.getParameter("grpId");
		String authLvl = (String) request.getParameter("authLvl");
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();

		args.put("GRP_ID", grpId);
		args.put("AUTH_LVL", authLvl);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		//logger.debug("== list_fclt_use : {}", args);

		Map<String, Object> map = new HashMap<String, Object>();

		if (!"".equalsIgnoreCase(authLvl)) {
			List<Map<String, String>> resultRows = grpAuthCctvService.getFcltUseList(args);
			map.put("rows", resultRows);
		}
				
		return map;
	}

	/*
	 * 그룹별 사용유형 입력
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/grp/auth_cctv/insert_cctv_used.json")
	@ResponseBody
	public Map<String, Object> insert_cctv_used(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		String sesUserId = lgnVO.getUserId();
		String[] grpIds = request.getParameterValues("grpId");
		String[] authLvl = request.getParameterValues("authLvl");
		String[] fcltUsedTyCd = request.getParameterValues("fcltUsedTyCd");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < grpIds.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();

			mapId.put("GRP_ID", grpIds[i]);
			mapId.put("AUTH_LVL", authLvl[i]);
			mapId.put("FCLT_USED_TY_CD", fcltUsedTyCd[i]);
			mapId.put("UPD_USER_ID", sesUserId);
			mapId.put("RGS_USER_ID", sesUserId);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();

		int ret;
		try {
			ret = grpAuthCctvService.insert_cctv_used(list);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
			
			//ConfigureVO configure = new ConfigureVO(lgnVO.getUserId());
			//configure = configureService.refresh(configure, lgnVO, request);
			EgovMap configure = configureService.getUmConfigInfo();
			model.addAttribute("configure", configure);
		}
		catch (DataIntegrityViolationException e) {
			logger.error("Exception insert_cctv_used : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "키 중복에러입니다.");
			}
			else {
				logger.error("Exception insert_cctv_used : {}", e.getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error("Exception insert_cctv_used : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	/*
	 * 구룹별 사용유형 삭제
	 */
	@RequestMapping(value="/wrks/sstm/grp/auth_cctv/delete_cctv_used.json")
	@ResponseBody
	public Map<String, Object> delete_cctv_used(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		String[] grpIds = request.getParameterValues("grpId");
		String[] authLvl = request.getParameterValues("authLvl");
		String[] fcltUsedTyCd = request.getParameterValues("fcltUsedTyCd");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < grpIds.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();

			mapId.put("GRP_ID", grpIds[i]);
			mapId.put("AUTH_LVL", authLvl[i]);
			mapId.put("FCLT_USED_TY_CD", fcltUsedTyCd[i]);
			mapId.put("UPD_USER_ID", sesUserId);
			mapId.put("RGS_USER_ID", sesUserId);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();

		try {
			grpAuthCctvService.delete_cctv_used(list);
			
			//ConfigureVO configure = new ConfigureVO(lgnVO.getUserId());
			//configure = configureService.refresh(configure, lgnVO, request);
			EgovMap configure = configureService.getUmConfigInfo();
			model.addAttribute("configure", configure);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			logger.error("delete_cctv_used DataIntegrityViolationException : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "키 중복에러입니다.");
			}
			else {
				logger.error("delete_cctv_used UncategorizedSQLException : {}", e.getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error("delete_cctv_used Exception  : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "알수없는 에러입니다."); // TODO: SQL오류 메세지 처리
		}
		return mapRet;
	}

	/*
	 * 그룹별 권한 업데이트
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/grp/auth_cctv/insert_cctv_auth.json")
	@ResponseBody
	public Map<String, Object> insert_cctv_auth(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		String sesUserId = lgnVO.getUserId();
		String[] grpIds = request.getParameterValues("grpId");
		String[] authLvl = request.getParameterValues("authLvl");
		String[] ptzCntrTy = request.getParameterValues("ptzCntrTy");
		String[] cctvAccessYn = request.getParameterValues("cctvAccessYn");
		String[] cctvSearchYn = request.getParameterValues("cctvSearchYn");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < grpIds.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();

			mapId.put("GRP_ID", grpIds[i]);
			mapId.put("AUTH_LVL", authLvl[i]);
			mapId.put("PTZ_CNTR_TY", ptzCntrTy[i]);
			mapId.put("CCTV_ACCESS_YN", cctvAccessYn[i]);
			mapId.put("CCTV_SEARCH_YN", cctvSearchYn[i]);
			mapId.put("UPD_USER_ID", sesUserId);
			mapId.put("RGS_USER_ID", sesUserId);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();

		int ret;
		try {
			ret = grpAuthCctvService.insert_cctv_auth(list);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");

			//ConfigureVO configure = new ConfigureVO(lgnVO.getUserId());
			//configure = configureService.refresh(configure, lgnVO, request);
			EgovMap configure = configureService.getUmConfigInfo();
			model.addAttribute("configure", configure);
		}
		catch (DataIntegrityViolationException e) {
			logger.error("Exception insert_cctv_auth : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "키 중복에러입니다.");
			}
			else {
				logger.error("Exception insert_cctv_auth : {}", e.getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error("Exception insert_cctv_auth : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}
}
