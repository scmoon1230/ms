package kr.co.ucp.wrks.cop.com.web;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.EgovCmmUseService;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.cop.com.service.EgovTemplateManageService;
import kr.co.ucp.wrks.cop.com.service.TemplateInf;
import kr.co.ucp.wrks.cop.com.service.TemplateInfVO;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * 템플릿 관리를 위한 컨트롤러 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.03.18
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.18  이삼섭          최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */
@Controller
public class EgovTemplateManageController {

    @Resource(name="EgovTemplateManageService")
    private EgovTemplateManageService tmplatService;

    @Resource(name="propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name="codeCmcdService")
    private CodeCmcdService codeCmcdService;

    @Autowired
    private DefaultBeanValidator beanValidator;

    /**
     * 템플릿 목록을 조회한다.
     *
     * @param searchVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/wrks/cop/com/selectTemplateInfs.do")
    public String selectTemplateInfs(@ModelAttribute("searchVO") TemplateInfVO tmplatInfVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
    {
		tmplatInfVO.setPageUnit(propertyService.getInt("pageUnit"));
		tmplatInfVO.setPageSize(propertyService.getInt("pageSize"));
	
		PaginationInfo paginationInfo = new PaginationInfo();
	
		paginationInfo.setCurrentPageNo(tmplatInfVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(tmplatInfVO.getPageUnit());
		paginationInfo.setPageSize(tmplatInfVO.getPageSize());
	
		tmplatInfVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		tmplatInfVO.setLastIndex(paginationInfo.getLastRecordIndex());
		tmplatInfVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
	
		Map<String, Object> map = tmplatService.selectTemplateInfs(tmplatInfVO);
		int totCnt = Integer.parseInt((String)map.get("resultCnt"));
	
		paginationInfo.setTotalRecordCount(totCnt);

		// 전자정부 페이지를 사용하기 위하여 메뉴 데이터 처리
		model.addAttribute("child", request.getParameter("child"));
		model.addAttribute("top", request.getParameter("top"));
		model.addAttribute("left", request.getParameter("left"));

		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));
		model.addAttribute("paginationInfo", paginationInfo);
	
		return "cop/com/EgovTemplateList";
    }

    /**
     * 템플릿에 대한 상세정보를 조회한다.
     *
     * @param searchVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/wrks/cop/com/selectTemplateInf.do")
    public String selectTemplateInf(@ModelAttribute("searchVO") TemplateInfVO tmplatInfVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
    {
		TemplateInfVO vo = tmplatService.selectTemplateInf(tmplatInfVO);
		model.addAttribute("TemplateInfVO", vo);
		// 게시판 템플릿 코드 조회
		model.addAttribute("resultList", grpCodeList("TPL_TY"));
		// 전자정부 페이지를 사용하기 위하여 메뉴 데이터 처리
		model.addAttribute("child", request.getParameter("child"));
		model.addAttribute("top", request.getParameter("top"));
		model.addAttribute("left", request.getParameter("left"));
		return "cop/com/EgovTemplateUpdt";
    }

    /**
     * 템플릿 정보를 등록한다.
     *
     * @param searchVO
     * @param tmplatInfo
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/wrks/cop/com/insertTemplateInf.do")
    public String insertTemplateInf(@ModelAttribute("searchVO") TemplateInfVO searchVO, @ModelAttribute("templateInf") TemplateInf templateInf, BindingResult bindingResult, SessionStatus status
    		, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
    {
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		beanValidator.validate(templateInf, bindingResult);
		if (bindingResult.hasErrors()) {
			// 게시판 템플릿 코드 조회
			model.addAttribute("resultList", grpCodeList("TPL_TY"));
		    return "cop/com/EgovTemplateRegist";
		}
		templateInf.setFrstRegisterId(user.getUserId());
		if (isAuthenticated) {
		    tmplatService.insertTemplateInf(templateInf);
		}
		return "forward:/wrks/cop/com/selectTemplateInfs.do";
    }

    /**
     * 템플릿 등록을 위한 등록페이지로 이동한다.
     *
     * @param searchVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/wrks/cop/com/addTemplateInf.do")
    public String addTemplateInf(@ModelAttribute("searchVO") TemplateInfVO searchVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
    {
		// 전자정부 페이지를 사용하기 위하여 메뉴 데이터 처리
		model.addAttribute("child", request.getParameter("child"));
		model.addAttribute("top", request.getParameter("top"));
		model.addAttribute("left", request.getParameter("left"));
    	// 게시판 템플릿 코드 조회
    	model.addAttribute("resultList", grpCodeList("TPL_TY"));
		return "cop/com/EgovTemplateRegist";
    }

    /**
     * 템플릿 정보를 수정한다.
     *
     * @param searchVO
     * @param tmplatInfo
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/wrks/cop/com/updateTemplateInf.do")
    public String updateTemplateInf(@ModelAttribute("searchVO") TemplateInfVO tmplatInfVO, @ModelAttribute("templateInf") TemplateInf templateInf, BindingResult bindingResult, SessionStatus status
    		, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
	
		beanValidator.validate(templateInf, bindingResult);
	
		if (bindingResult.hasErrors()) {

		    TemplateInfVO vo = tmplatService.selectTemplateInf(tmplatInfVO);
	
		    model.addAttribute("TemplateInfVO", vo);
		    // 게시판 템플릿 코드 조회
			model.addAttribute("resultList", grpCodeList("TPL_TY"));
	
		    return "cop/com/EgovTemplateUpdt";
		}
	
		templateInf.setLastUpdusrId(user.getUserId());
	
		if (isAuthenticated) {
		    tmplatService.updateTemplateInf(templateInf);
		}
	
		return "forward:/wrks/cop/com/selectTemplateInfs.do";
    }

    /**
     * 템플릿 정보를 삭제한다.
     *
     * @param searchVO
     * @param tmplatInfo
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/wrks/cop/bbs/deleteTemplateInf.do")
    public String deleteTemplateInf(@ModelAttribute("searchVO") TemplateInfVO searchVO, @ModelAttribute("tmplatInf") TemplateInf tmplatInf, SessionStatus status
    		, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
    {
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
	
		tmplatInf.setLastUpdusrId(user.getUserId());
	
		if (isAuthenticated) {
		    tmplatService.deleteTemplateInf(tmplatInf);
		}
	
		return "forward:/wrks/cop/com/selectTemplateInfs.do";
    }

    /**
     * 팝업을 위한 템플릿 목록을 조회한다.
     *
     * @param searchVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/wrks/cop/com/selectTemplateInfsPop.do")
    public String selectTemplateInfsPop(@ModelAttribute("searchVO") TemplateInfVO tmplatInfVO, @RequestParam Map<String, Object> commandMap
    		, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
    {
		String typeFlag = (String)commandMap.get("typeFlag");
//	
//		if ("CLB".equals(typeFlag)) {
//		    tmplatInfVO.setTypeFlag(typeFlag);
//		    tmplatInfVO.setTmplatSeCode("TMPT03");
//		} else if ("CMY".equals(typeFlag)) {
//		    tmplatInfVO.setTypeFlag(typeFlag);
//		    tmplatInfVO.setTmplatSeCode("TMPT02");
//		} else {
//		    tmplatInfVO.setTypeFlag(typeFlag);
//		    tmplatInfVO.setTmplatSeCode("TMPT01");
//		}
	
		if ("BBS".equals(typeFlag)) {
			tmplatInfVO.setTypeFlag(typeFlag);
			tmplatInfVO.setTmplatSeCode("BBS");
		}
		tmplatInfVO.setPageUnit(propertyService.getInt("pageUnit"));
		tmplatInfVO.setPageSize(propertyService.getInt("pageSize"));
		//CMY, CLB
	
		PaginationInfo paginationInfo = new PaginationInfo();
	
		paginationInfo.setCurrentPageNo(tmplatInfVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(tmplatInfVO.getPageUnit());
		paginationInfo.setPageSize(tmplatInfVO.getPageSize());
	
		tmplatInfVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		tmplatInfVO.setLastIndex(paginationInfo.getLastRecordIndex());
		tmplatInfVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
	
	
		Map<String, Object> map = tmplatService.selectTemplateInfs(tmplatInfVO);
		int totCnt = Integer.parseInt((String)map.get("resultCnt"));
	
		paginationInfo.setTotalRecordCount(totCnt);

		// 전자정부 페이지를 사용하기 위하여 메뉴 데이터 처리
		model.addAttribute("child", request.getParameter("child"));
		model.addAttribute("top", request.getParameter("top"));
		model.addAttribute("left", request.getParameter("left"));

		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));
		model.addAttribute("paginationInfo", paginationInfo);
	
		return "cop/com/EgovTemplateInqirePopup";
    }

    /**
     * 팝업 페이지를 호출한다.
     *
     * @param userVO
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/wrks/cop/com/openPopup.do")
    public String openPopupWindow(@RequestParam Map<String, Object> commandMap, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
    {
    	String requestUrl = (String)commandMap.get("requestUrl");
    	String trgetId = (String)commandMap.get("trgetId");
    	String width = (String)commandMap.get("width");
    	String height = (String)commandMap.get("height");
    	String typeFlag = (String)commandMap.get("typeFlag");
    	// 전자정부 페이지를 사용하기 위하여 메뉴 데이터 처리
    	String mneuParam = "&child=" + request.getParameter("child") + "&top=" + request.getParameter("top") + "&left=" + request.getParameter("left");

    	if (trgetId != null && !"".equals( trgetId)) {
        	if (typeFlag != null && !"".equals(typeFlag)) {
    		model.addAttribute("requestUrl", requestUrl + "?trgetId=" + trgetId + "&PopFlag=Y&typeFlag=" + typeFlag + mneuParam);
    	    } else {
    		model.addAttribute("requestUrl", requestUrl + "?trgetId=" + trgetId + "&PopFlag=Y" + mneuParam);
    	    }
    	} else {
        	if (typeFlag != null && !"".equals(typeFlag)) {
    		model.addAttribute("requestUrl", requestUrl + "?PopFlag=Y&typeFlag=" + typeFlag + mneuParam);
    	    } else {
    		model.addAttribute("requestUrl", requestUrl + "?PopFlag=Y" + mneuParam);
    	    }

    	}

    	model.addAttribute("width", width);
    	model.addAttribute("height", height);

    	return "/cop/com/EgovModalPopupFrame";
    }

	/**
	 * 공통코드 목록 조회
	 * @param cdGrpId
	 * @return
	 * @throws Exception
	 */
	private List<?> grpCodeList(String cdGrpId) throws Exception
	{
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("cdGrpId", cdGrpId);
		args.put("cdTy", "C");
		args.put("orderBy", "ORDER BY CD_ID ASC");

		return codeCmcdService.grpList(args);
	}
}
