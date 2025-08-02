package kr.co.ucp.wrks.sstm.menu.web;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.wrks.lgn.service.LgnLoginService;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.menu.service.MenuMenuService;

@Controller
public class MenuMenuController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	/** EgovPropertyService */
//	@Resource(name="propertiesService")
//	@Autowired protected EgovPropertyService propertiesService;

	/** TRACE */
	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Resource(name="lgnLoginService")
	private LgnLoginService lgnLoginService;

	@Resource(name="menuMenuService")
	private MenuMenuService menuMenuService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	// 프로그램메뉴 리스트
	@RequestMapping(value="/wrks/sstm/menu/menu.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		Map<String, Object> args = new HashMap<String, Object>();

//	    request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/* 사용유무 설정 */

		/* U-서비스 그룹 설정 */
		args.clear();
		args.put("cdGrpId", "USVC_GRP");
		args.put("cdTy", "C");

		request.setAttribute("uServiceGrp", codeCmcdService.grpList(args));

		args.clear();
		request.setAttribute("sysNmList", codeCmcdService.sysList(null));

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		return "wrks/sstm/menu/menu";
	}

	// 프로그램메뉴 조건검색
	@RequestMapping(value="/wrks/sstm/menu/menu/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");
		String prntPgmMenuId = (String) request.getParameter("prntPgmMenuId");

		Map<String, Object> args = new HashMap<String, Object>();

		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		args.put("PRNT_PGM_MENU_ID", prntPgmMenuId);

		List<Map<String, Object>> list = menuMenuService.select_pgm_menu(args);

		map.put("rows", list);

		return map;
	}

	// 프로그램메뉴 프로그램목록 조회
	@RequestMapping(value="/wrks/sstm/menu/menu/list_prgm.json")
	@ResponseBody
	public Map<String, Object> list_prgm(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");

		Map<String, Object> args = new HashMap<String, Object>();

		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		List<Map<String, Object>> list = menuMenuService.list_prgm(args);

		map.put("rows", list);

		return map;
	}

	// 프로그램메뉴 등록
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/menu/menu/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("PGM_MENU_ID", CommonUtil.getUUID());
		map.put("USVC_GRP_CD", (String) request.getParameter("usvcGrpCd"));
		map.put("SYS_CD", (String) request.getParameter("syscd"));
		map.put("PGM_ID", (String) request.getParameter("pgmId"));
		map.put("PGM_MENU_NM_KO", (String) request.getParameter("pgmMenuNmKo"));
		map.put("PGM_MENU_NM_EN", (String) request.getParameter("pgmMenuNmEn"));
		map.put("SORT_ORDR", (String) request.getParameter("sortOrdr"));
		map.put("PGM_MENU_DSCRT", (String) request.getParameter("pgmMenuDscrt"));
		map.put("PGM_MENU_VISIBLE_YN", (String) request.getParameter("pgmMenuVisibleYn"));
		map.put("USE_TY_CD", (String) request.getParameter("useTyCd"));
		map.put("PGM_MENU_ON_IMG_CRS", "");
		map.put("PGM_MENU_OFF_IMG_CRS", "");
		map.put("PGM_MENU_IMG_CRS", (String) request.getParameter("pgmMenuImgCrs"));
		map.put("RGS_USER_ID", sesUserId);
		map.put("PRNT_PGM_MENU_ID", (String) request.getParameter("prntPgmMenuId"));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int insertResult;
		try {
			insertResult = menuMenuService.insert_pgm_menu(map);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			logger.error(e.getRootCause().getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 코드입니다.");
			}
			else {
				logger.error(e.getCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}

		return mapRet;
	}

	// 프로그램메뉴 수정
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/menu/menu/update.json")
	@ResponseBody
	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("PGM_MENU_ID", (String) request.getParameter("pgmMenuId"));
		map.put("USVC_GRP_CD", (String) request.getParameter("usvcGrpCd"));
		map.put("SYS_CD", (String) request.getParameter("syscd"));
		map.put("PGM_ID", (String) request.getParameter("pgmId"));
		map.put("PGM_MENU_NM_KO", (String) request.getParameter("pgmMenuNmKo"));
		map.put("PGM_MENU_NM_EN", (String) request.getParameter("pgmMenuNmEn"));
		map.put("PRNT_PGM_MENU_ID", (String) request.getParameter("prntPgmMenuId"));
		map.put("SORT_ORDR", (String) request.getParameter("sortOrdr"));
		map.put("SORT_ORDR2", (String) request.getParameter("sortOrdr2"));
		map.put("PGM_MENU_DSCRT", (String) request.getParameter("pgmMenuDscrt"));
		map.put("PGM_MENU_VISIBLE_YN", (String) request.getParameter("pgmMenuVisibleYn"));
		map.put("USE_TY_CD", (String) request.getParameter("useTyCd"));
		map.put("PGM_MENU_ON_IMG_CRS", "");
		map.put("PGM_MENU_OFF_IMG_CRS", "");
		map.put("PGM_MENU_IMG_CRS", (String) request.getParameter("pgmMenuImgCrs"));
		map.put("UPD_USER_ID", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int updateResult;
		try {
			updateResult = menuMenuService.update_pgm_menu(map);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			logger.error(e.getRootCause().getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 코드입니다.");
			}
			else {
				logger.error(e.getCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}

		return mapRet;
	}

	// 프로그램메뉴 삭제
	@RequestMapping(value="/wrks/sstm/menu/menu/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> mapRet = new HashMap<String, Object>();

		//String saveDir = propertiesService.getString("Globals.dirWrksHome").trim() + propertiesService.getString("Globals.dirIcon").trim();
		String saveDir = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_ICON");
		if (saveDir == null || saveDir.equals("")) {
			mapRet.put("session", 1);
			mapRet.put("msg", "이미지 파일을 저장할 위치 정보가 없습니다.");
		}

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("PGM_MENU_ID", (String) request.getParameter("pgmMenuId"));
		map.put("UPD_USER_ID", sesUserId);

		// System.out.println(map);
		int result = menuMenuService.delete_pgm_menu(map);

		if (result > 0) {
			mapRet.put("session", 1);
			mapRet.put("msg", "삭제하였습니다.");
		}
		else {
			mapRet.put("session", 2);
			mapRet.put("msg", "삭제실패");
		}
		return mapRet;
	}

	
	@RequestMapping(value="/wrks/sstm/menu/menu/image.upload")
	@ResponseBody
	public Map<String, Object> upload(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		request.setCharacterEncoding("UTF-8");
		final MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> mapRet = new HashMap<String, Object>();

		boolean insert = false;

		String pgmMenuId = (String) request.getParameter("pgmMenuId");

		if (pgmMenuId == null || pgmMenuId.equals("")) {
			insert = true;
			pgmMenuId = CommonUtil.getUUID();
		}

		map.put("PGM_MENU_ID", pgmMenuId);
		map.put("USVC_GRP_CD", (String) request.getParameter("usvcGrpCd"));
		map.put("SYS_CD", (String) request.getParameter("syscd"));
		map.put("PGM_ID", (String) request.getParameter("pgmId"));
		map.put("PGM_MENU_NM_KO", URLDecoder.decode((String) multiRequest.getParameter("pgmMenuNmKo"), "UTF-8"));
		map.put("PGM_MENU_NM_EN", URLDecoder.decode((String) multiRequest.getParameter("pgmMenuNmEn"), "UTF-8"));
		map.put("PRNT_PGM_MENU_ID", (String) request.getParameter("prntPgmMenuId"));
		map.put("SORT_ORDR", (String) request.getParameter("sortOrdr"));
		map.put("SORT_ORDR2", (String) request.getParameter("sortOrdr2"));
		map.put("PGM_MENU_DSCRT", URLDecoder.decode((String) request.getParameter("pgmMenuDscrt"), "UTF-8"));
		map.put("PGM_MENU_VISIBLE_YN", (String) request.getParameter("pgmMenuVisibleYn"));
		map.put("USE_TY_CD", (String) request.getParameter("useTyCd"));
		map.put("NEW_WDW_YN", (String) request.getParameter("newWdwYn"));
		map.put("NEW_WIN_WIDTH", (String) request.getParameter("newWinWidth"));
		map.put("NEW_WIN_HEIGHT", (String) request.getParameter("newWinHeight"));

		//String saveDir = propertiesService.getString("Globals.dirWrksHome").trim() + propertiesService.getString("Globals.dirIcon").trim();
		String saveDir = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_ICON");

		if (saveDir == null || saveDir.equals("")) {
			mapRet.put("session", 1);
			mapRet.put("msg", "이미지 파일을 저장할 위치 정보가 없습니다.");
			return mapRet;
		}

		String pgmMenuImgCrs = (String) request.getParameter("pgmMenuImgCrs");
		map.put("PGM_MENU_ON_IMG_CRS", "");
		map.put("PGM_MENU_OFF_IMG_CRS", "");
		map.put("PGM_MENU_IMG_CRS", pgmMenuImgCrs);
		map.put("UPD_USER_ID", sesUserId);
		map.put("RGS_USER_ID", sesUserId);

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		if (insert) {
			menuMenuService.insert_pgm_menu(map);
		} else {
			menuMenuService.update_pgm_menu(map);
		}

		// 세션에 바낀 메뉴정보를 담아준다
		Map<String, String> args = new HashMap<String, String>();
		args.put("userId", lgnVO.getUserId());
		args.put("sysId", lgnVO.getSysId());
		List<Map<String, String>> menuList = lgnLoginService.getMenuList(args);
		lgnVO.setMenuList(menuList);
		SessionUtil.setAttribute("LoginVO", lgnVO);

		mapRet.put("session", 1);
		mapRet.put("msg", "저장하였습니다.");

		return mapRet;
	}
	
}
