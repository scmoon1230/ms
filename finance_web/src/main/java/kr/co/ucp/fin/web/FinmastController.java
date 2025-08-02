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
import kr.co.ucp.fin.service.FinmastService;

@Controller
public class FinmastController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="finmastService")
	FinmastService finmastService;

	@Resource(name="positionService")
	PositionService positionService;

	@Resource(name="acctgbService")
	AcctgbService acctgbService;

	// 리스트
	@RequestMapping(value="/fin/mast.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

        String toDate = EgovDateUtil.getToday();
        request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
        
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sidx"       , "ACCT_GB");
		args.put("sord"       , "ASC");
		request.setAttribute("acctgbList", acctgbService.selectAcctgb(args));		// 재정
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "fin/finmast";
	}

	// 조건검색
	@RequestMapping(value="/fin/mast/list.json")
	@ResponseBody
	public Map<String, Object> selectFinmast(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"  , (String) request.getParameter("stanYmd"));
		args.put("acctGb"   , (String) request.getParameter("acctGb"));
		args.put("assetGb"  , (String) request.getParameter("assetGb"));
		args.put("assetName", (String) request.getParameter("assetName"));
		args.put("accountNo", (String) request.getParameter("accountNo"));
		//args.put("useYn"    , (String) request.getParameter("useYn"));
		args.put("showYn", (String) request.getParameter("showYn"));
		
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = finmastService.selectFinmastList(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

}
