/**
* --------------------------------------------------------------------------------------------------------------
* @Class Name : EventStsController.java
* @Description :
* @Version : 1.0
* Copyright (c) 2015 by KR.CO.UCP.CNU All rights reserved.
* @Modification Information
* --------------------------------------------------------------------------------------------------------------
* DATE           AUTHOR     DESCRIPTION
* --------------------------------------------------------------------------------------------------------------
* 2015. 10. 1.   inhan29    최초작성
*
* --------------------------------------------------------------------------------------------------------------
*/
package kr.co.ucp.wrks.info.sts.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.egov.com.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.wrks.info.sts.service.UserConnStsService;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class UserConnStsController
{
	static Logger logger = LoggerFactory.getLogger(UserConnStsController.class);

	@Resource(name="userConnStsService")
	UserConnStsService userConnStsService;

	@Resource(name="codeCmcdService")
	CodeCmcdService codeCmcdService;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@RequestMapping(value="/wrks/info/sts/userConnStsList.do")
	public String eventStsList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{
  		request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */
  		
  		//LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        LoginVO lgnVO = SessionUtil.getUserInfo();
  		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
  		/* row per page 설정 끝 */
  		
		return "wrks/info/sts/userConnStsList";
	}

	@RequestMapping(value="/wrks/info/sts/userConnStsList/getUserConnStatsData.json")
	@ResponseBody
	public Map<String, Object> getUserConnStatsData(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{
		String searchYear = EgovStringUtil.nullConvert(request.getParameter("searchYear"));
		String searchMonth =EgovStringUtil.nullConvert(request.getParameter("searchMonth"));
		String searchUserType =EgovStringUtil.nullConvert(request.getParameter("searchUserType"));

		String pageNo = EgovStringUtil.nullConvert(request.getParameter("page"));
		String rowsPerPage = EgovStringUtil.nullConvert(request.getParameter("rows"));
		String sidx = EgovStringUtil.nullConvert(request.getParameter("sidx"));
		String sord = EgovStringUtil.nullConvert(request.getParameter("sord"));

		Map<String, String> args = new HashMap<String, String>();
		//LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        LoginVO lgnVO = SessionUtil.getUserInfo();

     	args.put("sysId", 	lgnVO.getSysId());
		args.put("searchUserType", searchUserType );
		args.put("searchYear", searchYear);
		args.put("searchMonth", searchMonth);

		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		List<Map<String, String>> list = userConnStsService.getUserConnStatsData(args);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);

		return map;
	}

	@RequestMapping(value="/wrks/info/sts/userConnStsList/list.excel")
 	public String getExcel(ModelMap model ,HttpServletRequest request ,HttpServletResponse response )
 			throws Exception {

		String searchYear = EgovStringUtil.nullConvert(request.getParameter("searchYear"));
		String searchMonth = EgovStringUtil.nullConvert(request.getParameter("searchMonth"));
		String searchUserType = EgovStringUtil.nullConvert(request.getParameter("searchUserType"));

		logger.info("--> Get Params >>>>> {}, {}, {}", searchUserType, searchYear, searchMonth);

 		Map<String, String> args = new HashMap<String, String>();
 		//LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        LoginVO lgnVO = SessionUtil.getUserInfo();

     	args.put("sysId", 	lgnVO.getSysId());
 		args.put("searchYear", searchYear);
 		args.put("searchMonth", searchMonth);
 		args.put("searchUserType", searchUserType);

 		String titleKey = EgovStringUtil.nullConvert(request.getParameter("titleKey"));
		String titleHeader = EgovStringUtil.nullConvert(request.getParameter("titleHeader"));

		List<Map<String, String>> resultRows = userConnStsService.getUserConnStatsExcelData(args);
		String date = (new SimpleDateFormat("yyyyMMdd", Locale.KOREA)).format(new Date());

		if("TO".equals(searchUserType)) {
			searchUserType = "전체";
		}

        //제목 및 검색조건 추가
        if(searchMonth == "" || searchMonth == null) {
    		model.put("fileName", "사용자접속통계(월별)_" + date);
    		model.put("title", "사용자접속통계(월별)");
    		model.put("search", "기준년 : " + searchYear);
        }else {
    		model.put("fileName", "사용자접속통계(일별)_" + date);
    		model.put("title", "사용자접속통계(일별)");
    		model.put("search", "기준년월 : " + searchYear + "-" + searchMonth);
        }
        //제목 및 검색조건 추가
		model.put("titleKey", titleKey);
		model.put("titleHeader", titleHeader);
		model.put("dataList", resultRows);

		return "excelView";
 	}
}
