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
import kr.co.ucp.wrks.sstm.menu.service.MenuGrpAuthService;
import kr.co.ucp.wrks.sstm.menu.service.MenuGrpService;










import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class MenuGrpAuthController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    @Resource(name="menuGrpAuthService")
    private MenuGrpAuthService menuGrpAuthService;

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

    // 그룹권한별 프로그램 메뉴 리스트
    @RequestMapping(value="/wrks/sstm/menu/grpauth.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

//    	request.setAttribute("listCmDstrtCdMng", menuGrpAuthService.getCM_DSTRT_CD_MNG(null));

    	Map<String, Object> args = new HashMap<String, Object>();    	/*사용유무 설정*/

		request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));

    	return "wrks/sstm/menu/grpauth";
	}


    // 그룹권한별 프로그램 메뉴 리스트
    @RequestMapping(value="/wrks/sstm/menu/grpauth/list_grpauth.json")
  	@ResponseBody
  	public Map<String, Object> list_grpauth(ModelMap model, HttpServletRequest request, HttpServletResponse response)
  			throws Exception {

    	String grpNm 	= (String)request.getParameter("grpNm");
    	String grpId 	= (String)request.getParameter("grpId");
    	String authLvl 	= (String)request.getParameter("authLvl");
    	String dstrtCd	= (String)request.getParameter("dstrtCd");
    	String useTyCd 	= (String)request.getParameter("useTyCd");
    	
		String pageNo 	= (String)request.getParameter("page");
		String sidx = (String)request.getParameter("sidx");
		String sord	= (String)request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();

		args.put("AUTH_LVL"		, authLvl);
		args.put("GRP_ID"		, grpId);
  		args.put("GRP_NM_KO"	, grpNm);
  		args.put("DSTRT_CD"		, dstrtCd);
  		args.put("USE_TY_CD"	, useTyCd);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

  		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = menuGrpAuthService.list_grpauth(args);

		resultMap.put("page", pageNo);
		resultMap.put("rows", resultRows);

  		return resultMap;
  	}


    // 그룹권한별 프로그램 메뉴 메뉴 리스트
    @RequestMapping(value="/wrks/sstm/menu/grpauth/list.json")
  	@ResponseBody
  	public Map<String, Object> getList(ModelMap model, HttpServletRequest request, HttpServletResponse response)
  			throws Exception {

    	String grpId = (String)request.getParameter("grpId");
    	String authLvl = (String)request.getParameter("authLvl");
		String pageNo = (String)request.getParameter("page");

		Map<String, String> args = new HashMap<String, String>();
  		args.put("GRP_ID", grpId);
  		args.put("AUTH_LVL", authLvl);
  		args.put("SYS_ID", prprtsService.getString("G_SYS_ID"));

  		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = menuGrpAuthService.list_grpauth_menu(args);

		resultMap.put("page", pageNo);
		resultMap.put("rows", resultRows);

  		return resultMap;
  	}


    // 그룹권한별 프로그램 메뉴 수정
    @RequestMapping(value="/wrks/sstm/menu/grpauth/update.json")
  	@ResponseBody
  	public Map<String, Object> insert(ModelMap model, HttpServletRequest request ,HttpServletResponse response)
  			throws Exception {

    	LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId = lgnVO.getUserId();
  		String grpId = (String)request.getParameter("grpId");
  		String authLvl = (String)request.getParameter("authLvl");
		String[] pgmMenuIds = request.getParameterValues("pgmMenuId");
		String[] values = request.getParameterValues("value");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for(int i = 0; i< pgmMenuIds.length; i++){
			Map<String, String> mapId = new HashMap<String, String>();
	  		logger.debug("--> RGS_AUTH_YN === ["+i+"] "+pgmMenuIds[i]+" => "+values[i]);
			mapId.put("GRP_ID", grpId);
			mapId.put("AUTH_LVL", authLvl);
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

  		menuGrpAuthService.update_grpauth_menu(list);

  		mapRet.put("session", 1);
  		mapRet.put("msg", "저장하였습니다.");

  		return mapRet;
  	}
    
    // 그룹권한별 프로그램 메뉴 복사
    @RequestMapping(value="/wrks/sstm/menu/grpauth/copy.json")
    @ResponseBody
    public Map<String, Object> copy(ModelMap model, HttpServletRequest request ,HttpServletResponse response)
    		throws Exception {

    	LoginVO lgnVO 		= (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId 	= lgnVO.getUserId();
    	String grpId 		= (String)request.getParameter("grpId");
    	String authLvl 		= (String)request.getParameter("authLvl");
    	String grpIdTo 		= (String)request.getParameter("grpIdTo");
    	String authLvlTo 	= (String)request.getParameter("authLvlTo");

		Map<String, String> args = new HashMap<String, String>();
  		args.put("GRP_ID"		, grpId);
  		args.put("AUTH_LVL"		, authLvl);
  		args.put("GRP_ID_TO"	, grpIdTo);
  		args.put("AUTH_LVL_TO"	, authLvlTo);
  		args.put("RGS_USER_ID"	, sesUserId);
  		args.put("UPD_USER_ID"	, sesUserId);

    	Map<String, Object> mapRet = new HashMap<String, Object>();

    	menuGrpAuthService.copy_grpauth_menu(args);

    	mapRet.put("session", 1);
    	mapRet.put("msg", "저장하였습니다.");

    	return mapRet;
    }
    
}
