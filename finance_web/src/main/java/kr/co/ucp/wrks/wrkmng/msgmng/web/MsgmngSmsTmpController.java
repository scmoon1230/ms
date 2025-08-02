package kr.co.ucp.wrks.wrkmng.msgmng.web;

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
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.wrkmng.msgmng.service.MsgmngSmsTmpService;

/**
 *
 * ----------------------------------------------------------
 * @Class Name : MsgmngSmsTmpController.java
 * @Description : SMS 임시
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA		   AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015. 11. 4.   pjm	   최초작성
 *
 * ----------------------------------------------------------
 *
 */
@Controller
public class MsgmngSmsTmpController {
	static Logger logger = LoggerFactory.getLogger(MsgmngSmsTmpController.class);

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	@Resource(name="msgmngSmsTmpService")
	private MsgmngSmsTmpService msgmngSmsTmpService;

	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	/** TRACE */
	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	/**
	 * SMS 임시 목록화면
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/wrks/wrkmng/msgmng/smstmp.do")
	public String smsTmp(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();

		/*SMS 전송유형 코드 조회*/
		args.put("cdGrpId", "SMS_TRMS_TY");
		args.put("cdTy", "C");
		args.put("orderBy", "ORDER BY CD_NM_KO ASC");
		request.setAttribute("smsTrmsTyList", codeCmcdService.grpList(args));

		/*SMS 공지유형 코드 조회*/
		args.clear();
		args.put("cdGrpId", "NTFCT_TY");
		args.put("cdTy", "C");
		args.put("orderBy", "ORDER BY CD_NM_KO ASC");
		request.setAttribute("ntfctTyList", codeCmcdService.grpList(args));

		request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
		/* row per page 설정 끝 */

		/*현재날짜 조회*/
		request.setAttribute("currentDay", codeCmcdService.getCurrentDay());

