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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 공통코드그룹 현황을 관리
 * @author		대전도안 김정원
 * @version		1.00	2014-01-25
 * @since			JDK 1.7.0_45(x64)
 * @revision
 * /


/**
 * ----------------------------------------------------------
 * @Class Name : CodeCmcdController
 * @Description : 공통코드그룹
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
public class CodeCmcdController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());		
	
	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    /** TRACE */
    @Resource(name="leaveaTrace")
    LeaveaTrace leaveaTrace;

    @Resource(name="codeCmcdService")
    private CodeCmcdService codeCmcdService;

    // 공통코드그룹 리스트
    @RequestMapping(value="/wrks/sstm/code/cmcd.do")
	public String view(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	Map<String, Object> args = new HashMap<String, Object>();

	    request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));    	/*사용유무 설정*/

    	/*시스템명 설정*/
    	args.clear();
    	args.put("sysCd", "sysCd");
    	request.setAttribute("sysNmList", codeCmcdService.grpList(args));

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
    	/* row per page 설정 끝 */

    	return "wrks/sstm/code/cmcd";
	}

    // 공통코드그룹 조건검색
    @RequestMapping(value="/wrks/sstm/code/cmcd/list.json")
    @ResponseBody
    public Map<String, Object> list(ModelMap model ,HttpServletRequest request ,HttpServletResponse response) throws Exception{
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

    // 공통코드그룹 등록
    @SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/cmcd/insert.json")
    @ResponseBody
    public Map<String, Object> insert(ModelMap model
    		, HttpServletRequest request ,HttpServletResponse response) throws Exception{

    	LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId = lgnVO.getUserId();

    	Map<String, Object> map = new HashMap<String, Object>();

		map.put("CD_GRP_ID", (String)request.getParameter("cdGrpId"));
		map.put("CD_NM_KO", (String)request.getParameter("cdNmKo"));
		map.put("SYS_CD", (String)request.getParameter("sysCd"));
		map.put("CD_NM_EN", (String)request.getParameter("cdNmEn"));
		map.put("USE_TY_CD", (String)request.getParameter("useTyCd"));
		map.put("CD_DSCRT", (String)request.getParameter("cdDscrt"));
		map.put("RGS_USER_ID", sesUserId);

/*
		int insertResult = codeCmcdService.insert(map);

		Map<String, Object> map_ret = new HashMap<String, Object>();

		map_ret.put("session", 1);
		map_ret.put("msg", "저장하였습니다.");

    	return map_ret;

*/


    	Map<String, Object> mapRet = new HashMap<String, Object>();

		if(!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 0);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
	  		return mapRet;
		}

  		try{
  			int insertResult;
			try {
				insertResult = codeCmcdService.insert(map);
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
     * 공통코드그룹 삭제
     */
    @SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/cmcd/delete.json")
    @ResponseBody
    public Map<String, Object> delete(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception{

    	LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId = lgnVO.getUserId();

    	Map<String, String> map = new HashMap<String, String>();

    	map.put("CD_GRP_ID", (String)request.getParameter("cdGrpIdBk"));
    	map.put("UPD_USER_ID", sesUserId);

		int ret = codeCmcdService.delete(map);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		mapRet.put("session", 1);
		mapRet.put("msg", "삭제하였습니다.");

		return mapRet;
    }


    /*
     * 공통코드그룹 수정
     */
    @SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/cmcd/update.json")
    @ResponseBody
    public Map<String, Object> update(ModelMap model
    		, HttpServletRequest request ,HttpServletResponse response) throws Exception{

    	LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId = lgnVO.getUserId();

    	//String pageNo = (String)request.getParameter("page");

    	Map<String, Object> map = new HashMap<String, Object>();

		map.put("CD_GRP_ID", (String)request.getParameter("cdGrpId"));
		map.put("CD_NM_KO", (String)request.getParameter("cdNmKo"));
		map.put("SYS_CD", (String)request.getParameter("sysCd"));
		map.put("CD_NM_EN", (String)request.getParameter("cdNmEn"));
		map.put("USE_TY_CD", (String)request.getParameter("useTyCd"));
		map.put("CD_DSCRT", (String)request.getParameter("cdDscrt"));
		map.put("UPD_USER_ID", sesUserId);
  		map.put("cdGrpIdBk", (String)request.getParameter("cdGrpIdBk"));

/*
  		int updateResult = codeCmcdService.update(map);
		Map<String, Object> map_ret = new HashMap();

		map_ret.put("session", 1);
		map_ret.put("msg", "수정하였습니다.");

    	return map_ret;

*/

    	Map<String, Object> mapRet = new HashMap<String, Object>();

    	//map_ret.put("page", pageNo);

		if(!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
	  		return mapRet;
		}

  		try{
  			int updateResult;
			try {
				updateResult = codeCmcdService.update(map);
			} catch(DataIntegrityViolationException e) {
				//System.out.println(e.getRootCause());
				logger.error(e.getRootCause().getMessage());
			} catch(UncategorizedSQLException e) {
				//dup error
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "이미 등록된 코드입니다.");

		  	  		return mapRet;
				}
				else {
					//System.out.println(e.getCause());
					logger.error(e.getRootCause().getMessage());
				}
			}catch(Exception e){
				//e.printStackTrace();
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
     * 공통코드그룹 다중삭제
     */
    @SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/cmcd/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request ,HttpServletResponse response)
			throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId = lgnVO.getUserId();

		String[] cdGrpIds = request.getParameterValues("cdGrpId");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for(int i = 0; i < cdGrpIds.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();

			mapId.put("CD_GRP_ID" , cdGrpIds[i]);
			mapId.put("UPD_USER_ID", sesUserId);

			list.add(mapId);
		}
/*
		int ret = codeCmcdService.deleteMulti(list);

		Map<String, Object> map_ret = new HashMap();

		map_ret.put("session", 1);
		map_ret.put("msg", "삭제하였습니다.");

		return map_ret;

*/

  		Map<String, Object> mapRet = new HashMap<String, Object>();
  		try{
  			int ret;
			try {
				ret = codeCmcdService.deleteMulti(list);
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

    /*
     * 코드별 그룹조회
     */
    @RequestMapping(value="/wrks/sstm/code/cmcd/grpList.json")
    @ResponseBody
    public Map<String, Object> grpList(ModelMap model ,HttpServletRequest request ,HttpServletResponse response) throws Exception{
    	Map<String, Object> map = new HashMap<String, Object>();

    	Map<String, Object> args = new HashMap<String, Object>();

    	args.put("cdGrpId", (String)request.getParameter("cdGrpId"));
    	args.put("cdTy", "C");
    	args.put("orderBy", "ORDER BY CD_ID ASC");

    	List<Map<String, String>> list = codeCmcdService.grpList(args);

    	map.put("rows", list);

    	return map;
    }

    /*
     * sys그룹조회
     */
    @RequestMapping(value="/wrks/sstm/code/cmcd/sysList.json")
  	@ResponseBody
  	public Map<String, Object> sysList(ModelMap model
  			, HttpServletRequest request ,HttpServletResponse response) throws Exception{

      	Map<String, Object> args = new HashMap<String, Object>();

  		String usvcGrpCd = (String)request.getParameter("usvcGrpCd");
  		args.put("USVC_GRP_CD", usvcGrpCd);


  		List<Map<String, String>> list = codeCmcdService.sysCodeList(args);
  		Map<String, Object> map = new HashMap<String, Object>();

  		map.put("sysInfo", list);
  		return map;
  	}
}



