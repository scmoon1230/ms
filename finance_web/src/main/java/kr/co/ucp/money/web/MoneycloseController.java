package kr.co.ucp.money.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.utl.fcc.service.EgovDateUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.env.service.AcctgbService;
import kr.co.ucp.env.service.MoneyService;
import kr.co.ucp.money.service.MoneycloseService;

@Controller
public class MoneycloseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="moneycloseService")
	MoneycloseService moneycloseService;

	@Resource(name="acctgbService")
	AcctgbService acctgbService;

	@Resource(name="moneyService")
	MoneyService moneyService;

	// 리스트
	@RequestMapping(value="/money/close.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

        String toDate = EgovDateUtil.getToday();
        request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
        
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"       , "ASC");

		args.put("sidx"       , "ACCT_GB");
		request.setAttribute("AcctgbList", acctgbService.selectAcctgb(args));		// 재정

		//args.put("stanYmd"    , EgovDateUtil.getToday());
		//args.put("sidx"       , "MONEY_CODE");
		//request.setAttribute("moneyList", moneyService.selectMoney(args));		// 헌금

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "money/moneyclose";
	}

	// 조건검색
	@RequestMapping(value="/money/close/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"     , (String) request.getParameter("stanYmd"));
		args.put("acctGb"      , (String) request.getParameter("acctGb"));
		//args.put("moneyCode"   , (String) request.getParameter("moneyCode"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));

		Map<String, String> seMap = moneycloseService.selectStartEndDay(args);
		String startYmd = seMap.get("startYmd").toString();
		String endYmd = seMap.get("endYmd").toString();
		args.put("startYmd", startYmd.replaceAll("-", ""));
		args.put("endYmd"  , endYmd.replaceAll("-", ""));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = moneycloseService.selectMoneyClose(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));
		map.put("startYmd", startYmd);
		map.put("endYmd", endYmd);

		return map;
	}

	// 합계
	@SuppressWarnings("unused")
	@RequestMapping(value="/money/close/getSum.json")
	@ResponseBody
	public Map<String, Object> getSum(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> args = new HashMap<String, String>();
	//	args.put("stanYmd"     , (String) request.getParameter("stanYmd"));
		args.put("startYmd"    , (String) request.getParameter("startYmd"));
		args.put("endYmd"      , (String) request.getParameter("endYmd"));
		args.put("acctGb"      , (String) request.getParameter("acctGb"));
	//	args.put("moneyCode"   , (String) request.getParameter("moneyCode"));

		Map<String, Object> mapRet = new HashMap<String, Object>();
		try {
			String totalAmnt = moneycloseService.selectMoneyCloseTotalAmnt(args);
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

	// 마감처리
	@SuppressWarnings("unused")
	@RequestMapping(value="/money/close/doClose.json")
	@ResponseBody
	public Map<String, Object> doClose(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, String> args = new HashMap<String, String>();
	//	args.put("stanYmd"  , EgovStringUtil.nullConvert(request.getParameter("stanYmd")));
	//	args.put("stanYmd"  , (String) request.getParameter("endYmd"));		// 전표기준일자(전표등록 시 사용하는 변수)
		args.put("startYmd" , (String) request.getParameter("startYmd"));
		args.put("endYmd"   , (String) request.getParameter("endYmd"));
		args.put("acctGb"   , EgovStringUtil.nullConvert(request.getParameter("acctGb")));
		args.put("inoutGb"  , EgovStringUtil.nullConvert(request.getParameter("inoutGb")));
		args.put("acctType" , EgovStringUtil.nullConvert(request.getParameter("acctType")));
		args.put("userId"   , sesUserId);
		//logger.info(map.toString());
		
		Map<String, Object> mapRet = new HashMap<String, Object>();
		try {
			// 마감처리하였는 지 확인
			Map<String, String> reMap = moneycloseService.selectMoneyCloseCnt(args);
			String closeCnt = String.valueOf(reMap.get("closeCnt"));
			if ( !"0".equalsIgnoreCase(closeCnt)) {
				mapRet.put("session", 1);
				mapRet.put("msg", "이미 마감처리하였습니다.\n\n마감을 취소하고 다시 시도하십시오.");
				
			} else {
				int insertResult = moneycloseService.doClose(args);
				mapRet.put("session", 1);
				mapRet.put("msg", "마감처리를 완료하였습니다.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	// 마감취소
	@SuppressWarnings("unused")
	@RequestMapping(value="/money/close/cancelClose.json")
	@ResponseBody
	public Map<String, Object> cancelClose(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, String> args = new HashMap<String, String>();
	//	args.put("stanYmd"  , EgovStringUtil.nullConvert(request.getParameter("stanYmd")));
		args.put("stanYmd"  , (String) request.getParameter("endYmd"));		// 수입전표기준일자
		args.put("startYmd" , EgovStringUtil.nullConvert(request.getParameter("startYmd")));
		args.put("endYmd"   , EgovStringUtil.nullConvert(request.getParameter("endYmd")));
		args.put("acctGb"   , EgovStringUtil.nullConvert(request.getParameter("acctGb")));
		args.put("inoutGb"  , EgovStringUtil.nullConvert(request.getParameter("inoutGb")));
	//	args.put("acctType" , EgovStringUtil.nullConvert(request.getParameter("acctType")));
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "99999");
		args.put("sidx"       , "SEQ_NO");
		args.put("sord"       , "ASC");
		
		logger.info(args.toString());
		
		Map<String, Object> mapRet = new HashMap<String, Object>();
		try {
			// 마감처리하였는 지 확인
			Map<String, String> reMap = moneycloseService.selectMoneyCloseCnt(args);
			String closeCnt = String.valueOf(reMap.get("closeCnt"));
			if ( "0".equalsIgnoreCase(closeCnt)) {
				mapRet.put("session", 1);
				mapRet.put("msg", "아직 마감처리하지 않았습니다.");
				
			} else {
				int insertResult = moneycloseService.cancelClose(args);
				mapRet.put("session", 1);
				mapRet.put("msg", "마감을 취소하였습니다.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}
	
}
