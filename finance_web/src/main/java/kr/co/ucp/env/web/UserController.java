package kr.co.ucp.env.web;

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
import kr.co.ucp.env.service.AcctgbService;
import kr.co.ucp.env.service.PositionService;
import kr.co.ucp.env.service.UserService;

@Controller
public class UserController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="userService")
	UserService userService;

	@Resource(name="positionService")
	PositionService positionService;

	@Resource(name="acctgbService")
	AcctgbService acctgbService;

	// 사용자 리스트
	@RequestMapping(value="/env/user.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sidx"       , "POSITION_CODE");
		args.put("sord"       , "ASC");
		request.setAttribute("positionList", positionService.selectPosition(args));		// 직분
		
		args.put("sidx"       , "ACCT_GB");
		request.setAttribute("acctgbList", acctgbService.selectAcctgb(args));		// 재정
		
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "env/user";
	}

	// 사용자 조건검색
	@RequestMapping(value="/env/user/list.json")
	@ResponseBody
	public Map<String, Object> selectUser(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("userName"    , (String) request.getParameter("userName"));
		args.put("positionCode", (String) request.getParameter("positionCode"));
		args.put("acctGb"      , (String) request.getParameter("acctGb"));
		args.put("userGb"      , (String) request.getParameter("userGb"));
		args.put("useYn"       , (String) request.getParameter("useYn"));
		args.put("stanYmd"     , (String) request.getParameter("stanYmd"));	// 당일 등록자 검색용
		
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = userService.selectUser(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 사용자 등록
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/user/insert.json")
	@ResponseBody
	public Map<String, Object> insertUser(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("userId", EgovStringUtil.nullConvert(request.getParameter("userId")));
		map.put("userName", EgovStringUtil.nullConvert(request.getParameter("userName")));
		map.put("userPwd", EgovStringUtil.nullConvert(request.getParameter("userPwd")));
		map.put("telNo", EgovStringUtil.nullConvert(request.getParameter("telNo")));
		map.put("userGb", EgovStringUtil.nullConvert(request.getParameter("userGb")));
		map.put("positionCode", EgovStringUtil.nullConvert(request.getParameter("positionCode")));
		map.put("acctGb", EgovStringUtil.nullConvert(request.getParameter("acctGb")));
		map.put("useYn", EgovStringUtil.nullConvert(request.getParameter("useYn")));


		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int insertResult;
		try {
			insertResult = userService.insertUser(map);
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

	// 사용자 수정
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/user/update.json")
	@ResponseBody
	public Map<String, Object> updateUser(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("userId", EgovStringUtil.nullConvert(request.getParameter("userId")));
		map.put("userName", EgovStringUtil.nullConvert(request.getParameter("userName")));
		map.put("userPwd", EgovStringUtil.nullConvert(request.getParameter("userPwd")));
		map.put("telNo", EgovStringUtil.nullConvert(request.getParameter("telNo")));
		map.put("userGb", EgovStringUtil.nullConvert(request.getParameter("userGb")));
		map.put("positionCode", EgovStringUtil.nullConvert(request.getParameter("positionCode")));
		map.put("acctGb", EgovStringUtil.nullConvert(request.getParameter("acctGb")));
		map.put("useYn", EgovStringUtil.nullConvert(request.getParameter("useYn")));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int updateResult;
		try {
			updateResult = userService.updateUser(map);
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

	// 사용자 삭제
	@RequestMapping(value="/env/user/delete.json")
	@ResponseBody
	public Map<String, Object> deleteUser(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		
		map.put("userId", EgovStringUtil.nullConvert(request.getParameter("userId")));

		int result = userService.deleteUser(map);

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

	// 사용자 다중삭제
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/user/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteUserMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		String[] userId = request.getParameterValues("userId");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < userId.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("userId", userId[i]);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = userService.deleteUserMulti(list);
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

	// 사용자아이디 중복 확인
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/usr/checkUserId.json")
	@ResponseBody
	public Map<String, Object> checkUserId(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception
	{
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("userId" , (String)request.getParameter("userId"));

		int ret = userService.selectUserIdCnt(map);
		
		Map<String, Object> map_ret = new HashMap<String, Object>();
		map_ret.put("session", 1);
		map_ret.put("msg", ret);

		return map_ret;
	}

}
