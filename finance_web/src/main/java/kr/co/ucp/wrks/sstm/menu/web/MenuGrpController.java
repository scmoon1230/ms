package kr.co.ucp.wrks.sstm.menu.web;

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
import kr.co.ucp.wrks.sstm.menu.service.MenuGrpService;






import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 그룹별로 프로그램에대한 권한을 관리
 * @author		대전도안 김정원
 * @version		1.00	2014-01-25
 * @since			JDK 1.7.0_45(x64)
 * @revision
 * /


/**
 * ----------------------------------------------------------
 * @Class Name : MenuGrpController
 * @Description : 그룹별 프로그램메뉴
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
public class MenuGrpController {

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    @Resource(name="menuGrpService")
    private MenuGrpService menuGrpService;

	/** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;

    /** EgovPropertyService */
//    @Resource(name="propertiesService")
//    protected EgovPropertyService propertiesService;

    @Resource(name="codeCmcdService")
    private CodeCmcdService codeCmcdService;

    /** TRACE */
    @Resource(name="leaveaTrace")
    LeaveaTrace leaveaTrace;


    /*
     * 그룹별 프로그램 메뉴 리스트
     */
    @RequestMapping(value="/wrks/sstm/menu/grp.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception {

    	request.setAttribute("listCmDstrtCdMng", menuGrpService.getCM_DSTRT_CD_MNG(null));

		request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));    	/*사용유무 설정*/

    	return "wrks/sstm/menu/grp";
	}


    /*
     * 그룹별 프로그램 메뉴 리스트
     */
    @RequestMapping(value="/wrks/sstm/menu/grp/list_grp.json")
  	@ResponseBody
  	public Map<String, Object> list_grp(ModelMap model ,HttpServletRequest request ,HttpServletResponse response )
  			throws Exception {

    	String grpId = (String)request.getParameter("grpId");
    	String grpNm = (String)request.getParameter("grpNm");
    	String dstrtCd	= (String)request.getParameter("dstrtCd");
    	String useTyCd = (String)request.getParameter("useTyCd");
		String pageNo = (String)request.getParameter("page");

		Map<String, String> args = new HashMap<String, String>();

  		args.put("GRP_NM_KO", grpNm);
  		args.put("DSTRT_CD", dstrtCd);
  		args.put("USE_TY_CD", useTyCd);
  		args.put("GRP_ID", grpId);

  		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = menuGrpService.list_grp(args);

		resultMap.put("page", pageNo);
		resultMap.put("rows", resultRows);

  		return resultMap;
  	}


    /*
     * 그룹별 프로그램 메뉴 메뉴 리스트
     */
    @RequestMapping(value="/wrks/sstm/menu/grp/list.json")
  	@ResponseBody
  	public Map<String, Object> getList(ModelMap model ,HttpServletRequest request ,HttpServletResponse response )
  			throws Exception {

    	String grpId = (String)request.getParameter("grpId");
		String pageNo = (String)request.getParameter("page");

		Map<String, String> args = new HashMap<String, String>();
  		args.put("GRP_ID", grpId);

  		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = menuGrpService.list(args);

		resultMap.put("page", pageNo);
		resultMap.put("rows", resultRows);

  		return resultMap;
  	}


    /*
     * 그룹별 프로그램 메뉴 수정
     */
    @RequestMapping(value="/wrks/sstm/menu/grp/update.json")
  	@ResponseBody
  	public Map<String, Object> insert(ModelMap model, HttpServletRequest request ,HttpServletResponse response)
  			throws Exception {

    	LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId = lgnVO.getUserId();
  		String grpId = (String)request.getParameter("grpId");
		String[] pgmMenuIds = request.getParameterValues("pgmMenuId");
		String[] values = request.getParameterValues("value");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for(int i = 0; i< pgmMenuIds.length; i++){
			Map<String, String> mapId = new HashMap<String, String>();

			mapId.put("GRP_ID", grpId);
			mapId.put("PGM_MENU_ID", pgmMenuIds[i]);
			mapId.put("RGS_AUTH_YN", values[i].substring(0, 1));
			mapId.put("SEA_AUTH_YN", values[i].substring(1, 2));
			mapId.put("UPD_AUTH_YN", values[i].substring(2, 3));
			mapId.put("DEL_AUTH_YN", values[i].substring(3, 4));
			mapId.put("RGS_USER_ID", sesUserId);
			mapId.put("UPD_USER_ID", sesUserId);

			list.add(mapId);
		}

  		Map<String, Object> mapRet = new HashMap<String, Object>();

  		menuGrpService.update(list);

  		mapRet.put("session", 1);
  		mapRet.put("msg", "저장하였습니다.");

  		return mapRet;
  	}

    /*
     * 그룹별 프로그램 메뉴 복사
     */
    @RequestMapping(value="/wrks/sstm/menu/grp/copy.json")
    @ResponseBody
    public Map<String, Object> copy(ModelMap model, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {

    	LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId = lgnVO.getUserId();
    	String grpId = (String)request.getParameter("grpId");
    	String grpIdTo = (String)request.getParameter("grpIdTo");

		Map<String, String> args = new HashMap<String, String>();
  		args.put("GRP_ID", grpId);
  		args.put("GRP_ID_TO", grpIdTo);
  		args.put("RGS_USER_ID", sesUserId);
  		args.put("UPD_USER_ID", sesUserId);

    	Map<String, Object> mapRet = new HashMap<String, Object>();

    	menuGrpService.copy(args);

    	mapRet.put("session", 1);
    	mapRet.put("msg", "저장하였습니다.");

    	return mapRet;
    }
}