		return "wrks/wrkmng/msgmng/sms_tmp";
	}

	/**
	 *  SMS 임시 조건 검색 목록
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/wrks/wrkmng/msgmng/smstmp/list.json")
	@ResponseBody
	public Map<String, Object> list(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception {

		String smsTrmsTyCd = request.getParameter("smsTrmsTyCd");
		String smsNtfctTyCd = request.getParameter("smsNtfctTyCd");
		String smsTrmsNm = request.getParameter("smsTrmsNm");
		String smsTrmsConts = request.getParameter("smsTrmsConts");
		String strDateStart = request.getParameter("strDateStart");
		String strDateEnd = request.getParameter("strDateEnd");

		String pageNo = request.getParameter("page");
		String rowsPerPage = request.getParameter("rows");
		String sidx = request.getParameter("sidx");
		String sord = request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();

		args.put("smsTrmsTyCd", smsTrmsTyCd);
		args.put("smsNtfctTyCd", smsNtfctTyCd);
		args.put("smsTrmsNm", smsTrmsNm);
		args.put("smsTrmsConts", smsTrmsConts);
		args.put("strDateStart", strDateStart);
		args.put("strDateEnd", strDateEnd);

		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = msgmngSmsTmpService.list(args);
		map.put("page", pageNo);
		map.put("rows", resultRows);

		return map;
	}

	/**
	 * SMS 임시 수신자 목록
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/wrks/wrkmng/msgmng/smstmp/list_rcv.json")
	@ResponseBody
	public Map<String, Object> listRcv(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception{

		Map<String, Object> map = new HashMap<String, Object>();
		String smsId = request.getParameter("smsId");
		String sidx = request.getParameter("sidx");
		String sord = request.getParameter("sord");
		Map<String, String> args = new HashMap<String, String>();
		args.put("smsId", smsId);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		List<Map<String, String>> list = msgmngSmsTmpService.list_rcv(args);
		map.put("rows", list);
		return map;
	}

	/**
	 * SMS 임시 등록
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/wrks/wrkmng/msgmng/smstmp/insert.json")
	@ResponseBody
	public Map<String, Object> insert(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception{

		List<Map<String, String>> rcvList = new ArrayList<Map<String, String>>();

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		String smsTrmsTyCd  = "S";
		String smsTrmsConts = URLDecoder.decode(request.getParameter("smsTrmsConts"), "UTF-8");
		String smsNtfctTyCd = URLDecoder.decode(request.getParameter("smsNtfctTyCd"), "UTF-8");
		String sesUserId	= lgnVO.getUserId();
		String smsTrmsNm	= lgnVO.getUserNmKo();
		String smsTrmsSttus = URLDecoder.decode(request.getParameter("smsTrmsSttus"), "UTF-8");

		// SMS 관리
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("smsTrmsTyCd", smsTrmsTyCd);
		map.put("smsTrmsConts", smsTrmsConts);
		map.put("smsNtfctTyCd", smsNtfctTyCd);
		map.put("smsTrmsId", sesUserId);
		map.put("smsTrmsNm", smsTrmsNm);
		map.put("smsTrmsSttus", smsTrmsSttus);
		map.put("rgsUserId", sesUserId);
		map.put("updUserId", sesUserId);

		// SMS id얻기
		String smsId = msgmngSmsTmpService.select_sms_sms_id();
		map.put("smsId", smsId);

		// SMS 수신정보
		String[] smsRcvIds = request.getParameterValues("smsRcvId");
		String[] smsRcvNms = request.getParameterValues("smsRcvNm");
		String[] smsRcvMoblNos = request.getParameterValues("smsRcvMoblNo");

		for(int i = 0; i< smsRcvIds.length; i++){
			Map<String, String> mapItemId = new HashMap<String, String>();

			mapItemId.put("smsId", smsId);
			mapItemId.put("smsRcvId", URLDecoder.decode(smsRcvIds[i], "UTF-8"));
			mapItemId.put("smsRcvNm", URLDecoder.decode(smsRcvNms[i], "UTF-8"));
			mapItemId.put("smsRcvMoblNo", URLDecoder.decode(smsRcvMoblNos[i], "UTF-8"));
			mapItemId.put("smsTrmsSttus", smsTrmsSttus);

			rcvList.add(mapItemId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();

		// 등록처리
		try{
			int insertResult = 0;
			try {
				insertResult = msgmngSmsTmpService.insert(map, rcvList);
			} catch(DataIntegrityViolationException e) {
				logger.error("insert DataIntegrityViolationException : {}", e.getMessage());
			} catch(UncategorizedSQLException e) {
				//dup error
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "아이디 확인후 다시 등록해주십시오.");
					return mapRet;
				}
				else {
					logger.error("insert UncategorizedSQLException : {}", e.getMessage());
				}
			}catch(Exception e){
				logger.error("insert Exception : {}", e.getMessage());
			}

			if(insertResult == 0){
				mapRet.put("session", 0);
				mapRet.put("msg", "등록에 실패하였습니다.");
				return mapRet;
			}
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		}
		catch(Exception e) {
  			logger.error("Exception  : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "알수없는 에러!!!");
		}

		return mapRet;
	}

	/**
	 * SMS 임시 수정
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/wrks/wrkmng/msgmng/smstmp/update.json")
	@ResponseBody
	public Map<String, Object> update(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception {

		List<Map<String, String>> rcvList = new ArrayList<Map<String, String>>();

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		String smsId = URLDecoder.decode(request.getParameter("smsId"), "UTF-8");
		String smsTrmsConts = URLDecoder.decode(request.getParameter("smsTrmsConts"), "UTF-8");
		String smsNtfctTyCd = URLDecoder.decode(request.getParameter("smsNtfctTyCd"), "UTF-8");
		String smsTrmsSttus = URLDecoder.decode(request.getParameter("smsTrmsSttus"), "UTF-8");
		String sesUserId	= lgnVO.getUserId();

		// SMS 임시
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("smsId", smsId);
		map.put("smsTrmsConts", smsTrmsConts);
		map.put("smsNtfctTyCd", smsNtfctTyCd);
		map.put("updUserId", sesUserId);

		// SMS 임시 수신정보
		String[] smsRcvIds = request.getParameterValues("smsRcvId");
		String[] smsRcvNms = request.getParameterValues("smsRcvNm");
		String[] smsRcvMoblNos = request.getParameterValues("smsRcvMoblNo");

		for(int i = 0; i< smsRcvIds.length; i++){
			Map<String, String> mapItemId = new HashMap<String, String>();

			mapItemId.put("isSmsIdExist", "Y");
			mapItemId.put("smsId", smsId);
			mapItemId.put("smsRcvId", URLDecoder.decode(smsRcvIds[i], "UTF-8"));
			mapItemId.put("smsRcvNm", URLDecoder.decode(smsRcvNms[i], "UTF-8"));
			mapItemId.put("smsRcvMoblNo", URLDecoder.decode(smsRcvMoblNos[i], "UTF-8"));
			mapItemId.put("smsTrmsSttus", smsTrmsSttus);

			rcvList.add(mapItemId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();

		// 수정처리
		try{
			int updateResult = 0;
			try {
				updateResult = msgmngSmsTmpService.update(map, rcvList);
			} catch(DataIntegrityViolationException e) {
				logger.error("update DataIntegrityViolationException : {}", e.getMessage());
			} catch(UncategorizedSQLException e) {
				//dup error
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "아이디 확인후 다시 등록해주십시오.");
					return mapRet;
				}
				else {
					logger.error("update UncategorizedSQLException : {}", e.getMessage());
				}
			}catch(Exception e){
				logger.error("update Exception : {}", e.getMessage());
			}

			if(updateResult == 0){
				mapRet.put("session", 0);
				mapRet.put("msg", "수정에 실패하였습니다.");
				return mapRet;
			}
			mapRet.put("session", 1);
			mapRet.put("msg", "수정하였습니다.");
		}
		catch(Exception e) {
  			logger.error("Exception  : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "알수없는 에러!!!");
		}

		return mapRet;
	}

	/**
	 * SMS 임시 삭제
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/wrks/wrkmng/msgmng/smstmp/delete.json")
	@ResponseBody
	public Map<String, Object> delete(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception{

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("smsId", URLDecoder.decode(request.getParameter("smsId"), "UTF-8"));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		try{
			try {
				msgmngSmsTmpService.delete(args);
			} catch(DataIntegrityViolationException e) {
				logger.error("delete DataIntegrityViolationException : {}", e.getMessage());
			} catch(UncategorizedSQLException e) {
				//dup error
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "삭제처리중 오류가 발생했습니다.잠시후 다시 시도해주세요");
					return mapRet;
				}
				else {
					logger.error("delete UncategorizedSQLException : {}", e.getMessage());
				}
			}catch(Exception e){
				logger.error("delete Exception : {}", e.getMessage());
			}

			mapRet.put("session", 1);
			mapRet.put("msg", "삭제하였습니다.");
		}
		catch(Exception e) {
  			logger.error("Exception  : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "알수없는 에러입니다.");
		}
		return mapRet;
	}

	/**
	 * SMS 전송 등록
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/wrks/wrkmng/msgmng/smstmp/insertRealSms.json")
	@ResponseBody
	public Map<String, Object> insertRealSms(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception{

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		String smsTrmsTyCd  = "S";
		String smsTrmsConts = URLDecoder.decode(request.getParameter("smsTrmsConts"), "UTF-8");
		String smsNtfctTyCd = URLDecoder.decode(request.getParameter("smsNtfctTyCd"), "UTF-8");
		String sesUserId	= lgnVO.getUserId();
		String smsTrmsNm	= lgnVO.getUserNmKo();
		String moblNo	   = lgnVO.getMoblNo();
		String smsTrmsSttus = URLDecoder.decode(request.getParameter("smsTrmsSttus"), "UTF-8");

		// SMS 관리
		Map<String, Object> map = new HashMap<String, Object>();

		String smsId = msgmngSmsTmpService.select_sms_sms_id();

		map.put("smsId", smsId);
		map.put("smsTrmsTyCd", smsTrmsTyCd);
		map.put("smsTrmsConts", smsTrmsConts);
		map.put("smsNtfctTyCd", smsNtfctTyCd);
		map.put("smsTrmsId", sesUserId);
		map.put("smsTrmsNm", smsTrmsNm);
		map.put("smsTrmsSttus", "");
		map.put("rgsUserId", sesUserId);
		map.put("updUserId", sesUserId);

		// SMS 수신정보
		String[] smsRcvIds = request.getParameterValues("smsRcvId");
		String[] smsRcvNms = request.getParameterValues("smsRcvNm");
		String[] smsRcvMoblNos = request.getParameterValues("smsRcvMoblNo");
		List<Map<String, String>> rcvList = new ArrayList<Map<String, String>>();

		for(int i = 0; i< smsRcvIds.length; i++){
			Map<String, String> map_item_id = new HashMap<String, String>();

			map_item_id.put("smsId", smsId);
			map_item_id.put("smsRcvId", URLDecoder.decode(smsRcvIds[i], "UTF-8"));
			map_item_id.put("smsRcvNm", URLDecoder.decode(smsRcvNms[i], "UTF-8"));
			map_item_id.put("smsRcvMoblNo", URLDecoder.decode(smsRcvMoblNos[i], "UTF-8"));
			map_item_id.put("smsTrmsSttus", smsTrmsSttus);

			rcvList.add(map_item_id);
		}

		Map<String, Object> map_ret = new HashMap<String, Object>();

		// 데이터입력
		try{
			int insertResult = 0;
			try {
				// updateSmsSend <-- 기존에 주석처리를 해놔서 insert 함수와 insertSmsMng가 동일함  - 확인해야함
				insertResult = msgmngSmsTmpService.insertSmsMng(map, rcvList);
			} catch(DataIntegrityViolationException e) {
				logger.error("insertRealSms DataIntegrityViolationException : {}", e.getMessage());
			} catch(UncategorizedSQLException e) {
				//dup error
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					map_ret.put("session", 0);
					map_ret.put("msg", "아이디 확인후 다시 등록해주십시오.");
					return map_ret;
				}
				else {
					logger.error("insertRealSms UncategorizedSQLException : {}", e.getMessage());
				}
			}catch(Exception e){
				logger.error("insertRealSms Exception : {}", e.getMessage());
			}

			if(insertResult == 0){
				map_ret.put("session", 0);
				map_ret.put("msg", "등록에 실패하였습니다.");
				return map_ret;
			}
			map_ret.put("session", 1);
			map_ret.put("msg", "저장하였습니다.");
		}
		catch(Exception e) {
  			logger.error("Exception  : {}", e.getMessage());
			map_ret.put("session", 0);
			map_ret.put("msg", "알수없는 에러!!!"); // TODO: SQL오류 메세지 처리
		}

		//SMS REAL 데이터입력
		try{
			int insertResult = 0;
			try {
				// 2019.06.20 추후 수정예정
//				insertResult = smsSend(request);
//				if(insertResult > 0) {
//					insertResult = msgmngSmsTmpService.updateSmsStatusOk(map);
//				}
//				insertResult = smsSend(request);
//				if(insertResult > 0) {
					insertResult = msgmngSmsTmpService.updateSmsStatusOk(map);
 //		   	}
			} catch(DataIntegrityViolationException e) {
				System.out.println(e.getRootCause());
			} catch(UncategorizedSQLException e) {
				//dup error
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					map_ret.put("session", 0);
					map_ret.put("msg", "아이디 확인후 다시 등록해주십시오.");
					return map_ret;
				}
				else {
					System.out.println(e.getCause());
				}
			}catch(Exception e){
				logger.error("insertRealSms Exception : {}", e.getMessage());
			}

			if(insertResult == 0){
				map_ret.put("session", 0);
				map_ret.put("msg", "등록에 실패하였습니다.");
				return map_ret;
			}
			map_ret.put("session", 1);
			map_ret.put("msg", "저장하였습니다.");
		}
		catch(Exception e) {
  			logger.error("Exception  : {}", e.getMessage());
			map_ret.put("session", 0);
			map_ret.put("msg", "알수없는 에러!!!"); // TODO: SQL오류 메세지 처리
		}
		return map_ret;
	}

	/**
	 * SMS 전송 등록
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/wrks/wrkmng/msgmng/smstmp/updateRealSms.json")
	@ResponseBody
	public Map<String, Object> updateRealSms(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception{

		List<Map<String, String>> rcvList = new ArrayList<Map<String, String>>();

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		String smsId = URLDecoder.decode(request.getParameter("smsId"), "UTF-8");
		String smsTrmsConts = URLDecoder.decode(request.getParameter("smsTrmsConts"), "UTF-8");
		String smsNtfctTyCd = URLDecoder.decode(request.getParameter("smsNtfctTyCd"), "UTF-8");
		String smsTrmsSttus = URLDecoder.decode(request.getParameter("smsTrmsSttus"), "UTF-8");
		String sesUserId	= lgnVO.getUserId();

		// SMS 임시
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("smsId", smsId);
		map.put("smsTrmsConts", smsTrmsConts);
		map.put("smsNtfctTyCd", smsNtfctTyCd);
		map.put("updUserId", sesUserId);

		// SMS 임시 수신정보
		String[] smsRcvIds = request.getParameterValues("smsRcvId");
		String[] smsRcvNms = request.getParameterValues("smsRcvNm");
		String[] smsRcvMoblNos = request.getParameterValues("smsRcvMoblNo");

		for(int i = 0; i< smsRcvIds.length; i++){
			Map<String, String> mapItemId = new HashMap<String, String>();

			mapItemId.put("isSmsIdExist", "Y");
			mapItemId.put("smsId", smsId);
			mapItemId.put("smsRcvId", URLDecoder.decode(smsRcvIds[i], "UTF-8"));
			mapItemId.put("smsRcvNm", URLDecoder.decode(smsRcvNms[i], "UTF-8"));
			mapItemId.put("smsRcvMoblNo", URLDecoder.decode(smsRcvMoblNos[i], "UTF-8"));
			mapItemId.put("smsTrmsSttus", smsTrmsSttus);

			rcvList.add(mapItemId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();

		// 수정처리
		try{
			int updateResult = 0;
			try {
				updateResult = msgmngSmsTmpService.update(map, rcvList);
			} catch(DataIntegrityViolationException e) {
				logger.error("updateRealSms DataIntegrityViolationException : {}", e.getMessage());
			} catch(UncategorizedSQLException e) {
				//dup error
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "아이디 확인후 다시 등록해주십시오.");
					return mapRet;
				}
				else {
					logger.error("updateRealSms UncategorizedSQLException : {}", e.getMessage());
				}
			}catch(Exception e){
				logger.error("updateRealSms Exception : {}", e.getMessage());
			}

			if(updateResult == 0){
				mapRet.put("session", 0);
				mapRet.put("msg", "수정에 실패하였습니다.");
				return mapRet;
			}
			mapRet.put("session", 1);
			mapRet.put("msg", "수정하였습니다.");
		}
		catch(Exception e) {
  			logger.error("Exception  : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "알수없는 에러!!!");
			return mapRet;
		}

		//SMS REAL 데이터입력
		try{
			int insertResult = 0;
			try {
				insertResult = smsSend(request);
				if(insertResult > 0) {
					insertResult = msgmngSmsTmpService.updateSmsStatusOk(map);
				}
			} catch(DataIntegrityViolationException e) {
				logger.error("updateRealSms DataIntegrityViolationException : {}", e.getMessage());
			} catch(UncategorizedSQLException e) {
				//dup error
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "아이디 확인후 다시 등록해주십시오.");
					return mapRet;
				}
				else {
					logger.error("updateRealSms UncategorizedSQLException : {}", e.getMessage());
				}
			}catch(Exception e){
				logger.error("updateRealSms Exception : {}", e.getMessage());
			}

			if(insertResult == 0){
				mapRet.put("session", 0);
				mapRet.put("msg", "등록에 실패하였습니다.");
				return mapRet;
			}
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		}
		catch(Exception e) {
  			logger.error("Exception  : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "알수없는 에러!!!"); // TODO: SQL오류 메세지 처리
		}
		return mapRet;
	}

	/**
	 * SMS 전송 등록
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/wrks/wrkmng/msgmng/smstmp/smsSend.json")
	@ResponseBody
	public Map<String, Object> smsSend(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception{

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		String smsId = URLDecoder.decode(request.getParameter("smsId"), "UTF-8");
		String sesUserId	= lgnVO.getUserId();

		// SMS 임시
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("smsId", smsId);
		map.put("updUserId", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		// 데이터입력
		try{
			int insertResult = 0;
			try {
				// 2019.06.20 추후 수정예정
//				insertResult = smsSend(request);
//				if(insertResult > 0) {
//					insertResult = msgmngSmsTmpService.updateSmsStatusOk(map);
//				}
//				insertResult = smsSend(request);
//				if(insertResult > 0) {
					insertResult = msgmngSmsTmpService.updateSmsStatusOk(map);
 //		   	}
			} catch(DataIntegrityViolationException e) {
				logger.error("update DataIntegrityViolationException : {}", e.getMessage());
			} catch(UncategorizedSQLException e) {
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
		  	  		mapRet.put("session", 0);
		  	  		mapRet.put("msg", "아이디 확인후 다시 등록해주십시오.");
		  	  		return mapRet;
				} else {
					logger.error("update UncategorizedSQLException : {}", e.getMessage());
				}
			}catch(Exception e){
				logger.error("update Exception : {}", e.getMessage());
			}

			if(insertResult == 0){
				mapRet.put("session", 0);
				mapRet.put("msg", "SMS전송에 실패하였습니다.");
				return mapRet;
			}
			mapRet.put("session", 1);
			mapRet.put("msg", "SMS를 전송하였습니다.");
		}
		catch(Exception e) {
  			logger.error("Exception  : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "알수없는 에러!!!"); // TODO: SQL오류 메세지 처리
		}

		return mapRet;
	}

	/*
	 * 사용자 그룹리스트 조회
	 */
	@RequestMapping(value="/wrks/wrkmng/msgmng/smstmp/grpList.json")
	@ResponseBody
	public Map<String, Object> grpList(@ModelAttribute("loginVO") LoginVO loginVO, ModelMap model
			, HttpServletRequest request ,HttpServletResponse response) throws Exception{

		Map<String, Object> map = new HashMap<String, Object>();

		/*검색조건*/
		String checkRcvId = request.getParameter("checkRcvId");

		Map<String, String> args = new HashMap<String, String>();

		args.put("CHECK_RCV_ID", checkRcvId);

		List<Map<String, String>> list = msgmngSmsTmpService.grpList(args);

		map.put("rows", list);

		return map;
	}

	public int smsSend(HttpServletRequest request) throws Exception
	{
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		// 전송내용
		String smsTrmsConts = URLDecoder.decode(request.getParameter("smsTrmsConts"), "utf-8");
		// 발신자 번호
		String moblNo 		= lgnVO.getMoblNo();

		// 수신자번호
		String[] smsRcvMoblNos = request.getParameterValues("smsRcvMoblNo");
		List<String> rcvList = new ArrayList<String>();
		for(int i = 0; i< smsRcvMoblNos.length; i++){
			logger.debug("moblNo  : {}", URLDecoder.decode(smsRcvMoblNos[i], "utf-8"));
			rcvList.add(URLDecoder.decode(smsRcvMoblNos[i], "utf-8"));
		}
		// 수신자명
		String[] arySmsRcvNm = request.getParameterValues("smsRcvNm");
		List<String> listRcvNm = new ArrayList<String>();
		for(int i = 0; i< arySmsRcvNm.length; i++){
			logger.debug("RcvNm  : {}", URLDecoder.decode(arySmsRcvNm[i], "utf-8"));
			listRcvNm.add(URLDecoder.decode(arySmsRcvNm[i], "utf-8"));
		}

		// 전송 아답터 정보 scmp_adpt_sms
		String socketOpt = prprtsService.getString("LINK_ADAPT_SMS").trim();

		return CommonUtil.smsSend(socketOpt, moblNo, smsTrmsConts, rcvList, listRcvNm);
	}
}
