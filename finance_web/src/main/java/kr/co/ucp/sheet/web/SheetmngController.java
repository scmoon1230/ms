package kr.co.ucp.sheet.web;

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
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.utl.fcc.service.EgovDateUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.env.service.AcctgbService;
import kr.co.ucp.money.service.MoneycloseService;
import kr.co.ucp.sheet.service.SheetmngService;
import kr.co.ucp.sheet2.service.Sheet2mngService;

@Controller
public class SheetmngController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="moneycloseService")
	MoneycloseService moneycloseService;

	@Resource(name="sheetmngService")
	SheetmngService sheetmngService;

	@Resource(name="sheet2mngService")
	Sheet2mngService sheet2mngService;

	@Resource(name="acctgbService")
	AcctgbService acctgbService;

	// 리스트
	@RequestMapping(value="/sheet/mng.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

        request.setAttribute("type", request.getParameter("type").toString());
		
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

		return "sheet/sheetmng";
	}

	// 조건검색
	@RequestMapping(value="/sheet/mng/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"     , (String) request.getParameter("stanYmd"));
		args.put("acctGb"      , (String) request.getParameter("acctGb"));
		args.put("inoutGb"     , (String) request.getParameter("inoutGb"));
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

		List<Map<String, String>> list = sheetmngService.selectSheetMng(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));
		map.put("startYmd", startYmd);
		map.put("endYmd", endYmd);

		return map;
	}

	// 합계
	@SuppressWarnings("unused")
	@RequestMapping(value="/sheet/mng/getSum.json")
	@ResponseBody
	public Map<String, Object> getSum(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"     , (String) request.getParameter("stanYmd"));
		args.put("acctGb"      , (String) request.getParameter("acctGb"));
		args.put("inoutGb"     , (String) request.getParameter("inoutGb"));
		
		Map<String, Object> mapRet = new HashMap<String, Object>();

		try {
			String totalAmnt = sheetmngService.selectSheetMngTotalAmnt(args);
			mapRet.put("session", 1);
			mapRet.put("totalAmnt", totalAmnt);
		} catch (Exception e) {
			logger.error(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	// 마감처리
	@SuppressWarnings("unused")
	@RequestMapping(value="/sheet/mng/doClose.json")
	@ResponseBody
	public Map<String, Object> doClose(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		String stanYmd = EgovStringUtil.nullConvert(request.getParameter("stanYmd"));
		String acctGb  = EgovStringUtil.nullConvert(request.getParameter("acctGb"));
		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"  , stanYmd);
		args.put("stanYm"   , stanYmd.substring(0,6));
		args.put("acctGb"   , acctGb);
		args.put("userId"   , sesUserId);
		//logger.info(map.toString());
		
		Map<String, Object> mapRet = new HashMap<String, Object>();
		try {
			// 지난주 주간합계가 있는 지 확인(지난주 마감처리하였는 지 확인)
			Map<String, String> prevStartEndMap = sheetmngService.selectPrevStartEndDay(args);
			String prevStanYmd = prevStartEndMap.get("endYmd").toString().replaceAll("-", "");	// 지난주 기준일자
			Map<String, String> prevArgs = new HashMap<String, String>();
			prevArgs.put("stanYmd"  , prevStanYmd);
			prevArgs.put("acctGb"   , acctGb);
			
			Map<String, String> otherWeeksumMap = sheetmngService.selectWeekSum(prevArgs);
			if ( otherWeeksumMap == null ) {	// 지난주 주간합계가 없을 때
				mapRet.put("session", 1);
				mapRet.put("msg", "지난주 전표가 마감처리되지 않았습니다.\n\n지난주 전표를 먼저 마감처리하십시오.");
				
			} else {
				// 마감처리하였는 지 확인
				//Map<String, String> reMap = sheetmngService.selectSheetCloseCnt(args);
				//String closeCnt = String.valueOf(reMap.get("closeCnt"));
				//logger.info("closeCnt = "+closeCnt);
				//if ( !"0".equalsIgnoreCase(closeCnt)) {		// 마감처리한 전표가 있을 때
				Map<String, String> thisWeeksumMap = sheetmngService.selectWeekSum(args);
				if ( thisWeeksumMap != null ) {	// 이번주 주간합계가 있을 때
					mapRet.put("session", 1);
					mapRet.put("msg", "이미 마감처리하였습니다.\n\n마감을 취소하고 다시 시도하십시오.");
					
				} else {
					int insertResult = sheetmngService.doClose(args);
					
					String msg = "마감처리를 완료하였습니다.";
					if ( "1".equalsIgnoreCase(acctGb)) {	// 1재정일 때
						insertResult = sheet2mngService.doClose(args);		// 종교인재정 마감처리한다.
						msg = "종교인재정도 함께 "+msg;
					}
					
					mapRet.put("session", 1);
					mapRet.put("msg", msg);
				}
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
	@RequestMapping(value="/sheet/mng/cancelClose.json")
	@ResponseBody
	public Map<String, Object> cancelClose(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		String stanYmd = EgovStringUtil.nullConvert(request.getParameter("stanYmd"));
		String acctGb  = EgovStringUtil.nullConvert(request.getParameter("acctGb"));
		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"  , stanYmd);
		args.put("stanYm"   , stanYmd.substring(0,6));
		args.put("acctGb"   , acctGb);
		args.put("userId"   , sesUserId);
		//logger.info(args.toString());
		
		Map<String, Object> mapRet = new HashMap<String, Object>();
		try {
			// 다음주 주간합계가 있는 지 확인(다음주 마감처리하였는 지 확인)
			Map<String, String> nextStartEndMap = sheetmngService.selectNextStartEndDay(args);
			String nextStanYmd = nextStartEndMap.get("endYmd").toString().replaceAll("-", "");	// 지난주 기준일자
			Map<String, String> nextArgs = new HashMap<String, String>();
			nextArgs.put("stanYmd"  , nextStanYmd);
			nextArgs.put("acctGb"   , acctGb);
			
			Map<String, String> otherWeeksumMap = sheetmngService.selectWeekSum(nextArgs);
			if ( otherWeeksumMap != null ) {	// 다음주 주간합계가 있을 때
				mapRet.put("session", 1);
				mapRet.put("msg", "다음주 전표가 이미 마감처리되었습니다.\n\n다음주 전표마감을 먼저 취소하십시오.");
				
			} else {
				// 마감처리하였는 지 확인
				//Map<String, String> reMap = sheetmngService.selectSheetCloseCnt(args);
				//String closeCnt = String.valueOf(reMap.get("closeCnt"));
				//if ( "0".equalsIgnoreCase(closeCnt)) {		// 마감처리한 전표가 없을 때
				Map<String, String> thisWeeksumMap = sheetmngService.selectWeekSum(args);
				if ( thisWeeksumMap == null ) {	// 이번주 주간합계가 없을 때
					mapRet.put("session", 1);
					mapRet.put("msg", "아직 마감처리하지 않았습니다.");
					
				} else {
					int insertResult = sheetmngService.cancelClose(args);

					String msg = "마감을 취소하였습니다.";
					if ( "1".equalsIgnoreCase(acctGb)) {	// 1재정일 때
						insertResult = sheet2mngService.cancelClose(args);		// 종교인재정 마감취소처리한다.
						msg = "종교인재정도 함께 "+msg;
					}
					
					mapRet.put("session", 1);
					mapRet.put("msg", msg);
				}	
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}
	
	// 등록
	@SuppressWarnings("unused")
	@RequestMapping(value="/sheet/mng/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, String> map = new HashMap<String, String>();

		map.put("stanYmd"  , EgovStringUtil.nullConvert(request.getParameter("stanYmd")));
		map.put("acctGb"   , EgovStringUtil.nullConvert(request.getParameter("acctGb")));
		map.put("acctCode" , EgovStringUtil.nullConvert(request.getParameter("acctCode")));
		map.put("acctType" , EgovStringUtil.nullConvert(request.getParameter("acctType")));
		map.put("infoData" , EgovStringUtil.nullConvert(request.getParameter("infoData")));
		map.put("userId"   , sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

//		if (!CommonUtil.checkDataFilterObj(map)) {
//			mapRet.put("session", 1);
//			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
//			return mapRet;
//		}

		int insertResult;
		try {
			insertResult = sheetmngService.insertSheetMng(map);
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
	@RequestMapping(value="/sheet/mng/update.json")
	@ResponseBody
	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, String> map = new HashMap<String, String>();

		map.put("seqNo", EgovStringUtil.nullConvert(request.getParameter("seqNo")));
		map.put("acctCode", EgovStringUtil.nullConvert(request.getParameter("acctCode")));
		map.put("moneyAmt", EgovStringUtil.nullConvert(request.getParameter("moneyAmt")));
		map.put("acctRemark", EgovStringUtil.nullConvert(request.getParameter("acctRemark")));

		Map<String, Object> mapRet = new HashMap<String, Object>();

//		if (!CommonUtil.checkDataFilterObj(map)) {
//			mapRet.put("session", 1);
//			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
//			return mapRet;
//		}

		int updateResult;
		try {
			updateResult = sheetmngService.updateSheetMng(map);
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
	@RequestMapping(value="/sheet/mng/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		
		map.put("seqNo", EgovStringUtil.nullConvert(request.getParameter("seqNo")));

		int result = sheetmngService.deleteSheetMng(map);

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

}
