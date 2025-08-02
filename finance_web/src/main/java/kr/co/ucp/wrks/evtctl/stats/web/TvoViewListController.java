/**
 * ----------------------------------------------------------------------------------
 *
 * @File Name    : TvoViewDetailController.java
 * @Description : 긴급영상제공통계 CCTV접속상세
 * @author : seungJun
 * @version : 1.0
 * ----------------------------------------------------------------------------------
 * @Copyright (c)2017 UCP, WideCUBE, All Rights Reserved.
 * ----------------------------------------------------------------------------------
 * Data           Author      Description
 * ----------------------------------------------------------------------------------
 * 2017. 2. 22.    seungJun    최초작성
 * ----------------------------------------------------------------------------------
 * @since : 2017. 2. 22.
 */
package kr.co.ucp.wrks.evtctl.stats.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.egov.com.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.utl.fcc.service.EgovDateUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.wrks.evtctl.stats.service.TvoViewListService;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;

@Controller
public class TvoViewListController {

    @Resource(name = "prprtsService")
    private PrprtsService prprtsService;

    @Resource(name = "codeCmcdService")
    private CodeCmcdService codeCmcdService;

    @Resource(name = "tvoViewListService")
    private TvoViewListService tvoViewListService;

    // CCTV접속목록
    @RequestMapping(value = "/wrks/evtctl/stats/tvoViewList.do")
    public String tvoViewList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
        //LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        LoginVO lgnVO = SessionUtil.getUserInfo();

        // 날짜조회
        String toDate = EgovDateUtil.getToday();
        request.setAttribute("startDate", EgovDateUtil.formatDate(EgovDateUtil.addYearMonthDay(toDate, 0, 0, -30), "-"));
        request.setAttribute("endDate", EgovDateUtil.formatDate(EgovDateUtil.addYearMonthDay(toDate, 0, 0, -1), "-"));
        request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
        // 이벤트 목록 조회 통합관제 : 전체, 119, 112 ... : 해당 서비스만
        String sysCd = "";
        String appType = EgovStringUtil.isNullToString(request.getParameter("sys"));
        if ("wrks1".equals(appType)) {
            sysCd = "";
        } else {
            sysCd = lgnVO.getSysCd();
        }
        Map<String, Object> args = new HashMap<String, Object>();
		args.put("sysCd", sysCd);

        request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */
        request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
        /* row per page 설정 끝 */
        return "/wrks/evtctl/stats/tvoViewList";
    }

    @RequestMapping(value = "/wrks/evtctl/stats/tvoViewList.json")
    @ResponseBody
    public Map<String, Object> tvoViewListData(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> args = new HashMap<String, String>();
        // 페이징 조건
		args.put("pageNo"     , EgovStringUtil.nullConvert(request.getParameter("page")));
		args.put("rowsPerPage", EgovStringUtil.nullConvert(request.getParameter("rows")));
		args.put("sidx"       , EgovStringUtil.nullConvert(request.getParameter("sidx")));
		args.put("sord"       , EgovStringUtil.nullConvert(request.getParameter("sord")));
		args.put("searchStartDate", EgovStringUtil.nullConvert(request.getParameter("searchStartDate")).replaceAll("-", ""));
		args.put("searchEndDate", EgovStringUtil.nullConvert(request.getParameter("searchEndDate")).replaceAll("-", ""));
        //args.put("searchSysCd", EgovStringUtil.nullConvert(request.getParameter("searchSysCd")));
		args.put("searchEvtNo"  , EgovStringUtil.isNullToString(request.getParameter("searchEvtNo")));
		args.put("searchPaperNo", EgovStringUtil.isNullToString(request.getParameter("searchPaperNo")));
		args.put("searchCctvNm" , EgovStringUtil.isNullToString(request.getParameter("searchCctvNm")));
		args.put("searchUserNm" , EgovStringUtil.isNullToString(request.getParameter("searchUserNm")));
        
        List<Map<String, String>> resultRows = tvoViewListService.tvoViewList(args);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", resultRows);
        map.put("rCnt", resultRows.size());
        return map;
    }

    @RequestMapping(value = "/wrks/evtctl/stats/tvoViewListExcel.excel")
    public String tvoViewListExcel(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String searchStartDate = EgovStringUtil.nullConvert(request.getParameter("searchStartDateExcel")).replaceAll("-", "");
        String searchEndDate = EgovStringUtil.nullConvert(request.getParameter("searchEndDateExcel")).replaceAll("-", "");

        Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , EgovStringUtil.nullConvert(request.getParameter("searchCurrPageExcel")));
		args.put("rowsPerPage", EgovStringUtil.nullConvert(request.getParameter("searchPageListExcel")));

		args.put("searchStartDate", searchStartDate);
		args.put("searchEndDate", searchEndDate);
		args.put("searchEvtNo"  , EgovStringUtil.isNullToString(request.getParameter("searchEvtNoExcel")));
		args.put("searchPaperNo", EgovStringUtil.isNullToString(request.getParameter("searchPaperNoExcel")));
		args.put("searchCctvNm" , EgovStringUtil.isNullToString(request.getParameter("searchCctvNmExcel")));
		args.put("searchUserNm" , EgovStringUtil.isNullToString(request.getParameter("searchUserNmExcel")));
        
        List<Map<String, String>> resultRows = tvoViewListService.tvoViewListExcel(args);

		String titleKey = "viewRqstYmdhms|viewRqstUserNm|evtNo|evtNm|evtYmdHms|evtAddr|rqstRsnTyNm|paperNo|viewEndYmdhms|tvoPrgrsNm";
		String titleHeader = "신청일시|신청자|사건번호|사건명|발생일시|발생주소|신청사유|공문/전자문서|열람종료일시|진행상태";
        // 제목 및 검색조건 추가
        String tit = "열람신청현황";
		model.put("title", tit);
		model.put("search", "조회기간 : " + searchStartDate + " ~ " + searchEndDate);
		model.put("fileName", tit + "_" + searchStartDate + "_" + searchEndDate + "_(" + EgovDateUtil.getToday()+")");
		model.put("titleKey", titleKey);
		model.put("titleHeader", titleHeader);
		model.put("dataList", resultRows);

        return "excelView";
    }
}
