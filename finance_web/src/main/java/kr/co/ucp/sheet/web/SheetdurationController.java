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
import kr.co.ucp.sheet.service.SheetdurationService;

@Controller
public class SheetdurationController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="sheetdurationService")
	SheetdurationService sheetdurationService;

	@Resource(name="acctgbService")
	AcctgbService acctgbService;

	// 리스트
	@RequestMapping(value="/sheet/duration.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

        //request.setAttribute("type", request.getParameter("type").toString());
		
        // 날짜조회
        String toDate = EgovDateUtil.getToday();
        request.setAttribute("startDate", EgovDateUtil.formatDate(toDate.subSequence(0, 6)+"01", "-"));
        request.setAttribute("endDate", EgovDateUtil.formatDate(EgovDateUtil.addYearMonthDay(toDate, 0, 0, -1), "-"));
        request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
        
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"       , "ASC");

		args.put("sidx"       , "ACCT_GB");
		request.setAttribute("AcctgbList", acctgbService.selectAcctgb(args));		// 재정
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "sheet/sheetduration";
	}

	// 조건검색
	@RequestMapping(value="/sheet/duration/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("startDate"   , EgovStringUtil.nullConvert(request.getParameter("startDate")));
		args.put("endDate"     , EgovStringUtil.nullConvert(request.getParameter("endDate")));
		args.put("acctGb"      , (String) request.getParameter("acctGb"));
		args.put("inoutGb"     , (String) request.getParameter("inoutGb"));
		args.put("acctCode"    , (String) request.getParameter("acctCode"));
		args.put("acctRemark"  , (String) request.getParameter("acctRemark"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = sheetdurationService.selectSheetDuration(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 합계
	@SuppressWarnings("unused")
	@RequestMapping(value="/sheet/duration/getSum.json")
	@ResponseBody
	public Map<String, Object> getSum(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> args = new HashMap<String, String>();
		args.put("startDate"   , EgovStringUtil.nullConvert(request.getParameter("startDate")));
		args.put("endDate"     , EgovStringUtil.nullConvert(request.getParameter("endDate")));
		args.put("acctGb"      , (String) request.getParameter("acctGb"));
		args.put("inoutGb"     , (String) request.getParameter("inoutGb"));
		args.put("acctCode"    , (String) request.getParameter("acctCode"));
		args.put("acctRemark"  , (String) request.getParameter("acctRemark"));
		
		Map<String, Object> mapRet = new HashMap<String, Object>();

		try {
			String totalAmnt = sheetdurationService.selectSheetDurationTotalAmnt(args);
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
