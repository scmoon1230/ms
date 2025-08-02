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
import kr.co.ucp.egov.com.utl.fcc.service.EgovDateUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.env.service.AcctcodeService;
import kr.co.ucp.env.service.AcctgbService;
import kr.co.ucp.env.service.PositionService;
import kr.co.ucp.env.service.MoneyService;

@Controller
public class MoneyController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="acctcodeService")
	AcctcodeService acctcodeService;

	@Resource(name="moneyService")
	MoneyService moneyService;

	@Resource(name="positionService")
	PositionService positionService;

	@Resource(name="acctgbService")
	AcctgbService acctgbService;

	// 리스트
	@RequestMapping(value="/env/money.do")
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

		return "env/money";
	}

	// 조건검색
	@RequestMapping(value="/env/money/list.json")
	@ResponseBody
	public Map<String, Object> selectMoney(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYy"   , (String) request.getParameter("stanYy"));
		args.put("acctGb"   , (String) request.getParameter("acctGb"));
		args.put("moneyName", (String) request.getParameter("moneyName"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = moneyService.selectMoney(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 등록
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/money/insert.json")
	@ResponseBody
	public Map<String, Object> insertMoney(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("moneyCode", EgovStringUtil.nullConvert(request.getParameter("moneyCode")));
		map.put("moneyName", EgovStringUtil.nullConvert(request.getParameter("moneyName")));
		map.put("acctCode", EgovStringUtil.nullConvert(request.getParameter("acctCode")));
		map.put("acctGb", EgovStringUtil.nullConvert(request.getParameter("acctGb")));


		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int insertResult;
		try {
			insertResult = moneyService.insertMoney(map);
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

	// 수정
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/money/update.json")
	@ResponseBody
	public Map<String, Object> updateMoney(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("moneyCode", EgovStringUtil.nullConvert(request.getParameter("moneyCode")));
		map.put("moneyName", EgovStringUtil.nullConvert(request.getParameter("moneyName")));
		map.put("acctCode", EgovStringUtil.nullConvert(request.getParameter("acctCode")));
		map.put("acctGb", EgovStringUtil.nullConvert(request.getParameter("acctGb")));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int updateResult;
		try {
			updateResult = moneyService.updateMoney(map);
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

	// 삭제
	@RequestMapping(value="/env/money/delete.json")
	@ResponseBody
	public Map<String, Object> deleteMoney(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		
		map.put("moneyCode", EgovStringUtil.nullConvert(request.getParameter("moneyCode")));

		int result = moneyService.deleteMoney(map);

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

	// 다중삭제
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/money/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMoneyMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		String[] moneyCode = request.getParameterValues("moneyCode");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < moneyCode.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("moneyCode", moneyCode[i]);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = moneyService.deleteMoneyMulti(list);
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

}
