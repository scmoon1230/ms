package kr.co.ucp.wrks.sstm.code.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.code.service.CodeDtcdService;

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
 * 코드상세 현황을 관리
 * @author		대전도안 김정원
 * @version		1.00	2014-01-25
 * @since			JDK 1.7.0_45(x64)
 * @revision
 * /


/**
 * ----------------------------------------------------------
 * @Class Name : CodeDtcdController
 * @Description : 코드상세
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-01-22   김정원       최초작성
 *
 * ----------------------------------------------------------
 * */
@Controller
public class CodeDtcdController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());	
	
	/** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    /** TRACE */
    @Resource(name="leaveaTrace")
    LeaveaTrace leaveaTrace;

    @Resource(name="codeDtcdService")
    private CodeDtcdService codeDtcdService;

    @Resource(name="codeCmcdService")
    private CodeCmcdService codeCmcdService;


    // 코드상세 리스트
    @RequestMapping(value="/wrks/sstm/code/dtcd.do")
	public String view(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	Map<String, Object> args = new HashMap<String, Object>();

    	/*코드그룹설정*/
    	args.put("cdTy", "G");
    	args.put("orderBy", "ORDER BY CD_NM_KO ASC");
    	request.setAttribute("cdGrpList", codeCmcdService.grpList(args));

	    request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/* 사용유형 */

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
    	/* row per page 설정 끝 */

    	return "wrks/sstm/code/dtcd";
	}

    // 공통코드그룹 조건검색
    @RequestMapping(value="/wrks/sstm/code/dtcd/cmcd/list.json")
    @ResponseBody
    public Map<String, Object> cmcdlist(ModelMap model ,HttpServletRequest request ,HttpServletResponse response) throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();

    	String userId = (String)request.getParameter("userId");
		String pageNo = (String)request.getParameter("page");
		String rowsPerPage = (String)request.getParameter("rows");
		String sidx = (String)request.getParameter("sidx");
		String sord	= (String)request.getParameter("sord");
		String groupId = (String)request.getParameter("groupId");
		String groupNm = (String)request.getParameter("groupNm");
		String useTyCd = (String)request.getParameter("useTyCd");

		Map<String, String> args = new HashMap<String, String>();

		args.put("userId", userId);
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		args.put("CD_GRP_ID", groupId);
		args.put("CD_GRP_NM", groupNm);
		args.put("USE_TY_CD", useTyCd);

		List<Map<String, String>> list = codeCmcdService.cmcdList(args);

    	map.put("rows", list);
		map.put("page", pageNo);

    	return map;
    }

    // 코드상세 조건검색
    @RequestMapping(value="/wrks/sstm/code/dtcd/list.json")
    @ResponseBody
	public Map<String, Object> list(ModelMap model ,HttpServletRequest request ,HttpServletResponse response) throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();

    	String cdGrpId = (String)request.getParameter("cdGrpId");
    	//String cdGrpNmKo = (String)request.getParameter("cdGrpNmKo");
		String pageNo = (String)request.getParameter("page");
		String rowsPerPage	= (String)request.getParameter("rows");
		String sidx = (String)request.getParameter("sidx");
		String sord	= (String)request.getParameter("sord");
		String useTyCd = (String)request.getParameter("useTyCd");

		Map<String, String> args = new HashMap<String, String>();

		args.put("CD_GRP_ID", cdGrpId);
		//args.put("CD_NM_KO", cdGrpNmKo);
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		args.put("USE_TY_CD", useTyCd);

		List<Map<String, String>> list = codeDtcdService.dtcdList(args);

    	map.put("rows", list);
    	//map.put("total", 10);
		map.put("page", pageNo);
		//map.put("records", 10);

    	return map;
    }


    /*
     * 코드상세 등록
     */
    @SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/dtcd/insert.json")
    @ResponseBody
    public Map<String, Object> insert(ModelMap model
    		, HttpServletRequest request, HttpServletResponse response) throws Exception{

    	LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("RGS_USER_ID", sesUserId);
		map.put("CD_GRP_ID", (String)request.getParameter("cdGrpId"));
		map.put("CD_ID", (String)request.getParameter("cdId"));
		map.put("CD_NM_KO", (String)request.getParameter("cdNmKo"));
		map.put("CD_NM_EN", (String)request.getParameter("cdNmEn"));
		map.put("USE_TY_CD", (String)request.getParameter("useTyCd"));
		map.put("CD_DSCRT", (String)request.getParameter("cdDscrt"));
		map.put("SORT_ORDR", request.getParameter("sortOrdr"));
		
		if ("USER_INSTT_NM".equalsIgnoreCase((String)request.getParameter("cdGrpId"))) {	// 사용자소속기관명 일 때
			String CD_ID = (String)request.getParameter("cdId");
			map.put("PRNT_GRP_ID", CD_ID.substring(0,5));
			map.put("CHLD_GRP_ID", CD_ID.substring(5));	
		}

		/*
		int result = codeDtcdService.insert(map);
		Map<String, Object> map_ret = new HashMap<String, Object>();
		map_ret.put("session", 1);
		map_ret.put("msg", "저장하였습니다.");
		return map_ret;
		*/

	   	Map<String, Object> mapRet = new HashMap<String, Object>();

  		if(!CommonUtil.checkDataFilterObj(map)) {
  			mapRet.put("session", 1);
  			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
  	  		return mapRet;
  		}

	  		try{
	  			int insertResult;
				try {
					insertResult = codeDtcdService.insert(map);
				} catch(DataIntegrityViolationException e) {
					if(e.getRootCause().getMessage().indexOf("UNIQUE constraint violation") >= 0) {
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
						mapRet.put("msg", "그룹 및 코드를 확인후 다시 등록해주시기 바랍니다.");
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
	  			mapRet.put("msg", "알수없는 에러!!!"); // TODO: SQL오류 메세지 처리
	  		}
	  		return mapRet;
    }


    /*
     * 코드상세 수정
     */
    @SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/dtcd/update.json")
    @ResponseBody
    public Map<String, Object> update(ModelMap model
    		, HttpServletRequest request,HttpServletResponse response)
			throws Exception {

    	LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("UPD_USER_ID", sesUserId);
		map.put("CD_GRP_ID", (String)request.getParameter("cdGrpId"));
		map.put("CD_ID", (String)request.getParameter("cdId"));
		map.put("CD_NM_KO", (String)request.getParameter("cdNmKo"));
		map.put("CD_NM_EN", (String)request.getParameter("cdNmEn"));
		map.put("USE_TY_CD", (String)request.getParameter("useTyCd"));
		map.put("CD_DSCRT", (String)request.getParameter("cdDscrt"));
		map.put("bCdGrpId", (String)request.getParameter("bCdGrpId"));
		map.put("bCdId", (String)request.getParameter("bCdId"));
		map.put("SORT_ORDR", request.getParameter("sortOrdr"));

/*
		int ret = codeDtcdService.update(map);
		Map<String, Object> map_ret = new HashMap();

		map_ret.put("session", 1);
		map_ret.put("msg", "저장하였습니다.");

		return map_ret;

*/
  	Map<String, Object> mapRet = new HashMap<String, Object>();

	if(!CommonUtil.checkDataFilterObj(map)) {
		mapRet.put("session", 1);
		mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
  		return mapRet;
	}
  		try{
  			int updateResult;
			try {
				updateResult = codeDtcdService.update(map);
			} catch(DataIntegrityViolationException e) {
				logger.error(e.getRootCause().getMessage());
			} catch(UncategorizedSQLException e) {
				//dup error
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "이미 등록된 코드입니다.");
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
  			mapRet.put("msg", "알수없는 에러!!!"); // TODO: SQL오류 메세지 처리
  		}
  		return mapRet;
    }


    /*
     * 코드상세 삭제
     */
    @SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/dtcd/delete.json")
    @ResponseBody
    public Map<String, Object> delete(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception{

    	LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId = lgnVO.getUserId();

    	Map<String, String> map = new HashMap<String, String>();

    	map.put("CD_GRP_ID", (String)request.getParameter("cdGrpId"));
    	map.put("CD_ID", (String)request.getParameter("cdId"));
    	map.put("UPD_USER_ID", sesUserId);

		int ret = codeDtcdService.delete(map);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		mapRet.put("session", 1);
		mapRet.put("msg", "삭제하였습니다.");

		return mapRet;
    }


    /*
     * 코드상세 다중삭제
     */
    @SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/dtcd/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request ,HttpServletResponse response)
			throws Exception {

    	LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId = lgnVO.getUserId();

		String[] cdGrpId = request.getParameterValues("cdGrpId");
		String[] cdId = request.getParameterValues("cdId");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for(int i = 0; i < cdGrpId.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("CD_GRP_ID" , cdGrpId[i]);
			mapId.put("CD_ID" , cdId[i]);
			mapId.put("UPD_USER_ID", sesUserId);
			list.add(mapId);
		}

/*
		int ret = codeDtcdService.deleteMulti(list);

		Map<String, Object> map_ret = new HashMap();

		map_ret.put("session", 1);
		map_ret.put("msg", "삭제하였습니다.");

		return map_ret;

*/
  		Map<String, Object> mapRet = new HashMap<String, Object>();
  		try{
  			int ret;
			try {
				ret = codeDtcdService.deleteMulti(list);
			} catch(DataIntegrityViolationException e) {
				logger.error(e.getRootCause().getMessage());
			} catch(UncategorizedSQLException e) {

				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "처리중 에러가 발생했습니다.");
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
  			mapRet.put("msg", "알수없는 에러가 발생했습니다."); // TODO: SQL오류 메세지 처리
  		}
  		return mapRet;
	}
}

