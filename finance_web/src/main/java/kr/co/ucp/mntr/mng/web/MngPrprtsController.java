package kr.co.ucp.mntr.mng.web;

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
import kr.co.ucp.mntr.mng.service.MngPrprtsService;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MngPrprtsController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	@Resource(name="mngPrprtsService")
	private MngPrprtsService mngPrprtsService;

	/*
	 * 리스트
	 */
	@RequestMapping(value="/mntr/mngPrprts.do")
	public String view(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();

	    request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/* 사용유무 설정 */
	    
		request.setAttribute("prprtsIdList", mngPrprtsService.prprtsIdList(args));
		
		request.setAttribute("prprtsTyList", mngPrprtsService.prprtsTyList(args));

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "/mntr/mng/mngPrprts";
	}

	/*
	 * 조건검색
	 */
	@RequestMapping(value="/mntr/mng/prprts/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, String> args = new HashMap<String, String>();
		//args.put("userId", (String) request.getParameter("userId"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		args.put("PRPRTS_ID", (String) request.getParameter("prprtsId"));
		args.put("PRPRTS_TY", (String) request.getParameter("prprtsTy"));
		args.put("PRPRTS_KEY", (String) request.getParameter("prprtsKey"));
		args.put("PRPRTS_VAL", (String) request.getParameter("prprtsVal"));
		args.put("DSCRT", (String) request.getParameter("dscrt"));
		args.put("USE_TY_CD", (String) request.getParameter("useTyCd"));
		args.put("SORT_ORDR", (String) request.getParameter("sortOrdr"));

		List<Map<String, String>> list = mngPrprtsService.prprtsList(args);

		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	/*
	 * 입력
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/mntr/mng/prprts/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("PRPRTS_ID", (String) request.getParameter("prprtsId"));
		map.put("PRPRTS_TY", (String) request.getParameter("prprtsTy"));
		map.put("PRPRTS_KEY", (String) request.getParameter("prprtsKey"));
		map.put("PRPRTS_VAL", (String) request.getParameter("prprtsVal"));
		map.put("DEFAULT_VAL", (String) request.getParameter("defaultVal"));
		map.put("DSCRT", (String) request.getParameter("dscrt"));
		map.put("SORT_ORDR", (String) request.getParameter("sortOrdr"));
		map.put("USE_TY_CD", (String) request.getParameter("useTyCd"));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int insertResult;
		try {
			insertResult = mngPrprtsService.insert(map);
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
	 * 수정
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/mntr/mng/prprts/update.json")
	@ResponseBody
	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("PRPRTS_ID", (String) request.getParameter("prprtsId"));
		map.put("PRPRTS_TY", (String) request.getParameter("prprtsTy"));
		map.put("PRPRTS_KEY", (String) request.getParameter("prprtsKey"));
		map.put("PRPRTS_VAL", (String) request.getParameter("prprtsVal"));
		map.put("DEFAULT_VAL", (String) request.getParameter("defaultVal"));
		map.put("DSCRT", (String) request.getParameter("dscrt"));
		map.put("SORT_ORDR", (String) request.getParameter("sortOrdr"));
		map.put("USE_TY_CD", (String) request.getParameter("useTyCd"));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int updateResult;
		try {
			updateResult = mngPrprtsService.update(map);
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
	 * 삭제
	 */
	@RequestMapping(value="/mntr/mng/prprts/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, String> map = new HashMap<String, String>();

		map.put("sysCdBk", (String) request.getParameter("sysCdBk"));
		map.put("UPD_USER_ID", sesUserId);

		int result = mngPrprtsService.delete(map);

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
	 * 다중삭제
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/mntr/mng/prprts/deleteMulti.json")
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
			ret = mngPrprtsService.deleteMulti(list);
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
