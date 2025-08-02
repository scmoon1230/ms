/**
 * -----------------------------------------------------------------
 *
 * @Class Name : MpgInfoController.java
 * @Description :
 * @Version : 1.0 Copyright (c) 2017 by KR.CO.UCP All rights reserved.
 * @Modification Information
 *
 *               <pre>
 * -----------------------------------------------------------------
 * DATE           AUTHOR      DESCRIPTION
 * -----------------------------------------------------------------
 * 2016. 4. 26.   강인한    New Write
 * 2019. 9. 18.   SaintJuny 시큐어 코딩 처리
 * -----------------------------------------------------------------
 *               </pre>
 */
package kr.co.ucp.wrks.sstm.mpg.web;

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
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.mpg.service.ConnIpMpgService;

@Controller
public class ConnIpMpgController {
	static Logger logger = LoggerFactory.getLogger(ConnIpMpgController.class);

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	@Resource(name="connIpMpgService")
	private ConnIpMpgService connIpMpgService;

	// 리스트
	@RequestMapping(value="/wrks/sstm/mpg/connIpMpgList.do")
	public String connIpMpgList(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		
	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
		
		return "wrks/sstm/mpg/connIpMpgList";
	}

	@RequestMapping(value="/wrks/sstm/mpg/connIpMpgListData.json")
	@ResponseBody
	public Map<String, Object> connIpMpgListData(@ModelAttribute("loginVO") LoginVO loginVO, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String pageNo = request.getParameter("page");
		String rowsPerPage = request.getParameter("rows");
		String sidx = request.getParameter("sidx");
		String sord = request.getParameter("sord");

		// 페이징 조건
		String connIp = request.getParameter("connIp");
		String connDesc = request.getParameter("connDesc");

		// 검색조건
		Map<String, String> args = new HashMap<String, String>();
		args.put("connIp", connIp);
		args.put("connDesc", connDesc);
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		List<Map<String, String>> list = connIpMpgService.selectConnIpMpgInfoList(args);

		map.put("rows", list);
		map.put("page", pageNo);
		return map;
	}

	// 입력
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mpg/connIpMpgList/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("connIp", request.getParameter("connIp"));
		map.put("networkId", request.getParameter("networkId"));
		map.put("connDesc", request.getParameter("connDesc"));
		map.put("rgsUserId", sesUserId);
		map.put("updUserId", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int insertResult;
		try {
			insertResult = connIpMpgService.insertConnIpMpgInfo(map);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			if (e.getRootCause().getMessage().indexOf("UNIQUE constraint violation") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 코드입니다.");
			}
			else {
				logger.error("DataIntegrityViolationException getCause() ==> [{}]", e.getCause());
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
				logger.error("insert UncategorizedSQLException getCause() ==> [{}]", e.getCause());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error("insert Exception : [{}]", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	// 수정
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mpg/connIpMpgList/update.json")
	@ResponseBody
	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("connIp", request.getParameter("connIp"));
		map.put("networkId", request.getParameter("networkId"));
		map.put("connDesc", request.getParameter("connDesc"));
		map.put("rgsUserId", "admin");
		map.put("updUserId", "admin");

		Map<String, Object> mapRet = new HashMap<String, Object>();
		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int updateResult;
		try {
			updateResult = connIpMpgService.updateConnIpMpgInfo(map);
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
				logger.error("update UncategorizedSQLException : {}", e.getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error("update Exception : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	// 삭제
	@RequestMapping(value="/wrks/sstm/mpg/connIpMpgList/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("connIp", (String) request.getParameter("connIp"));
		int result = connIpMpgService.deleteConnIpMpgInfo(map);
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

	// 선택삭제
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mpg/connIpMpgList/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		String[] connIp = request.getParameterValues("connIp");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < connIp.length; i++) {
			Map<String, Object> mapId = new HashMap<String, Object>();
			mapId.put("connIp", connIp[i]);
			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = connIpMpgService.deleteConnIpMpgInfoMulti(list);
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
				logger.error("deleteMulti UncategorizedSQLException : {} ", e.getMessage());
				logger.error(e.getCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error("deleteMulti Exception : {} ", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}
}
