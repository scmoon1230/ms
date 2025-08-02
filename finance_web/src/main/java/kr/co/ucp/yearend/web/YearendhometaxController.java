package kr.co.ucp.yearend.web;

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
import kr.co.ucp.yearend.service.YearendhometaxService;

@Controller
public class YearendhometaxController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="cmmService")
	CmmService cmmService;
	
	@Resource(name="yearendhometaxService")
	YearendhometaxService yearendhometaxService;
	
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
	@RequestMapping(value="/yearend/hometax.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, String> args = new HashMap<String, String>();

		request.setAttribute("stanYyList", yearendhometaxService.stanYyList(args));
		
		args.put("pageNo"	 , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"	   , "ASC");
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "yearend/yearendhometax";
	}

	// 조건검색
	@RequestMapping(value="/yearend/hometax/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYy"	, (String) request.getParameter("stanYy"));
		
		args.put("pageNo"	 , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"	   , (String) request.getParameter("sidx"));
		args.put("sord"	   , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = yearendhometaxService.selectMoney(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 엑셀
	@RequestMapping(value="/yearend/hometax/excel.do")
	public void excel(ModelMap model, @RequestParam Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String paraStanYy = EgovStringUtil.nullConvert(map.get("paraStanYy"));
		
		Map<String, String> args = new HashMap<String, String>();
		args.put("day1"	, EgovStringUtil.nullConvert(map.get("paraDay1")));
		//args.put("day2"	, EgovStringUtil.nullConvert(map.get("paraDay2")));
		args.put("type"	, EgovStringUtil.nullConvert(map.get("paraType")));
		args.put("org"	, EgovStringUtil.nullConvert(map.get("paraOrg")));
		args.put("stanYy"	 , paraStanYy);

		args.put("pageNo"	  , "1");
		args.put("rowsPerPage", "9999999");
		args.put("sidx"	, EgovStringUtil.nullConvert(map.get("sidx")));
		args.put("sord"	, EgovStringUtil.nullConvert(map.get("sord")));

		logger.debug(args.toString());

		List<Map<String, String>> dataList = yearendhometaxService.selectMoney(args);
		
		model.put("title", "");		//model.put("title", "기부금");
		model.put("fileName", "기부금_"+paraStanYy) ;
		model.put("titleKey", EgovStringUtil.nullConvert(map.get("titleKey")));
		model.put("titleHeader", URLDecoder.decode(EgovStringUtil.nullConvert(map.get("titleHeader")), "utf-8"));
		model.put("dataList", dataList);

		cmmService.buildExcelDocument(model, request, response);
	}

}
