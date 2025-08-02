package kr.co.ucp.wrks.sstm.grp.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.LoginVO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class GrpEvntController {

	/** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
    /** EgovPropertyService */
//    @Resource(name="propertiesService")
//    protected EgovPropertyService propertiesService;
    
    /** TRACE */
    @Resource(name="leaveaTrace") 
    LeaveaTrace leaveaTrace;
    
    @RequestMapping(value="/wrks/sstm/grp/evnt.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO,
			HttpServletRequest request,
			HttpServletResponse response,
			ModelMap model) 
			throws Exception {
    	return "wrks/sstm/grp/evnt";
	}
	
}