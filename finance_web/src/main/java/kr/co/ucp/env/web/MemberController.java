package kr.co.ucp.env.web;

import java.net.URLDecoder;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.CmmService;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.utl.fcc.service.EgovDateUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.env.service.AcctgbService;
import kr.co.ucp.env.service.DeptService;
import kr.co.ucp.env.service.PositionService;
import kr.co.ucp.env.service.RegionService;
import kr.co.ucp.env.service.MemberService;

@Controller
public class MemberController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="cmmService")
	CmmService cmmService;
	
	@Resource(name="memberService")
	MemberService memberService;

	@Resource(name="positionService")
	PositionService positionService;

	@Resource(name="deptService")
	DeptService deptService;

	@Resource(name="regionService")
	RegionService regionService;

	// 신도 리스트
	@RequestMapping(value="/env/member.do")
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

		return "env/member";
	}

	// 신도 조건검색
	@RequestMapping(value="/env/member/list.json")
	@ResponseBody
	public Map<String, Object> selectMember(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("memberId"    , (String) request.getParameter("memberId"));
		args.put("memberNo"    , (String) request.getParameter("memberNo"));
		args.put("memberName"  , (String) request.getParameter("memberName"));
		args.put("positionCode", (String) request.getParameter("positionCode"));
		args.put("deptCode"    , (String) request.getParameter("deptCode"));
		args.put("regionCode"  , (String) request.getParameter("regionCode"));
		args.put("useYn"       , (String) request.getParameter("useYn"));
		args.put("memberQuery" , (String) request.getParameter("memberQuery"));		// 헌금자 검색용
		
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = memberService.selectMember(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 엑셀
	@RequestMapping(value="/env/member/excel.do")
	public void excel(ModelMap model, @RequestParam Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//logger.debug((String) request.getParameter("sidx"));
		//logger.debug(EgovStringUtil.nullConvert(map.get("sidx")));

		Map<String, String> args = new HashMap<String, String>();
		args.put("memberId"    , (String) request.getParameter("paraMemberId"));
		args.put("memberNo"    , (String) request.getParameter("paraMemberNo"));
		args.put("memberName"  , (String) request.getParameter("paraMemberName"));
		args.put("positionCode", EgovStringUtil.nullConvert(map.get("paraPositionCode")));
		args.put("deptCode"    , EgovStringUtil.nullConvert(map.get("paraDeptCode")));
		args.put("regionCode"  , EgovStringUtil.nullConvert(map.get("paraRegionCode")));
		args.put("useYn"       , EgovStringUtil.nullConvert(map.get("paraUseYn")));

		args.put("pageNo"	  , "1");
		args.put("rowsPerPage", "9999999");
		args.put("sidx"	, EgovStringUtil.nullConvert(map.get("sidx")));
		args.put("sord"	, EgovStringUtil.nullConvert(map.get("sord")));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = memberService.selectMember(args);
		
		model.put("title", "");	//model.put("title", "신도정보");
		model.put("fileName", "신도정보") ;
		model.put("titleKey", EgovStringUtil.nullConvert(map.get("titleKey")));
		model.put("titleHeader", URLDecoder.decode(EgovStringUtil.nullConvert(map.get("titleHeader")), "utf-8"));
		model.put("dataList", list);

        cmmService.buildExcelDocument(model, request, response);
	}

	// 신도 등록
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/member/insert.json")
	@ResponseBody
	public Map<String, Object> insertMember(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		//map.put("memberId", EgovStringUtil.nullConvert(request.getParameter("memberId")));
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
		map.put("useYn", EgovStringUtil.nullConvert(request.getParameter("useYn")));


		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int insertResult;
		try {
			insertResult = memberService.insertMember(map);
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

	// 신도 수정
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/member/update.json")
	@ResponseBody
	public Map<String, Object> updateMember(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("memberId"    , EgovStringUtil.nullConvert(request.getParameter("memberId")));
		map.put("memberNo"    , EgovStringUtil.nullConvert(request.getParameter("memberNo")));
		map.put("memberName"  , EgovStringUtil.nullConvert(request.getParameter("memberName")));
		map.put("sexType"     , EgovStringUtil.nullConvert(request.getParameter("sexType")));
		map.put("telNo"       , EgovStringUtil.nullConvert(request.getParameter("telNo")));
		map.put("hphoneNo"    , EgovStringUtil.nullConvert(request.getParameter("hphoneNo")));
		map.put("positionCode", EgovStringUtil.nullConvert(request.getParameter("positionCode")));
		map.put("deptCode"    , EgovStringUtil.nullConvert(request.getParameter("deptCode")));
		map.put("regionCode"  , EgovStringUtil.nullConvert(request.getParameter("regionCode")));
		map.put("addr"        , EgovStringUtil.nullConvert(request.getParameter("addr")));
		map.put("familyRemark", EgovStringUtil.nullConvert(request.getParameter("familyRemark")));
		map.put("useYn"       , EgovStringUtil.nullConvert(request.getParameter("useYn")));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int updateResult;
		try {
			updateResult = memberService.updateMember(map);
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

	// 신도 삭제
	@RequestMapping(value="/env/member/delete.json")
	@ResponseBody
	public Map<String, Object> deleteMember(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		
		map.put("memberId", EgovStringUtil.nullConvert(request.getParameter("memberId")));
		map.put("memberNo", EgovStringUtil.nullConvert(request.getParameter("memberNo")));

		int result = memberService.deleteMember(map);

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

	// 신도 다중삭제
//	@SuppressWarnings("unused")
//	@RequestMapping(value="/env/member/deleteMulti.json")
//	@ResponseBody
//	public Map<String, Object> deleteMemberMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
//
//		String[] memberId = request.getParameterValues("memberId");
//
//		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//
//		for (int i = 0; i < memberId.length; i++) {
//			Map<String, String> mapId = new HashMap<String, String>();
//			mapId.put("memberId", memberId[i]);
//
//			list.add(mapId);
//		}
//
//		Map<String, Object> mapRet = new HashMap<String, Object>();
//		int ret;
//		try {
//			ret = memberService.deleteMemberMulti(list);
//			mapRet.put("session", 1);
//			mapRet.put("msg", "삭제하였습니다.");
//		}
//		catch (DataIntegrityViolationException e) {
//			logger.error(e.getRootCause().getMessage());
//			mapRet.put("session", 0);
//			mapRet.put("msg", "처리중 오류가 발생했습니다.");
//		}
//		catch (UncategorizedSQLException e) {
//			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
//				mapRet.put("session", 0);
//				mapRet.put("msg", "처리중 에러가 발생했습니다.");
//			}
//			else {
//				logger.error(e.getRootCause().getMessage());
//				mapRet.put("session", 0);
//				mapRet.put("msg", "처리중 오류가 발생했습니다.");
//			}
//		}
//		catch (Exception e) {
//			logger.error(e.getMessage());
//			mapRet.put("session", 0);
//			mapRet.put("msg", "처리중 오류가 발생했습니다.");
//		}
//		return mapRet;
//	}

}
