package kr.co.ucp.wrks.sstm.usr.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import kr.co.ucp.cmm.EgovCryptoUtil;
import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.EgovUserDetailsHelper;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.SecurityAny;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.utl.sim.service.EgovFileScrty;
//import kr.co.ucp.utl.sim.service.EgovFileScrty;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.grp.service.GrpUserAccService;
import kr.co.ucp.wrks.sstm.usr.service.UsrApproveService;
import kr.co.ucp.wrks.sstm.usr.service.UsrInfoService;

@Controller
public class UsrApproveController
{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// EgovMessageSource
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	// TRACE
	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Resource(name="usrInfoService")
	private UsrInfoService usrInfoService;

	@Resource(name="usrApproveService")
	private UsrApproveService usrApproveService;

	@Resource(name="grpUserAccService")
	private GrpUserAccService grpUserAccService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	// 사용자관리 리스트
    @RequestMapping(value="/wrks/sstm/usr/approve.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{
		/* 지구코드 조회 */
		request.setAttribute("listCmDstrtCdMng", grpUserAccService.getCM_DSTRT_CD_MNG(null));
		
		Map<String, Object> args = new HashMap<String, Object>();

		request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/*사용유형 설정*/

//		args.clear();
//		args.put("cdGrpId", "IPv");
//		args.put("cdTy", "C");
//		args.put("orderBy", "ORDER BY SORT_ORDR ASC");
//		List<Map<String, String>> ipList = codeCmcdService.grpList(args);
//		request.setAttribute("ipvList", ipList);

//		args.clear();
//		args.put("cdGrpId", ipList.get(0).get("CD_ID"));
//		args.put("cdTy", "C");
//		args.put("orderBy", "ORDER BY SORT_ORDR ASC");
//		request.setAttribute("ipvInList", codeCmcdService.grpList(args));

        /* 기관명 */
        args.clear();
		args.put("cdGrpId", "USER_INSTT_NM");
		args.put("prntGrpId", prprtsService.getString("DSTRT_CD"));
		args.put("cdTy", "C");
		args.put("orderBy", "ORDER BY CD_NM_KO ASC");
        List<Map<String, String>> userInsttList = codeCmcdService.grpList(args);
        request.setAttribute("userInsttList", userInsttList);

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
		
		return "wrks/sstm/usr/approve";
	}

	// 사용자관리 조건검색
	@RequestMapping(value="/wrks/sstm/usr/approve/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model ,HttpServletRequest request ,HttpServletResponse response) throws Exception
	{
		Map<String, Object> map = new HashMap<String, Object>();

		String userId = (String)request.getParameter("userId");
		String userNmKo = (String)request.getParameter("userNmKo");
		String useTyCd = (String)request.getParameter("useTyCd");
		String dstrtCd = (String)request.getParameter("dstrtCd");
		String insttCd = (String)request.getParameter("insttCd");
		String userApproveTy = (String)request.getParameter("userApproveTy");
		logger.debug("--> list(), userApproveTy >>>> {}", userApproveTy);
		
		String grpId = (String)request.getParameter("grpId");
		if ( "PVECENTER".equalsIgnoreCase(grpId)) { // 영상반출 관제센터 그룹
			grpId = "";
		}
//		String moblNo	= (String)request.getParameter("moblNo");
		String pageNo = (String)request.getParameter("page");
		String rowsPerPage = (String)request.getParameter("rows");
		String sidx = (String)request.getParameter("sidx");
		String sord	= (String)request.getParameter("sord");
		//logger.debug("--> list(), dstrtCd >>>> {}", dstrtCd);
		//logger.info("--> list(), useTyCd:{}", useTyCd);
		//logger.info("--> list(), insttCd:{}", insttCd);

//		if(moblNo != null && moblNo != ""){
//			String sMoblNo = null;
//			sMoblNo = moblNo.substring(0, 3);
//			sMoblNo += "-"+ moblNo.substring(3, 7);
//			sMoblNo += "-"+ moblNo.substring(7);
//
//			moblNo = sMoblNo;
//		}
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		Map<String, String> args = new HashMap<String, String>();
		// 2016.06.20 추가 (윤법상)
		args.put("sysId", lgnVO.getSysId());
		args.put("USER_ID", userId);
		args.put("USER_NM_KO", userNmKo);
		args.put("USE_TY_CD", useTyCd);
		args.put("DSTRT_CD", dstrtCd);
		args.put("INSTT_CD", insttCd);
		args.put("USER_APPROVE_TY", userApproveTy);
		//args.put("GRP_ID", grpId);
//		args.put("MOBL_NO", moblNo);
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		List<Map<String, String>> list =  usrInfoService.selectUserList(args);

		map.put("rows", list);
		map.put("page", pageNo);

		return map;
	}

	

    // 사용자 승인
    @SuppressWarnings("unused")
    @RequestMapping(value = "/wrks/sstm/usr/approve/approve.json")
    @ResponseBody
    public Map<String, Object> approve(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        String sesUserId = lgnVO.getUserId();
        Map<String, Object> mapRet = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String[] userIds = request.getParameterValues("userId[]");
        List<String> userIdList = Arrays.asList(userIds);

        map.put("userId", userIdList);
        map.put("updUserId", sesUserId);

        int ret;
        try {
            ret = usrApproveService.approve(map);
            mapRet.put("session", 1);
            mapRet.put("msg", "승인 처리하였습니다.");
        } catch (DataIntegrityViolationException e) {
            logger.error("usrApproveService.approve DataIntegrityViolationException : {}", e.getMessage());
            logger.error(e.getRootCause().getMessage());
        } catch (UncategorizedSQLException e) {
            if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
                mapRet.put("session", 0);
                mapRet.put("msg", "처리중 에러가 발생했습니다.");
            } else {
                logger.error("usrApproveService.approve UncategorizedSQLException : {}", e.getMessage());
            }
        } catch (Exception e) {
            logger.error("approve Exception : {}", e.getMessage());
            mapRet.put("session", 0);
            mapRet.put("msg", "승인에 실패하였습니다.");
        }
        return mapRet;
    }
    
