package kr.co.ucp.sheet.web;

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
import kr.co.ucp.env.service.PositionService;
import kr.co.ucp.env.service.RegionService;
import kr.co.ucp.env.service.WorshipService;
import kr.co.ucp.sheet.service.SheetsummonthService;

@Controller
public class SheetsummonthController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="sheetsummonthService")
	SheetsummonthService sheetsummonthService;

	@Resource(name="acctgbService")
	AcctgbService acctgbService;

	@Resource(name="worshipService")
	WorshipService worshipService;

	@Resource(name="positionService")
	PositionService positionService;

	@Resource(name="deptService")
	DeptService deptService;

	@Resource(name="regionService")
	RegionService regionService;

	// 리스트
	@RequestMapping(value="/sheet/summonth.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

        String curMonth = EgovDateUtil.getToday().substring(0,6);
        request.setAttribute("currentMonth", EgovDateUtil.formatDate(curMonth, "-"));
        //request.setAttribute("currentMonth", "2024-03");
        
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"       , "ASC");

		args.put("sidx"       , "ACCT_GB");
		request.setAttribute("AcctgbList", acctgbService.selectAcctgb(args));		// 재정

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "sheet/sheetsummonth";
	}

	// 조건검색
	@RequestMapping(value="/sheet/summonth/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYm"     , (String) request.getParameter("stanYm"));
		args.put("stanYmd"    , (String) request.getParameter("stanYmd"));
		args.put("acctGb"     , (String) request.getParameter("acctGb"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = sheetsummonthService.selectSheetSummonth(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 합계
	@SuppressWarnings("unused")
	@RequestMapping(value="/sheet/summonth/getSum.json")
	@ResponseBody
	public Map<String, Object> getSum(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYm"     , EgovStringUtil.nullConvert(request.getParameter("stanYm")));
		args.put("stanYmd"    , (String) request.getParameter("stanYmd"));
		args.put("acctGb"     , (String) request.getParameter("acctGb"));
		
		Map<String, Object> mapRet = new HashMap<String, Object>();

		try {
			List<Map<String, String>> list = sheetsummonthService.selectTotalAmnt(args);
			for (Map<String, String> amntMap : list) {
				if ( "수입".equalsIgnoreCase(amntMap.get("inoutGb").toString())) {
					mapRet.put("inTotalAmnt", String.valueOf(amntMap.get("inTotalAmnt")));
				} else {
					mapRet.put("outTotalAmnt", String.valueOf(amntMap.get("outTotalAmnt")));
				}
			}
			mapRet.put("session", 1);
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

}
