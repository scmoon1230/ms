package kr.co.ucp.wrks.sstm.code.web;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.interceptor.AuthenticInterceptor;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.code.service.CodeDstService;

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
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 지구코드현황을 관리
 * @author		대전도안 김정원
 * @version		1.00	2014-01-25
 * @since			JDK 1.7.0_45(x64)
 * @revision
 * /
/**
 * ----------------------------------------------------------
 * @Class Name : CodeDstController
 * @Description : 지구코드
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA		   AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-01-22   김정원	   최초작성
 *
 * ----------------------------------------------------------
 * */
@Controller
public class CodeDstController {
	static Logger logger = LoggerFactory.getLogger(AuthenticInterceptor.class);

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	/** TRACE */
	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Resource(name="codeDstService")
	private CodeDstService codeDstService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;


	// 지구코드그룹 리스트
	@RequestMapping(value="/wrks/sstm/code/dst.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();

		/*시군구 설정*/
//		args.clear();
//		args.put("orderBy", "ORDER BY SIGUNGU_CD");
//		request.setAttribute("sggList", codeDstService.sggList(args));

		request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/* 사용유형 */

		/*센터코드 설정*/
//		args.clear();
//		args.put("cdGrpId", "UCP_ID");
//		args.put("cdTy", "C");
//		args.put("orderBy", "ORDER BY CD_ID DESC");
//		request.setAttribute("ctrGrpList", codeCmcdService.grpList(args));

		request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
		/* row per page 설정 끝 */

		return "wrks/sstm/code/dst";
	}


