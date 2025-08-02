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
import kr.co.ucp.env.service.PositionService;

@Controller
public class PositionController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="positionService")
	PositionService positionService;

	// 직분코드 리스트
	@RequestMapping(value="/env/position.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "env/position";
	}

	// 직분코드 조건검색
	@RequestMapping(value="/env/position/list.json")
	@ResponseBody
	public Map<String, Object> selectPosition(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("positionName", (String) request.getParameter("positionName"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));

		List<Map<String, String>> list = positionService.selectPosition(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 직분코드 등록
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/position/insert.json")
	@ResponseBody
	public Map<String, Object> insertPosition(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("positionCode", EgovStringUtil.nullConvert(request.getParameter("positionCode")));
		map.put("positionName", EgovStringUtil.nullConvert(request.getParameter("positionName")));
		map.put("sortOrder", EgovStringUtil.nullConvert(request.getParameter("sortOrder")));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int insertResult;
		try {
			insertResult = positionService.insertPosition(map);
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

	// 직분코드 수정
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/position/update.json")
	@ResponseBody
	public Map<String, Object> updatePosition(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("positionCode", EgovStringUtil.nullConvert(request.getParameter("positionCode")));
		map.put("positionName", EgovStringUtil.nullConvert(request.getParameter("positionName")));
		map.put("sortOrder", EgovStringUtil.nullConvert(request.getParameter("sortOrder")));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int updateResult;
		try {
			updateResult = positionService.updatePosition(map);
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

	// 직분코드 삭제
	@RequestMapping(value="/env/position/delete.json")
	@ResponseBody
	public Map<String, Object> deletePosition(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		
		map.put("positionCode", EgovStringUtil.nullConvert(request.getParameter("positionCode")));

		int result = positionService.deletePosition(map);

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

	// 직분코드 다중삭제
	@SuppressWarnings("unused")
	@RequestMapping(value="/env/position/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deletePositionMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		String[] positionCode = request.getParameterValues("positionCode");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < positionCode.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("positionCode", positionCode[i]);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = positionService.deletePositionMulti(list);
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
