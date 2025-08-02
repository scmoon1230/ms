package kr.co.ucp.report.web;

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
import kr.co.ucp.env.service.AcctgbService;
import kr.co.ucp.env.service.DeptService;
import kr.co.ucp.env.service.MoneyService;
import kr.co.ucp.env.service.PositionService;
import kr.co.ucp.env.service.RegionService;
import kr.co.ucp.env.service.WorshipService;
import kr.co.ucp.report.service.RprtmoneyService;

@Controller
public class RprtmoneyController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="rprtmoneyService")
	RprtmoneyService rprtmoneyService;

	@Resource(name="acctgbService")
	AcctgbService acctgbService;

	@Resource(name="moneyService")
	MoneyService moneyService;

	@Resource(name="worshipService")
	WorshipService worshipService;

	@Resource(name="positionService")
	PositionService positionService;

	@Resource(name="deptService")
	DeptService deptService;

	@Resource(name="regionService")
	RegionService regionService;

	// 리스트
	@RequestMapping(value="/report/money.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		
		String type = request.getParameter("type").toString();
		request.setAttribute("type", type);
		
		// 날짜조회
		String toDate = EgovDateUtil.getToday();
	//	if ( "3".equalsIgnoreCase(type)) {
	//		request.setAttribute("startDate", EgovDateUtil.formatDate(toDate, "-"));
	//		request.setAttribute("endDate", EgovDateUtil.formatDate(toDate, "-"));
	//	} else {
			request.setAttribute("startDate", toDate.substring(0,4)+"-01-01");
			request.setAttribute("endDate", toDate.substring(0,4)+"-12-31");
	//	}
		request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
		
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"	 , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"	   , "ASC");

		args.put("sidx"	   , "ACCT_GB");
		request.setAttribute("AcctgbList", acctgbService.selectAcctgb(args));		// 재정

		args.put("stanYmd"    , EgovDateUtil.getToday());
		args.put("sidx"	   , "MONEY_CODE");
		request.setAttribute("moneyList", moneyService.selectMoney(args));		// 헌금

		args.put("sidx"	   , "WORSHIP_CODE");
		request.setAttribute("worshipList", worshipService.selectWorship(args));		// 예배
		
		args.put("sidx"	   , "POSITION_CODE");
		request.setAttribute("positionList", positionService.selectPosition(args));		// 직분

		args.put("sidx"	   , "DEPT_CODE");
		request.setAttribute("deptList", deptService.selectDept(args));		// 교구

		args.put("sidx"	   , "REGION_CODE");
		request.setAttribute("regionList", regionService.selectRegion(args));		// 구역
		
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "report/rprtmoney";
	}

	// 조건검색
	@RequestMapping(value="/report/money/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("startDate"   , EgovStringUtil.nullConvert(request.getParameter("startDate")));
		args.put("endDate"     , EgovStringUtil.nullConvert(request.getParameter("endDate")));
		args.put("acctGb"      , (String) request.getParameter("acctGb"));
		args.put("moneyCode"   , (String) request.getParameter("moneyCode"));
		args.put("worshipCode" , (String) request.getParameter("worshipCode"));
		args.put("positionCode", (String) request.getParameter("positionCode"));
		args.put("deptCode"    , (String) request.getParameter("deptCode"));
		args.put("regionCode"  , (String) request.getParameter("regionCode"));
		args.put("memberNo"    , (String) request.getParameter("memberNo"));
		args.put("memberName"  , (String) request.getParameter("memberName"));
		args.put("memberNoFlag", (String) request.getParameter("memberNoFlag"));
	//	args.put("moneyAmt"    , (String) request.getParameter("moneyAmt"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = rprtmoneyService.selectRprtMoney(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 합계
	@SuppressWarnings("unused")
	@RequestMapping(value="/report/money/getSum.json")
	@ResponseBody
	public Map<String, Object> getSum(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> args = new HashMap<String, String>();
		args.put("startDate"   , EgovStringUtil.nullConvert(request.getParameter("startDate")));
		args.put("endDate"	 , EgovStringUtil.nullConvert(request.getParameter("endDate")));
		args.put("acctGb"	  , (String) request.getParameter("acctGb"));
		args.put("moneyCode"   , (String) request.getParameter("moneyCode"));
		args.put("worshipCode" , (String) request.getParameter("worshipCode"));
		args.put("positionCode", (String) request.getParameter("positionCode"));
		args.put("deptCode"	, (String) request.getParameter("deptCode"));
		args.put("regionCode"  , (String) request.getParameter("regionCode"));
		args.put("memberNo"	, (String) request.getParameter("memberNo"));
		args.put("memberName"  , (String) request.getParameter("memberName"));
		args.put("memberNoFlag", (String) request.getParameter("memberNoFlag"));
	//	args.put("moneyAmt"	, (String) request.getParameter("moneyAmt"));
		
		Map<String, Object> mapRet = new HashMap<String, Object>();

		try {
			String totalAmnt = rprtmoneyService.selectRprtMoneyTotalAmnt(args);
			mapRet.put("session", 1);
			mapRet.put("totalAmnt", totalAmnt);
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	// 등록
	@SuppressWarnings("unused")
	@RequestMapping(value="/report/money/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("memberId", EgovStringUtil.nullConvert(request.getParameter("memberId")));
		map.put("memberNo", EgovStringUtil.nullConvert(request.getParameter("memberNo")));
		map.put("memberName", EgovStringUtil.nullConvert(request.getParameter("memberName")));
		map.put("sexType", EgovStringUtil.nullConvert(request.getParameter("sexType")));
		map.put("telNo", EgovStringUtil.nullConvert(request.getParameter("telNo")));
		map.put("hphoneNo", EgovStringUtil.nullConvert(request.getParameter("hphoneNo")));
		map.put("positionCode", EgovStringUtil.nullConvert(request.getParameter("positionCode")));
		map.put("deptCode", EgovStringUtil.nullConvert(request.getParameter("deptCode")));
		map.put("regionCode", EgovStringUtil.nullConvert(request.getParameter("regionCode")));
		map.put("addr", EgovStringUtil.nullConvert(request.getParameter("addr")));
		map.put("familyRemark", EgovStringUtil.nullConvert(request.getParameter("familyRemark")));


		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int insertResult;
		try {
			insertResult = rprtmoneyService.insertMoney(map);
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
	@RequestMapping(value="/report/money/update.json")
	@ResponseBody
	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("memberId", EgovStringUtil.nullConvert(request.getParameter("memberId")));
		map.put("memberNo", EgovStringUtil.nullConvert(request.getParameter("memberNo")));
		map.put("memberName", EgovStringUtil.nullConvert(request.getParameter("memberName")));
		map.put("sexType", EgovStringUtil.nullConvert(request.getParameter("sexType")));
		map.put("telNo", EgovStringUtil.nullConvert(request.getParameter("telNo")));
		map.put("hphoneNo", EgovStringUtil.nullConvert(request.getParameter("hphoneNo")));
		map.put("positionCode", EgovStringUtil.nullConvert(request.getParameter("positionCode")));
		map.put("deptCode", EgovStringUtil.nullConvert(request.getParameter("deptCode")));
		map.put("regionCode", EgovStringUtil.nullConvert(request.getParameter("regionCode")));
		map.put("addr", EgovStringUtil.nullConvert(request.getParameter("addr")));
		map.put("familyRemark", EgovStringUtil.nullConvert(request.getParameter("familyRemark")));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int updateResult;
		try {
			updateResult = rprtmoneyService.updateMoney(map);
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
	@RequestMapping(value="/report/money/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		
		map.put("memberId", EgovStringUtil.nullConvert(request.getParameter("memberId")));
		map.put("memberNo", EgovStringUtil.nullConvert(request.getParameter("memberNo")));

		int result = rprtmoneyService.deleteMoney(map);

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
	@RequestMapping(value="/report/money/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		String[] memberId = request.getParameterValues("memberId");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < memberId.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("memberId", memberId[i]);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = rprtmoneyService.deleteMoneyMulti(list);
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
