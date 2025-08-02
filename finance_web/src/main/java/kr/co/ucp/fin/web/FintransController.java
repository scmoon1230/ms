package kr.co.ucp.fin.web;

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
import kr.co.ucp.env.service.PositionService;
import kr.co.ucp.fin.service.FinmngService;
import kr.co.ucp.fin.service.FintransService;

@Controller
public class FintransController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="fintransService")
	FintransService fintransService;

	@Resource(name="finmngService")
	FinmngService finmngService;

	@Resource(name="positionService")
	PositionService positionService;

	@Resource(name="acctgbService")
	AcctgbService acctgbService;

	// 리스트
	@RequestMapping(value="/fin/trans.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

        String toDate = EgovDateUtil.getToday();
        request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
        
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sidx"       , "ACCT_GB");
		args.put("sord"       , "ASC");
		request.setAttribute("acctgbList", acctgbService.selectAcctgb(args));		// 재정
		
		//args.put("sidx"       , "ASSET_NAME, BANK_NAME, ACCOUNT_NO");
		//args.put("useYn", "Y");
		//request.setAttribute("financeinfoList", finmngService.selectFinance(args));	// 금융자산
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "fin/fintrans";
	}

	// 조건검색
	@RequestMapping(value="/fin/trans/list.json")
	@ResponseBody
	public Map<String, Object> selectFinance(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd", (String) request.getParameter("stanYmd"));
		args.put("acctGb" , (String) request.getParameter("acctGb"));
		args.put("inoutGb", (String) request.getParameter("inoutGb"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = fintransService.selectFintrans(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 합계
	@SuppressWarnings("unused")
	@RequestMapping(value="/fin/trans/getSum.json")
	@ResponseBody
	public Map<String, Object> getSum(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd", (String) request.getParameter("stanYmd"));
		args.put("acctGb" , (String) request.getParameter("acctGb"));
		args.put("inoutGb", (String) request.getParameter("inoutGb"));
		
		Map<String, Object> mapRet = new HashMap<String, Object>();

		try {
			List<Map<String, String>> list = fintransService.selectTotalAmnt(args);
			for (Map<String, String> amntMap : list) {
				if ( amntMap != null ) {
					mapRet.put("inTotalAmnt", String.valueOf(amntMap.get("inTotalAmnt")));
					mapRet.put("intTotalAmnt", String.valueOf(amntMap.get("intTotalAmnt")));
					mapRet.put("outTotalAmnt", String.valueOf(amntMap.get("outTotalAmnt")));
					mapRet.put("totalAmnt", String.valueOf(amntMap.get("totalAmnt")));
				}
			}
			mapRet.put("session", 1);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	// 등록
	@SuppressWarnings("unused")
	@RequestMapping(value="/fin/trans/insert.json")
	@ResponseBody
	public Map<String, Object> insertFinance(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("stanYmd"   , EgovStringUtil.nullConvert(request.getParameter("stanYmd")));
		//map.put("detSeq", EgovStringUtil.nullConvert(request.getParameter("detSeq")));
		map.put("inoutGb"   , EgovStringUtil.nullConvert(request.getParameter("inoutGb")));
		map.put("assetCode" , EgovStringUtil.nullConvert(request.getParameter("assetCode")));
		map.put("moneyAmt"  , EgovStringUtil.nullConvert(request.getParameter("moneyAmt")));
		map.put("intAmt"    , EgovStringUtil.nullConvert(request.getParameter("intAmt")));
		map.put("remark"    , EgovStringUtil.nullConvert(request.getParameter("remark")));
		map.put("acctProcGb", EgovStringUtil.nullConvert(request.getParameter("acctProcGb")));
		map.put("userId"    , sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int insertResult;
		try {
			map.put("acctYn"  , "N");
			if (!"0".equalsIgnoreCase(map.get("acctProcGb").toString())) {	// 해당없음 아닐 때
				map.put("acctYn"  , "Y");
				map.put("acctCode"  , EgovStringUtil.nullConvert(request.getParameter("acctCode")));
			}
			
			insertResult = fintransService.insertFintrans(map);
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
	@RequestMapping(value="/fin/trans/update.json")
	@ResponseBody
	public Map<String, Object> updateFinance(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, String> map = new HashMap<String, String>();

		map.put("stanYmd"   , EgovStringUtil.nullConvert(request.getParameter("stanYmd")));
		map.put("detSeq"    , EgovStringUtil.nullConvert(request.getParameter("detSeq")));
		map.put("inoutGb"   , EgovStringUtil.nullConvert(request.getParameter("inoutGb")));
		map.put("assetCode" , EgovStringUtil.nullConvert(request.getParameter("assetCode")));
		map.put("moneyAmt"  , EgovStringUtil.nullConvert(request.getParameter("moneyAmt")));
		map.put("intAmt"    , EgovStringUtil.nullConvert(request.getParameter("intAmt")));
		map.put("remark"    , EgovStringUtil.nullConvert(request.getParameter("remark")));
		map.put("acctProcGb", EgovStringUtil.nullConvert(request.getParameter("acctProcGb")));
		map.put("acctCode"  , EgovStringUtil.nullConvert(request.getParameter("acctCode")));
		map.put("userId"    , sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

	/*	if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}	*/

		int updateResult;
		try {
			updateResult = fintransService.updateFintrans(map);
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
	@RequestMapping(value="/fin/trans/delete.json")
	@ResponseBody
	public Map<String, Object> deleteFinance(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> map = new HashMap<String, String>();

		map.put("stanYmd", EgovStringUtil.nullConvert(request.getParameter("stanYmd")));
		map.put("detSeq", EgovStringUtil.nullConvert(request.getParameter("detSeq")));
		//map.put("assetCode", EgovStringUtil.nullConvert(request.getParameter("assetCode")));

		int result = fintransService.deleteFintrans(map);

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
	@RequestMapping(value="/fin/trans/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteFinanceMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		String[] stanYmd = request.getParameterValues("stanYmd");
		String[] assetCode = request.getParameterValues("assetCode");
		String[] detSeq = request.getParameterValues("detSeq");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < assetCode.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("stanYmd", stanYmd[i]);
			mapId.put("assetCode", assetCode[i]);
			mapId.put("detSeq", detSeq[i]);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = fintransService.deleteFintransMulti(list);
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

	// 마감처리
	@SuppressWarnings("unused")
	@RequestMapping(value="/fin/trans/doClose.json")
	@ResponseBody
	public Map<String, Object> doClose(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"  , EgovStringUtil.nullConvert(request.getParameter("stanYmd")));
	//	args.put("stanYmd"  , (String) request.getParameter("endYmd"));		// 전표기준일자(전표등록 시 사용하는 변수)
	//	args.put("startYmd" , (String) request.getParameter("startYmd"));
	//	args.put("endYmd"   , (String) request.getParameter("endYmd"));
	//	args.put("acctGb"   , EgovStringUtil.nullConvert(request.getParameter("acctGb")));
	//	args.put("inoutGb"  , EgovStringUtil.nullConvert(request.getParameter("inoutGb")));
		args.put("acctType" , EgovStringUtil.nullConvert(request.getParameter("acctType")));
		args.put("userId"   , sesUserId);
		//logger.info(map.toString());
		
		Map<String, Object> mapRet = new HashMap<String, Object>();
		try {
			// 마감처리하였는 지 확인
			Map<String, String> reMap = fintransService.selectFintransCloseCnt(args);
			String closeCnt = String.valueOf(reMap.get("closeCnt"));
			if ( !"0".equalsIgnoreCase(closeCnt)) {
				mapRet.put("session", 1);
				mapRet.put("msg", "이미 마감처리하였습니다.\n\n마감을 취소하고 다시 시도하십시오.");
				
			} else {
				int insertResult = fintransService.doClose(args);
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
	@RequestMapping(value="/fin/trans/cancelClose.json")
	@ResponseBody
	public Map<String, Object> cancelClose(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"  , EgovStringUtil.nullConvert(request.getParameter("stanYmd")));
	//	args.put("stanYmd"  , (String) request.getParameter("endYmd"));		// 수입전표기준일자
	//	args.put("startYmd" , EgovStringUtil.nullConvert(request.getParameter("startYmd")));
	//	args.put("endYmd"   , EgovStringUtil.nullConvert(request.getParameter("endYmd")));
	//	args.put("acctGb"   , EgovStringUtil.nullConvert(request.getParameter("acctGb")));
	//	args.put("inoutGb"  , EgovStringUtil.nullConvert(request.getParameter("inoutGb")));
	//	args.put("acctType" , EgovStringUtil.nullConvert(request.getParameter("acctType")));
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "99999");
		args.put("sidx"       , "SEQ_NO");
		args.put("sord"       , "ASC");
		
		logger.info(args.toString());
		
		Map<String, Object> mapRet = new HashMap<String, Object>();
		try {
			// 마감처리하였는 지 확인
			Map<String, String> reMap = fintransService.selectFintransCloseCnt(args);
			String closeCnt = String.valueOf(reMap.get("closeCnt"));
			if ( "0".equalsIgnoreCase(closeCnt)) {
				mapRet.put("session", 1);
				mapRet.put("msg", "아직 마감처리하지 않았습니다.");
				
			} else {
				int insertResult = fintransService.cancelClose(args);
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
