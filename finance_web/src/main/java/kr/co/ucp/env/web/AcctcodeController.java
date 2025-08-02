package kr.co.ucp.env.web;

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

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.env.service.AcctgbService;
import kr.co.ucp.env.service.PositionService;
import kr.co.ucp.env.service.AcctcodeService;

@Controller
public class AcctcodeController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="acctcodeService")
	AcctcodeService acctcodeService;

	@Resource(name="positionService")
	PositionService positionService;

	@Resource(name="acctgbService")
	AcctgbService acctgbService;

	// 계정과목 리스트
	@RequestMapping(value="/env/acctcode.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, String> args = new HashMap<String, String>();

		request.setAttribute("stanYyList", acctcodeService.stanYyList(args));
		
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sidx"       , "ACCT_GB");
		args.put("sord"       , "ASC");
		request.setAttribute("acctgbList", acctgbService.selectAcctgb(args));		// 재정
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "env/acctcode";
	}

	// 계정과목 조건검색
	@RequestMapping(value="/env/acctcode/list.json")
	@ResponseBody
	public Map<String, Object> selectAcctcode(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("stanYmd"   , (String) request.getParameter("stanYmd"));
		args.put("stanYy"    , (String) request.getParameter("stanYy"));
		args.put("acctGb"    , (String) request.getParameter("acctGb"));
		args.put("acctLevel" , (String) request.getParameter("acctLevel"));
		args.put("sumYn"     , (String) request.getParameter("sumYn"));
		args.put("useYn"     , (String) request.getParameter("useYn"));
		args.put("acctCode"  , (String) request.getParameter("acctCode"));
		args.put("acctName"  , (String) request.getParameter("acctName"));
		args.put("acctQuery" , (String) request.getParameter("acctQuery"));
		args.put("assetCode" , (String) request.getParameter("assetCode"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));

		String tmp = EgovStringUtil.nullConvert(request.getParameter("acctLevels"));
		if ( !"".equalsIgnoreCase(tmp) ) {
			args.put("acctLevels", (String[]) EgovStringUtil.nullConvert(request.getParameter("acctLevels")).split(","));
		}
		
		tmp = EgovStringUtil.nullConvert(request.getParameter("inoutGb"));
		if ( "I".equalsIgnoreCase(tmp)) {			// 입금
			args.put("inoutGb" , "지출");
		} else if ( "O".equalsIgnoreCase(tmp)) {	// 출금
			args.put("inoutGb" , "수입");
		}

		logger.debug(args.toString());

		List<Map<String, String>> list = acctcodeService.selectAcctcode(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 계정과목 등록
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/acctcode/insert.json")
	@ResponseBody
	public Map<String, Object> insertAcctcode(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("stanYy", EgovStringUtil.nullConvert(request.getParameter("stanYy")));
		map.put("acctGb", EgovStringUtil.nullConvert(request.getParameter("acctGb")));
		map.put("acctLevel", EgovStringUtil.nullConvert(request.getParameter("acctLevel")));
		map.put("acctUp", EgovStringUtil.nullConvert(request.getParameter("acctUp")));
		map.put("acctCode", EgovStringUtil.nullConvert(request.getParameter("acctCode")));
		map.put("acctName", EgovStringUtil.nullConvert(request.getParameter("acctName")));
		map.put("printName", EgovStringUtil.nullConvert(request.getParameter("printName")));
		map.put("sumYn", EgovStringUtil.nullConvert(request.getParameter("sumYn")));
		map.put("inoutGb", EgovStringUtil.nullConvert(request.getParameter("inoutGb")));
		map.put("inType", EgovStringUtil.nullConvert(request.getParameter("inType")));
		map.put("useYn", EgovStringUtil.nullConvert(request.getParameter("useYn")));
		map.put("userId", lgnVO.getUserId());
		if ( !"".equalsIgnoreCase(EgovStringUtil.nullConvert(request.getParameter("linkAcctCode")))) {
			map.put("linkAcctCode", EgovStringUtil.nullConvert(request.getParameter("linkAcctCode")));
			map.put("linkAcctGb", "A");
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int insertResult;
		try {
			insertResult = acctcodeService.insertAcctcode(map);
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

	// 계정과목 수정
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/acctcode/update.json")
	@ResponseBody
	public Map<String, Object> updateAcctcode(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("stanYy", EgovStringUtil.nullConvert(request.getParameter("stanYy")));
		map.put("acctGb", EgovStringUtil.nullConvert(request.getParameter("acctGb")));
		map.put("acctLevel", EgovStringUtil.nullConvert(request.getParameter("acctLevel")));
		map.put("acctUp", EgovStringUtil.nullConvert(request.getParameter("acctUp")));
		map.put("acctCode", EgovStringUtil.nullConvert(request.getParameter("acctCode")));
		map.put("acctName", EgovStringUtil.nullConvert(request.getParameter("acctName")));
		map.put("printName", EgovStringUtil.nullConvert(request.getParameter("printName")));
		map.put("sumYn", EgovStringUtil.nullConvert(request.getParameter("sumYn")));
		map.put("inoutGb", EgovStringUtil.nullConvert(request.getParameter("inoutGb")));
		map.put("inType", EgovStringUtil.nullConvert(request.getParameter("inType")));
		map.put("useYn", EgovStringUtil.nullConvert(request.getParameter("useYn")));
		map.put("userId", lgnVO.getUserId());
		if ( !"".equalsIgnoreCase(EgovStringUtil.nullConvert(request.getParameter("linkAcctCode")))) {
			map.put("linkAcctCode", EgovStringUtil.nullConvert(request.getParameter("linkAcctCode")));
			map.put("linkAcctGb", "A");
		} else {
			map.put("linkAcctCode", "");
			map.put("linkAcctGb", "");
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int updateResult;
		try {
			updateResult = acctcodeService.updateAcctcode(map);
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

	// 계정과목 삭제
	@RequestMapping(value="/env/acctcode/delete.json")
	@ResponseBody
	public Map<String, Object> deleteAcctcode(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> map = new HashMap<String, String>();

		map.put("stanYy", EgovStringUtil.nullConvert(request.getParameter("stanYy")));
		map.put("acctGb", EgovStringUtil.nullConvert(request.getParameter("acctGb")));
		map.put("acctCode", EgovStringUtil.nullConvert(request.getParameter("acctCode")));

		int result = acctcodeService.deleteAcctcode(map);

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

	// 계정과목 다중삭제
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/acctcode/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteAcctcodeMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		String[] acctCode = request.getParameterValues("acctCode");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < acctCode.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("acctCode", acctCode[i]);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = acctcodeService.deleteAcctcodeMulti(list);
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

	// 계정과목 중복 확인
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/acctcode/checkAcctCode.json")
	@ResponseBody
	public Map<String, Object> checkAcctCode(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception
	{
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("acctCode" , (String)request.getParameter("acctCode"));

		int ret = acctcodeService.selectAcctcodeCnt(map);
		
		Map<String, Object> map_ret = new HashMap<String, Object>();
		map_ret.put("session", 1);
		map_ret.put("msg", ret);

		return map_ret;
	}

	// 계정과목 복사생성
	@RequestMapping(value="/env/acctcode/makeNextFromPrev.json")
	@ResponseBody
	public Map<String, Object> makeNextFromPrev(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		String nextStanYy = EgovStringUtil.nullConvert(request.getParameter("nextStanYy"));
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("prevStanYy", EgovStringUtil.nullConvert(request.getParameter("prevStanYy")));
		args.put("nextStanYy", nextStanYy);
		args.put("userId", lgnVO.getUserId());
		args.put("pageNo"     , 1);
		args.put("rowsPerPage", 99999);
		args.put("sidx"       , "ACCT_CODE");
		args.put("sord"       , "ASC");
		
		int result = acctcodeService.makeNextFromPrev(args);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (result > 0) {
			mapRet.put("session", 1);
			mapRet.put("msg", "성공하였습니다.");
		}
		else {
			mapRet.put("session", 2);
			mapRet.put("msg", nextStanYy+"년도 계정과목이 이미 등록되어 있습니다.");
		}

		return mapRet;
	}

}