	// 지구코드그룹 조건검색
	@RequestMapping(value="/wrks/sstm/code/dst/list.json")
  	@ResponseBody
  	public Map<String, Object> getList(ModelMap model ,HttpServletRequest request ,HttpServletResponse response ) throws Exception {
		Map<String, String> args = new HashMap<String, String>();
		args.put("dstrtCd"    , (String)request.getParameter("dstrtCd"));
		args.put("dstrtNm"    , (String)request.getParameter("dstrtNm"));
		args.put("useTyCd"    , (String)request.getParameter("useTyCd"));
		args.put("pageNo"     , (String)request.getParameter("page")   );
		args.put("rowsPerPage", (String)request.getParameter("rows")   );
		args.put("sidx"	      , (String)request.getParameter("sidx")   );
		args.put("sord"	      , (String)request.getParameter("sord")   );

		List<Map<String, String>> resultRows = codeDstService.codeDstList(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", (String)request.getParameter("page"));
		map.put("rows", resultRows);

		return map;
	}


	// 지구코드그룹 입력
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/dst/insert.json")
  	@ResponseBody
  	public Map<String, Object> insert(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

  		Map<String, Object> map = new HashMap<String, Object>();
  		map.put("dstrtCd"      , (String)request.getParameter("dstrtCd")      );
  		map.put("dstrtNm"      , (String)request.getParameter("dstrtNm")      );
  		map.put("dstrtTy"      , (String)request.getParameter("dstrtTy")      );
  		map.put("repTelNo"     , (String)request.getParameter("repTelNo")     );
  		map.put("linkUrl"	   , (String)request.getParameter("linkUrl")	  );
  		map.put("vrsWebrtcAddr", (String)request.getParameter("vrsWebrtcAddr"));
  		map.put("playbackSpeed", (String)request.getParameter("playbackSpeed"));
  		map.put("useTyCd"      , (String)request.getParameter("useTyCd")	  );
  		map.put("dstrtDscrt"   , (String)request.getParameter("dstrtDscrt")   );
  		map.put("rgsUserId"    , lgnVO.getUserId()                            );
  		map.put("updUserId"    , lgnVO.getUserId()                            );

  		Map<String, Object> mapRet = new HashMap<String, Object>();

		if(!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
	  		return mapRet;
		}

		try{
  			int ret;
			try {
	  			ret = codeDstService.insertCodeDst(map);
			} catch(DataIntegrityViolationException e) {
				if (e.getRootCause().getMessage().indexOf("UNIQUE constraint violation") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "이미 등록된 코드입니다.");
					return mapRet;
				}
				else {
					logger.error(e.getRootCause().getMessage());
				}
				
			} catch(UncategorizedSQLException e) {
				//dup error
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "시군구코드 확인후 등록해주세요.");
		  	  		return mapRet;
				}
				else {
					logger.error(e.getRootCause().getMessage());
				}
			}catch(Exception e){
				logger.error(e.getMessage());
			}

			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
  		}
  		catch(Exception e){
  			mapRet.put("session", 0);
  			mapRet.put("msg", "알수없는 에러입니다."); // TODO: SQL오류 메세지 처리
  		}
  		return mapRet;
	}


	// 지구코드그룹 수정
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/dst/update.json")
  	@ResponseBody
  	public Map<String, Object> update(ModelMap model, HttpServletRequest request ,HttpServletResponse response)
  			throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

  		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dstrtCd"      , (String)request.getParameter("dstrtCd")      );
  		map.put("dstrtNm"      , (String)request.getParameter("dstrtNm")      );
  		map.put("dstrtTy"      , (String)request.getParameter("dstrtTy")      );
  		map.put("repTelNo"	   , (String)request.getParameter("repTelNo")     );
  		map.put("linkUrl"	   , (String)request.getParameter("linkUrl")      );
  		map.put("vrsWebrtcAddr", (String)request.getParameter("vrsWebrtcAddr"));
  		map.put("playbackSpeed", (String)request.getParameter("playbackSpeed"));
  		map.put("useTyCd"      , (String)request.getParameter("useTyCd")      );
  		map.put("dstrtDscrt"   , (String)request.getParameter("dstrtDscrt")   );
	//	map.put("DSTRT_CD_BAK"   , (String)request.getParameter("dstrtCdBak") );
  		map.put("updUserId"	, lgnVO.getUserId()							      );

		Map<String, Object> mapRet = new HashMap<String, Object>();
		
		if(!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
	  		return mapRet;
		}

		try{
  			int ret;
			try {
	  			ret = codeDstService.updateCodeDst(map);
			} catch(DataIntegrityViolationException e) {
				logger.error(e.getRootCause().getMessage());
			} catch(UncategorizedSQLException e) {
				//dup error
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "지구코드 확인후 업데이트해주세요.");
		  	  		return mapRet;
				}
				else {
					logger.error(e.getRootCause().getMessage());
				}
			}catch(Exception e){
				logger.error(e.getMessage());
			}

			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
  		}
  		catch(Exception e){
  			mapRet.put("session", 0);
  			mapRet.put("msg", "알수없는 에러입니다."); // TODO: SQL오류 메세지 처리
  		}
  		return mapRet;
	}


	// 지구코드그룹 삭제
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/dst/delete.json")
  	@ResponseBody
  	public Map<String, Object> delete(ModelMap model, HttpServletRequest request ,HttpServletResponse response)
  			throws Exception {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

  		Map<String, String> map = new HashMap<String, String>();
  		map.put("dstrtCd"  , URLDecoder.decode(request.getParameter("dstrtCd"), "UTF-8"));
  		map.put("updUserId", lgnVO.getUserId()                                          );

		Map<String, Object> mapRet = new HashMap<String, Object>();

		try{
  			int ret;
			try {
	  			ret = codeDstService.deleteCodeDst(map);
			} catch(DataIntegrityViolationException e) {
				logger.error(e.getRootCause().getMessage());
			} catch(UncategorizedSQLException e) {
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "삭제처리중 오류가 발생했습니다.잠시후 다시 시도해주세요");
		  	  		return mapRet;
				}
				else {
					logger.error(e.getRootCause().getMessage());
				}
			}catch(Exception e){
				logger.error(e.getMessage());
			}

			mapRet.put("session", 1);
			mapRet.put("msg", "삭제하였습니다.");
  		}
  		catch(Exception e){
  			mapRet.put("session", 0);
  			mapRet.put("msg", "알수없는 에러입니다."); // TODO: SQL오류 메세지 처리
  		}
  		return mapRet;
  	}


	// 지구코드그룹 다중삭제
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/dst/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request ,HttpServletResponse response)
			throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		String[] dstrtCds = request.getParameterValues("dstrtCd");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for(int i = 0; i< dstrtCds.length; i++){
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("dstrtCd"  , dstrtCds[i]      );
			mapId.put("updUserId", lgnVO.getUserId());
			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();

		try{
  			int ret;
			try {
	  			ret = codeDstService.deleteCodeDstMulti(list);
			} catch(DataIntegrityViolationException e) {
				logger.error(e.getRootCause().getMessage());
			} catch(UncategorizedSQLException e) {
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "삭제처리중 오류가 발생했습니다.잠시후 다시 시도해주세요");
		  	  		return mapRet;
				}
				else {
					logger.error(e.getRootCause().getMessage());
				}
			}catch(Exception e){
				logger.error(e.getMessage());
			}
			mapRet.put("session", 1);
			mapRet.put("msg", "삭제하였습니다.");
  		}
  		catch(Exception e){
  			mapRet.put("session", 0);
  			mapRet.put("msg", "알수없는 에러입니다."); // TODO: SQL오류 메세지 처리
  		}
  		return mapRet;
	}
}


