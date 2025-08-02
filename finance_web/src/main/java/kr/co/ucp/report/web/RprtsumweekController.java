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
import kr.co.ucp.report.service.RprtsumweekService;

@Controller
public class RprtsumweekController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="rprtsumweekService")
	RprtsumweekService rprtsumweekService;

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
	@RequestMapping(value="/report/sumweek.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

        // 날짜조회
        String toDate = EgovDateUtil.getToday();
        request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
        
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"       , "ASC");

		args.put("sidx"       , "ACCT_GB");
		request.setAttribute("AcctgbList", acctgbService.selectAcctgb(args));		// 재정
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "report/rprtsumweek";
	}

	// 조건검색
	@RequestMapping(value="/report/sumweek/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		String stanYmd = EgovStringUtil.nullConvert(request.getParameter("stanYmd"));
		args.put("stanYmd"     , stanYmd);
		args.put("acctGb"      , (String) request.getParameter("acctGb"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		args.put("stanYy"      , stanYmd.substring(0,4));
		List<Map<String, String>> list = moneyService.selectMoney(args);		// 헌금
		for( int j=0; j<list.size() ; j++ ) {
			list.get(j).put("moneyAmtLastWeek", "0");
			list.get(j).put("moneyAmtThisWeek", "0");
			list.get(j).put("moneyAmtSumm", "0");
		}

		Map<String, String> durLastWeek = rprtsumweekService.selectDurationLastWeek(args);
		Map<String, String> durThisWeek = rprtsumweekService.selectDurationThisWeek(args);
		Map<String, String> durSumm = rprtsumweekService.selectDurationSumm(args);
		
		List<Map<String, String>> listLastWeek = rprtsumweekService.selectMoneyLastWeek(args);	// 지난주합계
		if ( listLastWeek != null ) {
			for ( int i=0 ; i<listLastWeek.size() ; i++ ) {
				for( int j=0 ; j<list.size() ; j++ ) {
					//System.out.println(i+","+j+" : "+listLastWeek.get(i).get("moneyCode").toString()+" == "+list.get(j).get("moneyCode").toString());
					if(listLastWeek.get(i).get("moneyCode").toString().equalsIgnoreCase(list.get(j).get("moneyCode").toString())) {
						//System.out.println(i+","+j+" : "+String.valueOf(listLastWeek.get(i).get("moneyAmt")));
						list.get(j).put("moneyAmtLastWeek", String.valueOf(listLastWeek.get(i).get("moneyAmt")));
					}
				}
			}
		}

		List<Map<String, String>> listThisWeek = rprtsumweekService.selectMoneyThisWeek(args);	// 이번주합계
		if ( listThisWeek != null ) {
			for ( int i=0 ; i<listThisWeek.size() ; i++ ) {
				for( int j=0 ; j<list.size() ; j++ ) {
					//System.out.println(i+","+j+" : "+listThisWeek.get(i).get("moneyCode").toString()+" == "+list.get(j).get("moneyCode").toString());
					if(listThisWeek.get(i).get("moneyCode").toString().equalsIgnoreCase(list.get(j).get("moneyCode").toString())) {
						//System.out.println(i+","+j+" : "+String.valueOf(listThisWeek.get(i).get("moneyAmt")));
						list.get(j).put("moneyAmtThisWeek", String.valueOf(listThisWeek.get(i).get("moneyAmt")));
					}
				}
			}
		}

		List<Map<String, String>> listSumm = rprtsumweekService.selectMoneySumm(args);		// 누계
		if ( listSumm != null ) {
			for ( int i=0 ; i<listSumm.size() ; i++ ) {
				for( int j=0 ; j<list.size() ; j++ ) {
					//System.out.println(i+","+j+" : "+listMonth.get(i).get("moneyCode").toString()+" == "+list.get(j).get("moneyCode").toString());
					if(listSumm.get(i).get("moneyCode").toString().equalsIgnoreCase(list.get(j).get("moneyCode").toString())) {
						//System.out.println(i+","+j+" : "+String.valueOf(listMonth.get(i).get("moneyAmt")));
						list.get(j).put("moneyAmtSumm", String.valueOf(listSumm.get(i).get("moneyAmt")));
					}
				}
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));
		map.put("durLastWeek", durLastWeek.get("duration").toString());
		map.put("durThisWeek", durThisWeek.get("duration").toString());
		map.put("durSumm", durSumm.get("duration").toString());

		return map;
	}

	// 합계
	@SuppressWarnings("unused")
	@RequestMapping(value="/report/sumweek/getSum.json")
	@ResponseBody
	public Map<String, Object> getSum(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"     , EgovStringUtil.nullConvert(request.getParameter("stanYmd")));
		args.put("acctGb"      , (String) request.getParameter("acctGb"));
		
		Map<String, Object> mapRet = new HashMap<String, Object>();

		try {
			String lastWeekAmnt = rprtsumweekService.selectLastWeekAmnt(args);
			String thisWeekAmnt = rprtsumweekService.selectThisWeekAmnt(args);
			String totalAmnt = rprtsumweekService.selectTotalAmnt(args);
			mapRet.put("session", 1);
			mapRet.put("lastWeekAmnt", lastWeekAmnt);
			mapRet.put("thisWeekAmnt", thisWeekAmnt);
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
