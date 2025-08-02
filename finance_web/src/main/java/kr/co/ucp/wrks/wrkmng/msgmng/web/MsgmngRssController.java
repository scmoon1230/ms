package kr.co.ucp.wrks.wrkmng.msgmng.web;

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
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.grp.service.GrpUserAccService;
import kr.co.ucp.wrks.wrkmng.msgmng.service.MsgmngRssService;

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
 * RSS정보 현황을 관리
 * @author		대전도안 설준환
 * @version		1.00	2014-02-16
 * @since			JDK 1.7.0_45(x64)
 * @revision
 * /


/**
 * ----------------------------------------------------------
 * @Class Name : MsgmngRssController
 * @Description : RSS정보관리
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-02-16   설준환       최초작성
 * 
 * ----------------------------------------------------------
 * */
@Controller
public class MsgmngRssController {

	/** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    /** TRACE */
    @Resource(name="leaveaTrace") 
    LeaveaTrace leaveaTrace;
    
    @Resource(name="grpUserAccService")
    private GrpUserAccService grpUserAccService;
    
    @Resource(name="codeCmcdService")
    private CodeCmcdService codeCmcdService;
    
    @Resource(name="msgmngRssService")
    private MsgmngRssService msgmngRssService;
    
    /*
     * RSS 리스트
     */
    @RequestMapping(value="/wrks/wrkmng/msgmng/rss.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	
    	Map<String, Object> args = new HashMap<String, Object>();

    	request.setAttribute("listCmDstrtCdMng", grpUserAccService.getCM_DSTRT_CD_MNG(null));		/*지구코드 조회*/

	    request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));    	/*사용유무 설정*/
    	
	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
    	return "wrks/wrkmng/msgmng/rss";
	}
	
    /*
     * RSS 조건검색
     */
    @RequestMapping(value="/wrks/wrkmng/msgmng/rss/list.json")
    @ResponseBody
    public Map<String, Object> list(ModelMap model
			, HttpServletRequest request ,HttpServletResponse response) throws Exception{
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	String dstrtCd = request.getParameter("dstrtCd");
    	String useTy = request.getParameter("useTy");
		String pageNo = request.getParameter("page");
		String rowsPerPage	= request.getParameter("rows");
		String sidx = request.getParameter("sidx");
		String sord	= request.getParameter("sord");
		
		Map<String, String> args = new HashMap<String, String>();
		
		args.put("dstrtCd", dstrtCd);
		args.put("useTy", useTy);
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		@SuppressWarnings("unchecked")
		List<Map<String, String>> list = msgmngRssService.view(args);
		map.put("rows", list);
		map.put("page", pageNo);
		return map;
    }
    
    
    /*
     * RSS 입력
     */
    @SuppressWarnings("unused")
	@RequestMapping(value="/wrks/wrkmng/msgmng/rss/insert.json")
    @ResponseBody
    public Map<String, Object> insert(ModelMap model
			, HttpServletRequest request ,HttpServletResponse response) throws Exception{
    	
    	LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId = lgnVO.getUserId();
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	map.put("rssTitle", URLDecoder.decode(request.getParameter("rssTitle"), "UTF-8"));
    	map.put("rssUrl", URLDecoder.decode(request.getParameter("rssUrl"), "UTF-8"));
    	map.put("useTy", URLDecoder.decode(request.getParameter("useTy"), "UTF-8"));
    	map.put("dstrtCd", URLDecoder.decode(request.getParameter("dstrtCd"), "UTF-8"));
    	map.put("rgsUserId", sesUserId);
    	
		Map<String, Object> map_ret = new HashMap<String, Object>();

  		try{
  			int insertResult;
			try {
				insertResult = msgmngRssService.insert(map);
			} catch(DataIntegrityViolationException e) {
				System.out.println(e.getRootCause());
			} catch(UncategorizedSQLException e) {
				//dup error
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
		  	  		map_ret.put("session", 0);
		  	  		map_ret.put("msg", "이미 등록된 아이디입니다.");
		  	  		return map_ret;
				}
				else {
					System.out.println(e.getCause());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
  	  		map_ret.put("session", 1);
  	  		map_ret.put("msg", "저장하였습니다.");
  		}
  		catch(Exception e){
  	  		map_ret.put("session", 0);
  	  		map_ret.put("msg", "알수없는 에러!!!"); // TODO: SQL오류 메세지 처리	
  		}
		return map_ret;
    }
    
    
    /*
     * RSS 수정
     */
    @SuppressWarnings("unused")
	@RequestMapping(value="/wrks/wrkmng/msgmng/rss/update.json")
    @ResponseBody
    public Map<String, Object> update(ModelMap model
			, HttpServletRequest request ,HttpServletResponse response) throws Exception{
    	    	
    	LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId = lgnVO.getUserId();
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	map.put("rssId", request.getParameter("rssId"));
    	map.put("rssTitle", URLDecoder.decode(request.getParameter("rssTitle"), "UTF-8"));
    	map.put("rssUrl", URLDecoder.decode(request.getParameter("rssUrl"), "UTF-8"));
    	map.put("useTy", URLDecoder.decode(request.getParameter("useTy"), "UTF-8"));
    	map.put("dstrtCd", URLDecoder.decode(request.getParameter("dstrtCd"), "UTF-8"));
    	map.put("updUserId", sesUserId);
    	
    	Map<String, Object> map_ret = new HashMap<String, Object>();

  		try{
  			int updateResult;
			try {
				updateResult = msgmngRssService.update(map);
			} catch(DataIntegrityViolationException e) {
				System.out.println(e.getRootCause());
			} catch(UncategorizedSQLException e) {
				//dup error
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
		  	  		map_ret.put("session", 0);
		  	  		map_ret.put("msg", "이미 등록된 아이디입니다.");
		  	  		return map_ret;
				}
				else {
					System.out.println(e.getCause());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
  	  		map_ret.put("session", 1);
  	  		map_ret.put("msg", "저장하였습니다.");
  		}
  		catch(Exception e){
  	  		map_ret.put("session", 0);
  	  		map_ret.put("msg", "알수없는 에러!!!"); // TODO: SQL오류 메세지 처리
  		}	
  		return map_ret;
    }
    
    
    /*
     * RSS 삭제
     */
	@RequestMapping(value="/wrks/wrkmng/msgmng/rss/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model
			, HttpServletRequest request ,HttpServletResponse response) throws Exception{
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("updUserId", sesUserId);
		map.put("rssId", request.getParameter("rssId"));
		
		int result = msgmngRssService.delete(map);
		
		Map<String, Object> map_ret = new HashMap<String, Object>();
		
		if(result > 0){
			map_ret.put("session", 1);
			map_ret.put("msg", "삭제하였습니다.");
		}else{
			map_ret.put("session", 2);
			map_ret.put("msg", "삭제실패");			
		}
		
		return map_ret;
	}
	
	
	/*
     * RSS 다중삭제
     */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/wrkmng/msgmng/rss/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model
			, HttpServletRequest request ,HttpServletResponse response){
    	LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId = lgnVO.getUserId();
		String[] rssIdArr = request.getParameterValues("rssId"); 
		
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		
		for(int i=0; i<rssIdArr.length; i++){
			Map<String, String> map_id = new HashMap<String, String>();
			map_id.put("updUserId", sesUserId);
			map_id.put("rssId", rssIdArr[i]);
			list.add(map_id);
		}
	
		Map<String, Object> map_ret = new HashMap<String, Object>();
  		try{
  			int ret;
			try {
				ret = msgmngRssService.deleteMulti(list);
			} catch(DataIntegrityViolationException e) {
				System.out.println(e.getRootCause());
			} catch(UncategorizedSQLException e) {
				
				if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
		  	  		map_ret.put("session", 0);
		  	  		map_ret.put("msg", "처리중 에러가 발생했습니다.");
		  	  		return map_ret;
				}
				else {
					System.out.println(e.getCause());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
  	  		map_ret.put("session", 1);
  	  		map_ret.put("msg", "삭제하였습니다.");
  		}
  		catch(Exception e){
  	  		map_ret.put("session", 0);
  	  		map_ret.put("msg", "알수없는 에러가 발생했습니다."); // TODO: SQL오류 메세지 처리
  		}
  		return map_ret;
	}
}



