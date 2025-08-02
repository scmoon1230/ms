package kr.co.ucp.report.web;

import java.net.URLDecoder;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.CmmService;
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
import kr.co.ucp.report.service.RprtmemsortService;

@Controller
public class RprtmemsortController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="cmmService")
	CmmService cmmService;
	
	@Resource(name="rprtmemsortService")
	RprtmemsortService rprtmemsortService;

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
	@RequestMapping(value="/report/memsort.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		
		String type = request.getParameter("type").toString();
		request.setAttribute("type", type);
		
		// 날짜조회
		String toDate = EgovDateUtil.getToday();
		if ( "4".equalsIgnoreCase(type)) {
		//	request.setAttribute("startDate", EgovDateUtil.formatDate(toDate, "-"));
		//	request.setAttribute("endDate", EgovDateUtil.formatDate(toDate, "-"));
		} else {
		//	request.setAttribute("startDate", toDate.substring(0,4)+"-01-01");
		//	request.setAttribute("endDate", toDate.substring(0,4)+"-12-31");
		}
		request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
		
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"       , "ASC");

		args.put("stanYmd"    , EgovDateUtil.getToday());
		args.put("sidx"       , "MONEY_CODE");
		request.setAttribute("moneyList", moneyService.selectMoney(args));		// 헌금

		args.put("sidx"       , "WORSHIP_CODE");
		request.setAttribute("worshipList", worshipService.selectWorship(args));		// 예배
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "report/rprtmemsort";
	}

	// 조건검색
	@RequestMapping(value="/report/memsort/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"     , (String) request.getParameter("stanYmd"));
		args.put("moneyCode"   , (String) request.getParameter("moneyCode"));
		args.put("worshipCode" , (String) request.getParameter("worshipCode"));
		args.put("idExist"     , (String) request.getParameter("idExist"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = rprtmemsortService.selectRprtMemsort(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 합계
	@SuppressWarnings("unused")
	@RequestMapping(value="/report/memsort/getSum.json")
	@ResponseBody
	public Map<String, Object> getSum(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"     , (String) request.getParameter("stanYmd"));
		args.put("moneyCode"   , (String) request.getParameter("moneyCode"));
		args.put("worshipCode" , (String) request.getParameter("worshipCode"));
		args.put("idExist", (String) request.getParameter("idExist"));
		
		Map<String, Object> mapRet = new HashMap<String, Object>();

		try {
			String totalAmnt = rprtmemsortService.selectRprtMemsortTotalAmnt(args);
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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// 리스트
	@RequestMapping(value="/report/memsorthigh.do")
	public String viewhigh(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		
		// 날짜조회
		String toDate = EgovDateUtil.getToday();
		request.setAttribute("startDate", EgovDateUtil.formatDate(toDate, "-"));
		request.setAttribute("endDate", EgovDateUtil.formatDate(toDate, "-"));
		request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
		
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"       , "ASC");

		args.put("stanYmd"    , EgovDateUtil.getToday());
		args.put("sidx"       , "MONEY_CODE");
		request.setAttribute("moneyList", moneyService.selectMoney(args));		// 헌금

		args.put("sidx"       , "WORSHIP_CODE");
		request.setAttribute("worshipList", worshipService.selectWorship(args));		// 예배
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "report/rprtmemsorthigh";
	}

	// 인쇄
	@RequestMapping(value="/report/memsorthighPrint.do")
	public String viewhighPrint(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		
		// 날짜조회
		String toDate = EgovDateUtil.getToday();
		request.setAttribute("startDate", EgovDateUtil.formatDate(toDate, "-"));
		request.setAttribute("endDate", EgovDateUtil.formatDate(toDate, "-"));
		request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
		
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"       , "ASC");

		args.put("stanYmd"    , EgovDateUtil.getToday());
		args.put("sidx"       , "MONEY_CODE");
		request.setAttribute("moneyList", moneyService.selectMoney(args));		// 헌금

		args.put("sidx"       , "WORSHIP_CODE");
		request.setAttribute("worshipList", worshipService.selectWorship(args));		// 예배
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "report/rprtmemsorthighPrint";
	}

	// 조건검색
	@RequestMapping(value="/report/memsorthigh/list.json")
	@ResponseBody
	public Map<String, Object> listhigh(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		//args.put("stanYmd"     , (String) request.getParameter("stanYmd"));
		args.put("startDate"   , EgovStringUtil.nullConvert(request.getParameter("startDate")));
		args.put("endDate"     , EgovStringUtil.nullConvert(request.getParameter("endDate")));
		args.put("moneyCode"   , (String) request.getParameter("moneyCode"));
		args.put("worshipCode" , (String) request.getParameter("worshipCode"));
		args.put("idExist"     , (String) request.getParameter("idExist"));
		args.put("moneyAmt"    , (String) request.getParameter("moneyAmt"));
		
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = rprtmemsortService.selectRprtMemsorthigh(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 엑셀
	@RequestMapping(value="/report/memsorthigh/excel.do")
	public void excel(ModelMap model, @RequestParam Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String paraStartDate = EgovStringUtil.nullConvert(map.get("paraStartDate"));
        String paraEndDate = EgovStringUtil.nullConvert(map.get("paraEndDate"));

		Map<String, String> args = new HashMap<String, String>();
		//args.put("stanYmd"     , EgovStringUtil.nullConvert(map.get("paraMoneyAmt")));
		args.put("startDate"   , paraStartDate);
		args.put("endDate"	   , paraEndDate);
		args.put("moneyCode"   , EgovStringUtil.nullConvert(map.get("paraMoneyCode")));
		args.put("worshipCode" , EgovStringUtil.nullConvert(map.get("paraWorshipCode")));
		args.put("idExist"     , EgovStringUtil.nullConvert(map.get("paraIdExist")));
		args.put("moneyAmt"	   , EgovStringUtil.nullConvert(map.get("paraMoneyAmt")));

		args.put("pageNo"	  , "1");
		args.put("rowsPerPage", "9999999");
		args.put("sidx"	, EgovStringUtil.nullConvert(map.get("sidx")));
		args.put("sord"	, EgovStringUtil.nullConvert(map.get("sord")));

		logger.debug(args.toString());

		List<Map<String, String>> dataList = rprtmemsortService.selectRprtMemsorthigh(args);
		
		model.put("title", "");	//model.put("title", "고액헌금자");
		model.put("fileName", "고액헌금자_"+paraStartDate+"~"+paraEndDate) ;
		model.put("titleKey", EgovStringUtil.nullConvert(map.get("titleKey")));
		model.put("titleHeader", URLDecoder.decode(EgovStringUtil.nullConvert(map.get("titleHeader")), "utf-8"));
		model.put("dataList", dataList);

        cmmService.buildExcelDocument(model, request, response);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// 리스트
	@RequestMapping(value="/report/moneyheader.do")
	public String moneyheader(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		
		// 날짜조회
		String toDate = EgovDateUtil.getToday();
		request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
		
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"       , "ASC");

		args.put("stanYmd"    , EgovDateUtil.getToday());
		args.put("sidx"       , "MONEY_CODE");
		request.setAttribute("moneyList", moneyService.selectMoney(args));		// 헌금

		args.put("sidx"       , "WORSHIP_CODE");
		request.setAttribute("worshipList", worshipService.selectWorship(args));		// 예배
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "report/rprtmoneyheader";
	}

	// 리스트
	@RequestMapping(value="/report/moneyheaderPrint.do")
	public String moneyheaderPrint(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		
		// 날짜조회
		String toDate = EgovDateUtil.getToday();
		request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
		
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"       , "ASC");

		args.put("stanYmd"    , EgovDateUtil.getToday());
		args.put("sidx"       , "MONEY_CODE");
		request.setAttribute("moneyList", moneyService.selectMoney(args));		// 헌금

		args.put("sidx"       , "WORSHIP_CODE");
		request.setAttribute("worshipList", worshipService.selectWorship(args));		// 예배
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "report/rprtmoneyheaderPrint";
	}

	// 조건검색
	@RequestMapping(value="/report/moneyheader/list.json")
	@ResponseBody
	public Map<String, Object> moneyheaderlist(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"     , (String) request.getParameter("stanYmd"));
		args.put("moneyCode"   , (String) request.getParameter("moneyCode"));
		args.put("worshipCode" , (String) request.getParameter("worshipCode"));
		args.put("idExist"     , (String) request.getParameter("idExist"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = rprtmemsortService.selectRprtMemsort(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 합계
	@SuppressWarnings("unused")
	@RequestMapping(value="/report/moneyheader/getSum.json")
	@ResponseBody
	public Map<String, Object> moneyheadergetSum(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"     , (String) request.getParameter("stanYmd"));
		args.put("moneyCode"   , (String) request.getParameter("moneyCode"));
		args.put("worshipCode" , (String) request.getParameter("worshipCode"));
		args.put("idExist", (String) request.getParameter("idExist"));
		
		Map<String, Object> mapRet = new HashMap<String, Object>();

		try {
			String totalAmnt = rprtmemsortService.selectRprtMemsortTotalAmnt(args);
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

	
	
	
	
	
	
	
	
	
	
	
	
	
}
