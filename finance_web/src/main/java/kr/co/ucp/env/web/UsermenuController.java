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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.env.service.UserService;
import kr.co.ucp.env.service.UsermenuService;

@Controller
public class UsermenuController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="userService")
	UserService userService;

    @Resource(name="usermenuService")
    private UsermenuService usermenuService;

    // 그룹권한별 프로그램 메뉴 리스트
    @RequestMapping(value="/env/usermenu.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

    	return "env/usermenu";
	}


    // 그룹권한별 프로그램 메뉴 리스트
    @RequestMapping(value="/env/usermenu/userlist.json")
  	@ResponseBody
  	public Map<String, Object> userlist(ModelMap model, HttpServletRequest request, HttpServletResponse response)
  			throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("userName", (String) request.getParameter("userName"));
//		args.put("positionCode", (String) request.getParameter("positionCode"));
//		args.put("acctGb", (String) request.getParameter("acctGb"));
//		args.put("userGb", (String) request.getParameter("userGb"));
//		args.put("useYn", (String) request.getParameter("useYn"));
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		List<Map<String, String>> list = userService.selectUser(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

  		return map;
  	}


    // 그룹권한별 프로그램 메뉴 메뉴 리스트
    @RequestMapping(value="/env/usermenu/menulist.json")
  	@ResponseBody
  	public Map<String, Object> menulist(ModelMap model, HttpServletRequest request, HttpServletResponse response)
  			throws Exception {

		Map<String, String> args = new HashMap<String, String>();
  		args.put("userId", (String)request.getParameter("userId"));

		List<Map<String, String>> list = usermenuService.menulist(args);

  		Map<String, Object> map = new HashMap<String, Object>();
  		map.put("rows", list);
  		map.put("page", (String)request.getParameter("page"));

  		return map;
  	}


    // 그룹권한별 프로그램 메뉴 수정
    @RequestMapping(value="/env/usermenu/update.json")
  	@ResponseBody
  	public Map<String, Object> update(ModelMap model, HttpServletRequest request ,HttpServletResponse response)
  			throws Exception {

    	LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	String sesUserId = lgnVO.getUserId();
    	
  		String userId = (String)request.getParameter("userId");
  		//String authLvl = (String)request.getParameter("authLvl");
		String[] pgmMenuIds = request.getParameterValues("pgmMenuId");
		String[] values = request.getParameterValues("value");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for(int i = 0; i< pgmMenuIds.length; i++){
			Map<String, String> mapId = new HashMap<String, String>();
	  		//logger.debug("--> menuInfo == ["+i+"] "+pgmMenuIds[i]+" => "+values[i]);
			mapId.put("userId", userId);
			//mapId.put("AUTH_LVL", authLvl);
			mapId.put("menuId", pgmMenuIds[i]);
			mapId.put("yn", values[i]);
			//mapId.put("SEA_AUTH_YN", values[i].substring(1, 2));
			//mapId.put("UPD_AUTH_YN", values[i].substring(2, 3));
			//mapId.put("DEL_AUTH_YN", values[i].substring(3, 4));
			mapId.put("RGS_USER_ID", sesUserId);
			mapId.put("UPD_USER_ID", sesUserId);

			list.add(mapId);
		}

  		Map<String, Object> mapRet = new HashMap<String, Object>();

  		usermenuService.update_grpauth_menu(list);

  		mapRet.put("session", 1);
  		mapRet.put("msg", "저장하였습니다.");

  		return mapRet;
  	}
    
//    // 그룹권한별 프로그램 메뉴 복사
//    @RequestMapping(value="/env/usermenu/copy.json")
//    @ResponseBody
//    public Map<String, Object> copy(ModelMap model, HttpServletRequest request ,HttpServletResponse response)
//    		throws Exception {
//
//    	LoginVO lgnVO 		= (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
//    	String sesUserId 	= lgnVO.getUserId();
//    	String grpId 		= (String)request.getParameter("grpId");
//    	String authLvl 		= (String)request.getParameter("authLvl");
//    	String grpIdTo 		= (String)request.getParameter("grpIdTo");
//    	String authLvlTo 	= (String)request.getParameter("authLvlTo");
//
//		Map<String, String> args = new HashMap<String, String>();
//  		args.put("GRP_ID"		, grpId);
//  		args.put("AUTH_LVL"		, authLvl);
//  		args.put("GRP_ID_TO"	, grpIdTo);
//  		args.put("AUTH_LVL_TO"	, authLvlTo);
//  		args.put("RGS_USER_ID"	, sesUserId);
//  		args.put("UPD_USER_ID"	, sesUserId);
//
//    	Map<String, Object> mapRet = new HashMap<String, Object>();
//
//    	usermenuService.copy_grpauth_menu(args);
//
//    	mapRet.put("session", 1);
//    	mapRet.put("msg", "저장하였습니다.");
//
//    	return mapRet;
//    }
    
}
