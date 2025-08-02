package kr.co.ucp.env.web;

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
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.utl.fcc.service.EgovDateUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.env.service.UserlogService;

@Controller
public class DbbackupController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="userlogService")
	UserlogService userlogService;

	// 사용기록 리스트
	@RequestMapping(value="/env/dbbackup.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

        // 날짜조회
        String toDate = EgovDateUtil.getToday();
        request.setAttribute("startDate", EgovDateUtil.formatDate(EgovDateUtil.addYearMonthDay(toDate, 0, 0, -14), "-"));
        request.setAttribute("endDate", EgovDateUtil.formatDate(EgovDateUtil.addYearMonthDay(toDate, 0, 0, -1), "-"));
        request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
        
		return "env/dbbackup";
	}

}
