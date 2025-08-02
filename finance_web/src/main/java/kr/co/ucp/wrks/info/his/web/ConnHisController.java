/**
* --------------------------------------------------------------------------------------------------------------
* @Class Name : ConnHisController.java
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
package kr.co.ucp.wrks.info.his.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.EgovUserDetailsHelper;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.utl.fcc.service.EgovDateUtil;
import kr.co.ucp.wrks.info.his.service.ConnHisService;
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
public class ConnHisController
{
	static Logger logger = LoggerFactory.getLogger(ConnHisController.class);

    @Resource(name = "prprtsService")
    private PrprtsService prprtsService;

	@Resource(name="connHisService")
	ConnHisService connHisService;

	@Resource(name="codeCmcdService")
	CodeCmcdService codeCmcdService;

   /** EgovPropertyService */
//    @Resource(name="propertiesService")
//    protected EgovPropertyService propertiesService;

	// CCTV 접속 이력화면
	@RequestMapping(value="/wrks/info/his/connHisList.do")
	public String connHisList(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
        
		String toDate = EgovDateUtil.getToday();
		String startDay = EgovDateUtil.formatDate(EgovDateUtil.addYearMonthDay(toDate, 0, 0, -7), "-");
		request.setAttribute("firstDay", startDay);
		request.setAttribute("currentDay", EgovDateUtil.formatDate(EgovDateUtil.getToday(), "-"));      // 그 달의 오늘날자 구하기

        /* row per page 설정 */
        request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */
        
        LoginVO lgnVO = SessionUtil.getUserInfo();
        request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
        /* row per page 설정 끝 */
		return "wrks/info/his/connHisList";
	}

	//CCTV 접속이력데이터
	@RequestMapping(value="/wrks/info/his/connHisListData.json")
	@ResponseBody
	public Map<String, Object> connHisListData(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		
		String pageNo = request.getParameter("page");
		String rowsPerPage	= request.getParameter("rows");
		String sidx = request.getParameter("sidx");
		String sord	= request.getParameter("sord");

		// 페이징 조건
		String seachTy = request.getParameter("seachTy");
		String seachTxt = request.getParameter("seachTxt");
		String strDateStart = request.getParameter("strDateStart").replace("-", "");
		String strDateEnd = request.getParameter("strDateEnd").replace("-", "");

		// 검색조건
		Map<String, String> args = new HashMap<String, String>();
		args.put("seachTy", seachTy);
		args.put("seachTxt", seachTxt);
		args.put("searchStartDate", strDateStart.concat("000000"));
		args.put("searchEndDate", strDateEnd.concat("235959"));

     	args.put("sysId", 	lgnVO.getSysId());

		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		List<Map<String, String>> list = connHisService.connHisListData(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", pageNo);

		return map;
	}

	@RequestMapping(value = "/wrks/info/his/connHisListData.excel")
	public String connHisListExcel(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		String seachTy = EgovStringUtil.nullConvert(request.getParameter("seachTy"));
		String seachTxt = EgovStringUtil.nullConvert(request.getParameter("seachTxt"));
		String searchStartDate = EgovStringUtil.nullConvert(request.getParameter("searchStartDate")).replaceAll("-", "");
		String searchEndDate = EgovStringUtil.nullConvert(request.getParameter("searchEndDate")).replaceAll("-", "");

		Map<String, String> args = new HashMap<String, String>();

		args.put("sysId", lgnVO.getSysId());
		
		args.put("seachTy", seachTy);
		args.put("seachTxt", seachTxt);
		args.put("searchStartDate", searchStartDate.concat("000000"));
		args.put("searchEndDate", searchEndDate.concat("235959"));
		List<Map<String, String>> resultRows = connHisService.connHisListExcel(args);

		//searchStartDate = EgovStringUtil.nullConvert(request.getParameter("searchStartDate")).replaceAll("-", "");
		//searchEndDate = EgovStringUtil.nullConvert(request.getParameter("searchEndDate")).replaceAll("-", "");
		
		String titleKey = "connectYmd|loginFirstTime|userNmKo|userId|grpNm|authLvlNm|insttNm|deptNm";
		String titleHeader = "일자|접속시간|사용자명|사용자아이디|그룹명|권한레벨명|기관|부서";
		// 제목 및 검색조건 추가
        String tit = "사용자접속현황";
		model.put("title", tit);
		model.put("search", "조회기간 : " + searchStartDate + " ~ " + searchEndDate);
		model.put("fileName", tit + "_" + searchStartDate + "_" + searchEndDate + "_(" + EgovDateUtil.getToday()+")");
		model.put("titleKey", titleKey);
		model.put("titleHeader", titleHeader);
		model.put("dataList", resultRows);

		return "excelView";
	}

}
