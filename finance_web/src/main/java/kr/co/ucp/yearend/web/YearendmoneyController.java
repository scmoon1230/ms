package kr.co.ucp.yearend.web;

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
import kr.co.ucp.yearend.service.YearendmoneyService;

@Controller
public class YearendmoneyController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="yearendmoneyService")
	YearendmoneyService yearendmoneyService;

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
	@RequestMapping(value="/yearend/money.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 날짜조회
		String toDate = EgovDateUtil.getToday();
		request.setAttribute("startDate", toDate.substring(0,4)+"-01-01");
		request.setAttribute("endDate", toDate.substring(0,4)+"-12-31");
		request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
		
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"	 , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"	   , "ASC");
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "yearend/yearendmoney";
	}

	// 조건검색
	@RequestMapping(value="/yearend/money/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

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
		
		args.put("pageNo"	 , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"	   , (String) request.getParameter("sidx"));
		args.put("sord"	   , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = yearendmoneyService.selectMoney(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

}
