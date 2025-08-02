package kr.co.ucp.sheet2.web;

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
import kr.co.ucp.sheet2.service.Sheet2monthService;

@Controller
public class Sheet2monthController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="sheet2monthService")
	Sheet2monthService sheet2monthService;

	@Resource(name="acctgbService")
	AcctgbService acctgbService;

	// 리스트
	@RequestMapping(value="/sheet2/month.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

        // 날짜조회
        String curMonth = EgovDateUtil.getToday().substring(0,6);
        request.setAttribute("currentMonth", EgovDateUtil.formatDate(curMonth, "-"));
        
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"       , "ASC");

		args.put("sidx"       , "ACCT_GB");
		request.setAttribute("AcctgbList", acctgbService.selectAcctgb(args));		// 재정

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "sheet2/sheet2month";
	}

	// 조건검색
	@RequestMapping(value="/sheet2/month/list.json")
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

		Map<String, String> durMonth = sheet2monthService.selectDurationMonth(args);
		Map<String, String> durSumm = sheet2monthService.selectDurationSumm(args);

		args.put("yearFromYmd" , durSumm.get("fromYmd").toString().replaceAll("-", ""));
		
		List<Map<String, String>> list = sheet2monthService.selectMoneyMonth(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));
		map.put("durMonth", durMonth.get("fromYmd").toString()+" ~ "+durMonth.get("endYmd").toString());
		map.put("durSumm", durSumm.get("fromYmd").toString()+" ~ "+durSumm.get("endYmd").toString());

		return map;
	}

	// 등록
	@SuppressWarnings("unused")
	@RequestMapping(value="/sheet2/month/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("memberId", EgovStringUtil.nullConvert(request.getParameter("memberId")));
		map.put("memberNo", EgovStringUtil.nullConvert(request.getParameter("memberNo")));
		map.put("memberName", EgovStringUtil.nullConvert(request.getParameter("memberName")));
		map.put("sexType", EgovStringUtil.nullConvert(request.getParameter("sexType")));
		map.put("telNo", EgovStringUtil.nullConvert(request.getParameter("telNo")));
		map.put("hphoneNo", EgovStringUtil.nullConvert(request.getParameter("hphoneNo")));
		map.put("positionCode", EgovStringUtil.nullConvert(request.getParameter("positionCode")));
		map.put("deptCode", EgovStringUtil.nullConvert(request.getParameter("deptCode")));
		map.put("regionCode", EgovStringUtil.nullConvert(request.getParameter("regionCode")));
		map.put("addr", EgovStringUtil.nullConvert(request.getParameter("addr")));
		map.put("familyRemark", EgovStringUtil.nullConvert(request.getParameter("familyRemark")));


		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int insertResult;
		try {
			insertResult = sheet2monthService.insertMoney(map);
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
	@RequestMapping(value="/sheet2/month/update.json")
	@ResponseBody
	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("memberId", EgovStringUtil.nullConvert(request.getParameter("memberId")));
		map.put("memberNo", EgovStringUtil.nullConvert(request.getParameter("memberNo")));
		map.put("memberName", EgovStringUtil.nullConvert(request.getParameter("memberName")));
		map.put("sexType", EgovStringUtil.nullConvert(request.getParameter("sexType")));
		map.put("telNo", EgovStringUtil.nullConvert(request.getParameter("telNo")));
		map.put("hphoneNo", EgovStringUtil.nullConvert(request.getParameter("hphoneNo")));
		map.put("positionCode", EgovStringUtil.nullConvert(request.getParameter("positionCode")));
		map.put("deptCode", EgovStringUtil.nullConvert(request.getParameter("deptCode")));
		map.put("regionCode", EgovStringUtil.nullConvert(request.getParameter("regionCode")));
		map.put("addr", EgovStringUtil.nullConvert(request.getParameter("addr")));
		map.put("familyRemark", EgovStringUtil.nullConvert(request.getParameter("familyRemark")));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int updateResult;
		try {
			updateResult = sheet2monthService.updateMoney(map);
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
	@RequestMapping(value="/sheet2/month/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		
		map.put("memberId", EgovStringUtil.nullConvert(request.getParameter("memberId")));
		map.put("memberNo", EgovStringUtil.nullConvert(request.getParameter("memberNo")));

		int result = sheet2monthService.deleteMoney(map);

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
	@RequestMapping(value="/sheet2/month/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		String[] memberId = request.getParameterValues("memberId");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < memberId.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("memberId", memberId[i]);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = sheet2monthService.deleteMoneyMulti(list);
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
