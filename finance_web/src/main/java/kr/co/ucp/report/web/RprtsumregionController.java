package kr.co.ucp.report.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import kr.co.ucp.env.service.DeptService;
import kr.co.ucp.env.service.MoneyService;
import kr.co.ucp.env.service.PositionService;
import kr.co.ucp.env.service.RegionService;
import kr.co.ucp.env.service.WorshipService;
import kr.co.ucp.report.service.RprtsumregionService;

@Controller
public class RprtsumregionController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="acctcodeService")
	AcctcodeService acctcodeService;

	@Resource(name="rprtsumregionService")
	RprtsumregionService rprtsumregionService;

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
	@RequestMapping(value="/report/sumregion.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

        // 날짜조회
        String toDate = EgovDateUtil.getToday();
        request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
        
		Map<String, String> args = new HashMap<String, String>();

		request.setAttribute("stanYyList", acctcodeService.stanYyList(args));
		
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"       , "ASC");

		args.put("stanYmd"    , EgovDateUtil.getToday());
		args.put("sidx"       , "MONEY_CODE");
		request.setAttribute("moneyList", moneyService.selectMoney(args));		// 헌금

		args.put("sidx"       , "DEPT_CODE");
		request.setAttribute("deptList", deptService.selectDept(args));		// 교구

		args.put("sidx"       , "REGION_CODE");
		request.setAttribute("regionList", regionService.selectRegion(args));		// 구역
		
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "report/rprtsumregion";
	}

	// 조건검색
	@RequestMapping(value="/report/sumregion/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		String deptCode = (String) request.getParameter("deptCode");
		String regionCode = (String) request.getParameter("regionCode");

		String stanYy   = (String) request.getParameter("stanYy");
		String startYmd = stanYy+"-01-01";
		String endYmd   = stanYy+"-12-31";
		args.put("stanYy"      , stanYy);
		args.put("startYmd"    , startYmd.replaceAll("-", ""));
		args.put("endYmd"      , endYmd.replaceAll("-", ""));
	//	args.put("stanYmd"     , EgovStringUtil.nullConvert(request.getParameter("stanYmd")));
	//	args.put("acctGb"      , (String) request.getParameter("acctGb"));
		args.put("moneyCode"   , (String) request.getParameter("moneyCode"));
		args.put("deptCode"    , (String) request.getParameter("deptCode"));
		args.put("regionCode"  , (String) request.getParameter("regionCode"));
	//	args.put("worshipCode" , (String) request.getParameter("worshipCode"));
	//	args.put("positionCode", (String) request.getParameter("positionCode"));
	//	args.put("memberNo"    , (String) request.getParameter("memberNo"));
	//	args.put("memberName"  , (String) request.getParameter("memberName"));
		
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		logger.debug(args.toString());

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();	// 교구 구역 정보
		
		List<Map<String, String>> listCount = rprtsumregionService.selectDeptRegion(args);		// 교구 구역 개수
		for( int i=0; i<listCount.size() ; i++ ) {
			String sSize = String.valueOf(listCount.get(i).get("regionNum"));
			for (int j=0 ; j<Integer.parseInt(sSize) ; j++ ) {
				Map<String, String> info = new HashMap<String, String>();
				info.put("moneyCode", (String) request.getParameter("moneyCode"));
				info.put("deptCode", listCount.get(i).get("deptCode").toString());
				info.put("regionCode", StringUtils.leftPad(String.valueOf(j+1),3,"0"));
				
				if ( !"".equalsIgnoreCase(deptCode) && !"".equalsIgnoreCase(regionCode) ) {
					if ( regionCode.equalsIgnoreCase(StringUtils.leftPad(String.valueOf(j+1),3,"0"))) {
						list.add(info);
					}
				} else {
					list.add(info);
				}
				//System.out.println(info.get("deptCode").toString()+", "+info.get("regionCode").toString());
			}
		}
		String ROWCNT = Integer.toString(list.size());
		System.out.println("ROWCNT = "+ROWCNT);
		
		for( int j=0; j<list.size() ; j++ ) {
			list.get(j).put("rowcnt", ROWCNT);
			list.get(j).put("moneyAmtMonth0", "0");		list.get(j).put("moneyAmtMonth1", "0");		list.get(j).put("moneyAmtMonth2", "0");
			list.get(j).put("moneyAmtMonth3", "0");		list.get(j).put("moneyAmtMonth4", "0");		list.get(j).put("moneyAmtMonth5", "0");
			list.get(j).put("moneyAmtMonth6", "0");		list.get(j).put("moneyAmtMonth7", "0");		list.get(j).put("moneyAmtMonth8", "0");
			list.get(j).put("moneyAmtMonth9", "0");		list.get(j).put("moneyAmtMonth10", "0");	list.get(j).put("moneyAmtMonth11", "0");
			list.get(j).put("moneyAmtSumm", "0");
		}

		//Map<String, String> durYear = rprtsumregionService.selectDurationYear(args);
		
		for( int m=0; m<list.size() ; m++ ) {
			args.put("deptCode"    , list.get(m).get("deptCode").toString());
			args.put("regionCode"  , list.get(m).get("regionCode").toString());

			String stanYmd = startYmd.replaceAll("-", "");

			for ( int k=0 ; k<12 ; k++ ) {
				args.put("stanYmd"     , stanYmd);

				List<Map<String, String>> listMonth = rprtsumregionService.selectMoneyMonth(args);	// 월계
				if ( listMonth != null ) {
					for ( int i=0 ; i<listMonth.size() ; i++ ) {
						for( int j=0 ; j<list.size() ; j++ ) {
							//System.out.println(i+","+j+" : "+listMonth.get(i).get("moneyCode").toString()+" == "+list.get(j).get("moneyCode").toString());
							if(listMonth.get(i).get("moneyCode").toString().equalsIgnoreCase(list.get(j).get("moneyCode").toString())) {
								//System.out.println(i+","+j+" : "+String.valueOf(listMonth.get(i).get("moneyAmt")));
								list.get(m).put("moneyAmtMonth"+k, String.valueOf(listMonth.get(i).get("moneyAmt")));
							}
						}
					}
				}
				Map<String, String> nextMonth = rprtsumregionService.selectNextMonth(args);
				stanYmd = nextMonth.get("stanYmd").toString();
			}

			List<Map<String, String>> listSumm = rprtsumregionService.selectMoneyYear(args);		// 누계
			if ( listSumm != null ) {
				for ( int i=0 ; i<listSumm.size() ; i++ ) {
					for( int j=0 ; j<list.size() ; j++ ) {
						//System.out.println(i+","+j+" : "+listMonth.get(i).get("moneyCode").toString()+" == "+list.get(j).get("moneyCode").toString());
						if(listSumm.get(i).get("moneyCode").toString().equalsIgnoreCase(list.get(j).get("moneyCode").toString())) {
							//System.out.println(i+","+j+" : "+String.valueOf(listMonth.get(i).get("moneyAmt")));
							list.get(m).put("moneyAmtSumm", String.valueOf(listSumm.get(i).get("moneyAmt")));
						}
					}
				}
			}
			    
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));
		map.put("durYear", startYmd+" ~ "+endYmd);

		return map;
	}

	// 합계
	@SuppressWarnings("unused")
	@RequestMapping(value="/report/sumregion/getSum.json")
	@ResponseBody
	public Map<String, Object> getSum(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> args = new HashMap<String, String>();
		String stanYy   = (String) request.getParameter("stanYy");
		String startYmd = stanYy+"-01-01";
		String endYmd   = stanYy+"-12-31";
		args.put("stanYy"      , stanYy);
		args.put("startYmd"    , startYmd.replaceAll("-", ""));
		args.put("endYmd"      , endYmd.replaceAll("-", ""));
	//	args.put("stanYmd"     , EgovStringUtil.nullConvert(request.getParameter("stanYmd")));
	//	args.put("acctGb"      , (String) request.getParameter("acctGb"));
		args.put("moneyCode"   , (String) request.getParameter("moneyCode"));
		args.put("deptCode"    , (String) request.getParameter("deptCode"));
		args.put("regionCode"  , (String) request.getParameter("regionCode"));
	//	args.put("worshipCode" , (String) request.getParameter("worshipCode"));
	//	args.put("positionCode", (String) request.getParameter("positionCode"));
	//	args.put("memberNo"    , (String) request.getParameter("memberNo"));
	//	args.put("memberName"  , (String) request.getParameter("memberName"));
		
		Map<String, Object> mapRet = new HashMap<String, Object>();

		try {

			//Map<String, String> durYear = rprtsumregionService.selectDurationYear(args);
			String stanYmd = startYmd.replaceAll("-", "");

			for ( int k=0 ; k<12 ; k++ ) {
				args.put("stanYmd"     , stanYmd);
				mapRet.put("monthAmnt"+k, rprtsumregionService.selectMonthAmnt(args));

				Map<String, String> nextMonth = rprtsumregionService.selectNextMonth(args);
				stanYmd = nextMonth.get("stanYmd").toString();
			}
			
			String totalAmnt = rprtsumregionService.selectTotalAmnt(args);
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
