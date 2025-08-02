package kr.co.ucp.wrks.sstm.code.web;

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
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.code.service.CodeSggcdService;

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

@Controller
public class CodeSggcdController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	/** TRACE */
	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Resource(name="codeSggcdService")
	CodeSggcdService codeSggcdSerivce;

	@Resource(name="codeCmcdService")
	CodeCmcdService codeCmcdService;

	// 시군구코드 리스트
	@RequestMapping(value="/wrks/sstm/code/sggcd.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

	//	Map<String, Object> args = new HashMap<String, Object>();

		/* 시군구선택 설정 */
	//	request.setAttribute("sido", codeSggcdSerivce.sidoNm_SigunguList(args));
	//	request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/* 사용유형 */
	//	request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "wrks/sstm/code/sggcd";
	}

	// 시군구코드 조건검색
	@RequestMapping(value="/wrks/sstm/code/sggcd/list.json")
	@ResponseBody
	public Map<String, Object> selectWorship(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("worshipName", (String) request.getParameter("worshipName"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));

		List<Map<String, String>> list = codeSggcdSerivce.selectWorship(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 시군구코드 등록
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/sggcd/insert.json")
	@ResponseBody
	public Map<String, Object> insertWorship(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("worshipCode", EgovStringUtil.nullConvert(request.getParameter("worshipCode")));
		map.put("worshipName", EgovStringUtil.nullConvert(request.getParameter("worshipName")));
		map.put("worshipDay", EgovStringUtil.nullConvert(request.getParameter("worshipDay")));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int insertResult;
		try {
			insertResult = codeSggcdSerivce.insertWorship(map);
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
				logger.error(e.getRootCause().getMessage());
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

	// 시군구코드 수정
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/sggcd/update.json")
	@ResponseBody
	public Map<String, Object> updateWorship(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("worshipCode", EgovStringUtil.nullConvert(request.getParameter("worshipCode")));
		map.put("worshipName", EgovStringUtil.nullConvert(request.getParameter("worshipName")));
		map.put("worshipDay", EgovStringUtil.nullConvert(request.getParameter("worshipDay")));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int updateResult;
		try {
			updateResult = codeSggcdSerivce.updateWorship(map);
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
				logger.error(e.getRootCause().getMessage());
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

	// 시군구코드 삭제
	@RequestMapping(value="/wrks/sstm/code/sggcd/delete.json")
	@ResponseBody
	public Map<String, Object> deleteWorship(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		map.put("worshipCode", EgovStringUtil.nullConvert(request.getParameter("worshipCode")));

		int result = codeSggcdSerivce.deleteWorship(map);

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

	// 시군구코드 다중삭제
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/sggcd/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteWorshipMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		String[] sigunguCd = request.getParameterValues("worshipCode");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < sigunguCd.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("SIGUNGU_CD", sigunguCd[i]);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = codeSggcdSerivce.deleteWorshipMulti(list);
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
				logger.error(e.getRootCause().getMessage());
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

	// 시군구코드 시군구명 조건검색
//	@RequestMapping(value="/wrks/sstm/code/selectSggcd.json")
//	@ResponseBody
//	public Map<String, Object> sggList(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Map<String, Object> args = new HashMap<String, Object>();
//
//	//	String sidoNm = (String) request.getParameter("sidoNm");
//	//	args.put("sidoNm", sidoNm);
//
//		/* 시도를 다시 '전체'로 바꿨을 경우 */
//		//if (sidoNm == null || sidoNm == "") {
//	//	if(sidoNm == null || "".equals(sidoNm)) {
//	//		args.put("session", 1);
//	//		return args;
//	//	}
//
//		List<Map<String, String>> list = codeSggcdSerivce.sigunguCdList(args);
//		
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("session", 1);
//		map.put("sigungu", list);
//
//		return map;
//	}

}
