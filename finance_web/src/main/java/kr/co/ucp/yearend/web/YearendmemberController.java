package kr.co.ucp.yearend.web;

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
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.env.service.DeptService;
import kr.co.ucp.env.service.PositionService;
import kr.co.ucp.env.service.RegionService;
import kr.co.ucp.yearend.service.YearendmemberService;

@Controller
public class YearendmemberController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="yearendmemberService")
	YearendmemberService yearendmemberService;

	@Resource(name="positionService")
	PositionService positionService;

	@Resource(name="deptService")
	DeptService deptService;

	@Resource(name="regionService")
	RegionService regionService;

	// 리스트
	@RequestMapping(value="/yearend/member.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sidx"       , "POSITION_CODE");
		args.put("sord"       , "ASC");
		request.setAttribute("positionList", positionService.selectPosition(args));		// 직분

		args.put("sidx"       , "DEPT_CODE");
		request.setAttribute("deptList", deptService.selectDept(args));		// 교구

		args.put("sidx"       , "REGION_CODE");
		request.setAttribute("regionList", regionService.selectRegion(args));		// 구역
		
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "yearend/yearendmember";
	}

	// 조건검색
	@RequestMapping(value="/yearend/member/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("memberNo", (String) request.getParameter("memberNo"));
		args.put("memberName", (String) request.getParameter("memberName"));
		args.put("positionCode", (String) request.getParameter("positionCode"));
		args.put("useYn", (String) request.getParameter("useYn"));
		args.put("juminNoGb", (String) request.getParameter("juminNoGb"));
		args.put("agreeYn", (String) request.getParameter("agreeYn"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = yearendmemberService.selectMember(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

}