 // 사용자 미승인
    @SuppressWarnings("unused")
    @RequestMapping(value = "/wrks/sstm/usr/approve/notapprove.json")
    @ResponseBody
    public Map<String, Object> notApprove(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        String sesUserId = lgnVO.getUserId();
        Map<String, Object> mapRet = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String[] userIds = request.getParameterValues("userId[]");
        List<String> userIdList = Arrays.asList(userIds);

        map.put("userId", userIdList);
        map.put("updUserId", sesUserId);

        int ret;
        try {
            ret = usrApproveService.notApprove(map);
            mapRet.put("session", 1);
            mapRet.put("msg", "미승인 처리하였습니다.");
        } catch (DataIntegrityViolationException e) {
            logger.error("usrApproveService.notApprove DataIntegrityViolationException : {}", e.getMessage());
            logger.error(e.getRootCause().getMessage());
        } catch (UncategorizedSQLException e) {
            if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
                mapRet.put("session", 0);
                mapRet.put("msg", "처리중 에러가 발생했습니다.");
            } else {
                logger.error("usrApproveService.notApprove UncategorizedSQLException : {}", e.getMessage());
            }
        } catch (Exception e) {
            logger.error("notApprove Exception : {}", e.getMessage());
            mapRet.put("session", 0);
            mapRet.put("msg", "미승인에 실패하였습니다.");
        }
        return mapRet;
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 사용자관리 사용자별 그룹 조회(등록된 그룹 이외)
	@RequestMapping(value="/wrks/sstm/usr/approve/list_grp.json")
	@ResponseBody
	public Map<String, Object> list_grp(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{
		String userId = (String)request.getParameter("userId");
		String grpId = (String)request.getParameter("grpId");
		if ( "PVECENTER".equalsIgnoreCase(grpId)) { // 영상반출 관제센터 그룹
			grpId = "";
		}
		String pageNo = (String)request.getParameter("page");
		String rowsPerPage = (String)request.getParameter("rows");
		String sidx = (String)request.getParameter("sidx");
		String sord	= (String)request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		args.put("sysId", lgnVO.getSysId());
		logger.debug("--> loginVO.getSysId() >>>> {}", lgnVO.getSysId());
		args.put("USER_ID", userId);
		args.put("GRP_ID", grpId);
		
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		//List<Map<String, String>> result_rows = usrApproveService.getCmGroupList(args);
		List<Map<String, String>> result_rows = usrInfoService.getCmGroupList(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", pageNo);
		map.put("rows", result_rows);
		return map;
	}

	// 사용자관리 사용자별 그룹 조회
	@RequestMapping(value="/wrks/sstm/usr/approve/list_user_grp.json")
	@ResponseBody
	public Map<String, Object> list_user_grp(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{
		String userId = (String)request.getParameter("userId");
		String pageNo = (String)request.getParameter("page");
		String rowsPerPage = (String)request.getParameter("rows");
		String sidx = (String)request.getParameter("sidx");
		String sord = (String)request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();
		args.put("USER_ID", userId);
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		//List<Map<String, String>> result_rows = usrApproveService.getCmGrpUserList(args);
		List<Map<String, String>> result_rows = usrInfoService.getCmGrpUserList(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", pageNo);
		map.put("rows", result_rows);
		return map;
	}

}