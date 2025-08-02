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

@Controller
public class FinmngController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="finmngService")
	FinmngService finmngService;

	@Resource(name="positionService")
	PositionService positionService;

	@Resource(name="acctgbService")
	AcctgbService acctgbService;

	// 리스트
	@RequestMapping(value="/fin/mng.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

        String toDate = EgovDateUtil.getToday();
        request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
        //request.setAttribute("currentDay", "2024-03-24");
        
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sidx"       , "ACCT_GB");
		args.put("sord"       , "ASC");
		request.setAttribute("acctgbList", acctgbService.selectAcctgb(args));		// 재정
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "fin/finmng";
	}

	// 조건검색
	@RequestMapping(value="/fin/mng/list.json")
	@ResponseBody
	public Map<String, Object> selectFinance(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("acctGb"   , (String) request.getParameter("acctGb"));
		args.put("assetGb"  , (String) request.getParameter("assetGb"));
		args.put("assetCode", (String) request.getParameter("assetCode"));
		args.put("assetName", (String) request.getParameter("assetName"));
		args.put("accountNo", (String) request.getParameter("accountNo"));
		args.put("useYn"    , (String) request.getParameter("useYn"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = finmngService.selectFinance(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 등록
	@SuppressWarnings("unused")
	@RequestMapping(value="/fin/mng/insert.json")
	@ResponseBody
	public Map<String, Object> insertFinance(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("assetCode", EgovStringUtil.nullConvert(request.getParameter("assetCode")));
		map.put("assetName", EgovStringUtil.nullConvert(request.getParameter("assetName")));
		map.put("acctGb", EgovStringUtil.nullConvert(request.getParameter("acctGb")));
		map.put("assetGb", EgovStringUtil.nullConvert(request.getParameter("assetGb")));
		map.put("bankName", EgovStringUtil.nullConvert(request.getParameter("bankName")));
		map.put("accountNo", EgovStringUtil.nullConvert(request.getParameter("accountNo")));
		map.put("issueYmd", EgovStringUtil.nullConvert(request.getParameter("issueYmd")));
		map.put("mtyYmd", EgovStringUtil.nullConvert(request.getParameter("mtyYmd")));
		map.put("remark", EgovStringUtil.nullConvert(request.getParameter("remark")));
		map.put("useYn", EgovStringUtil.nullConvert(request.getParameter("useYn")));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int insertResult;
		try {
			insertResult = finmngService.insertFinance(map);
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
	@RequestMapping(value="/fin/mng/update.json")
	@ResponseBody
	public Map<String, Object> updateFinance(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("assetCode", EgovStringUtil.nullConvert(request.getParameter("assetCode")));
		map.put("assetName", EgovStringUtil.nullConvert(request.getParameter("assetName")));
		map.put("acctGb", EgovStringUtil.nullConvert(request.getParameter("acctGb")));
		map.put("assetGb", EgovStringUtil.nullConvert(request.getParameter("assetGb")));
		map.put("bankName", EgovStringUtil.nullConvert(request.getParameter("bankName")));
		map.put("accountNo", EgovStringUtil.nullConvert(request.getParameter("accountNo")));
		map.put("issueYmd", EgovStringUtil.nullConvert(request.getParameter("issueYmd")));
		map.put("mtyYmd", EgovStringUtil.nullConvert(request.getParameter("mtyYmd")));
		map.put("remark", EgovStringUtil.nullConvert(request.getParameter("remark")));
		map.put("useYn", EgovStringUtil.nullConvert(request.getParameter("useYn")));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int updateResult;
		try {
			updateResult = finmngService.updateFinance(map);
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
	@RequestMapping(value="/fin/mng/delete.json")
	@ResponseBody
	public Map<String, Object> deleteFinance(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> map = new HashMap<String, String>();

		map.put("assetCode", EgovStringUtil.nullConvert(request.getParameter("assetCode")));

		int result = finmngService.deleteFinance(map);

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
	@RequestMapping(value="/fin/mng/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteFinanceMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		String[] assetCode = request.getParameterValues("assetCode");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < assetCode.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("assetCode", assetCode[i]);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = finmngService.deleteFinanceMulti(list);
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

}
