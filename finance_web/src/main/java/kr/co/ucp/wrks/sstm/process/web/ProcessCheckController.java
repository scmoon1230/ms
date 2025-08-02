package kr.co.ucp.wrks.sstm.process.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.process.service.ProcessCheckService;

/**
 * ----------------------------------------------------------
 * @Class Name : ProcessCheckController
 * @Description : 프로세스
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-03-26   설준환       최초작성
 *
 * ----------------------------------------------------------
 * */
@Controller
public class ProcessCheckController {

	/** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    /** TRACE */
    @Resource(name="leaveaTrace")
    LeaveaTrace leaveaTrace;

	@Resource(name="processCheckService")
	private ProcessCheckService processCheckService;

	@Resource(name="codeCmcdService")
    private CodeCmcdService codeCmcdService;



    /*
     * 프로세스 관리 리스트
     */
    @RequestMapping(value="/wrks/sstm/process/check.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	Map<String, Object> args = new HashMap<String, Object>();

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
    	/* row per page 설정 끝 */

    	/*상태 설정*/
    	//request.setAttribute("rcvTrmsYnList", watchdogProcessService.listRcvTrmsYn(null));

    	return "wrks/sstm/process/check";
	}

    /*
     *검색
     */
    @RequestMapping(value="/wrks/sstm/process/check/list.json")
    @ResponseBody
 	public Map<String, Object> getList(ModelMap model, HttpServletRequest request, HttpServletResponse response ) throws Exception {

 		String rcvPrcsId = request.getParameter("rcvPrcsId");
 		String rcvPrcsNm = request.getParameter("rcvPrcsNm");
 		String rcvSvrIp = request.getParameter("rcvSvrIp");
		String rcvTrmsYn = request.getParameter("rcvTrmsYn");

		String pageNo = request.getParameter("page");
		String rowsPerPage	= request.getParameter("rows");
		String sidx = request.getParameter("sidx");
		String sord	= request.getParameter("sord");

    	Map<String, String> args = new HashMap<String, String>();
    	args.put("rcvPrcsId", rcvPrcsId);
    	args.put("rcvPrcsNm", rcvPrcsNm);
		args.put("rcvSvrIp", rcvSvrIp);
		args.put("rcvTrmsYn", rcvTrmsYn);

		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		List<Map<String, String>> list = processCheckService.getList(args);

    	Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
 		map.put("page", pageNo);

 		return map;
 	}
}
