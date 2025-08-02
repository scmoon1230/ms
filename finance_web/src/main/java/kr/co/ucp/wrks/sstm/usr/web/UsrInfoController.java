package kr.co.ucp.wrks.sstm.usr.web;

import java.util.ArrayList;
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
import kr.co.ucp.cmm.service.CmmService;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.utl.sim.service.EgovFileScrty;
//import kr.co.ucp.utl.sim.service.EgovFileScrty;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.code.service.CodeDtcdMapper;
import kr.co.ucp.wrks.sstm.grp.service.GrpUserAccService;
import kr.co.ucp.wrks.sstm.usr.service.UsrInfoService;

@Controller
public class UsrInfoController
{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// EgovMessageSource
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "cmmService")
	private CmmService cmmService;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	// TRACE
	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Resource(name="usrInfoService")
	private UsrInfoService usrInfoService;

	@Resource(name="grpUserAccService")
	private GrpUserAccService grpUserAccService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	@Resource(name="codeDtcdMapper")
	private CodeDtcdMapper codeDtcdMapper;

	// 사용자관리 리스트
	@RequestMapping(value="/wrks/sstm/usr/info.do")
	public String info(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{
		/* 지구코드 조회 */
	//	request.setAttribute("listCmDstrtCdMng", grpUserAccService.getCM_DSTRT_CD_MNG(null));

	//	request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/*사용유형 설정*/

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
	/*	Map<String, String> args = new HashMap<String, String>();
		args.put("CD_GRP_ID", "USER_INSTT_NM");
		args.put("CD_TY", "C");
		args.put("USE_TY_CD", "Y");
		args.put("PRNT_GRP_ID", prprtsService.getString("DSTRT_CD"));
		args.put("ORDER_BY", "order by CD_NM_KO asc");
		List<Map<String, String>> userInsttList = codeDtcdMapper.selectUserInsttInfoList(args);
		//List<Map<String, String>> userInsttList = codeCmcdService.grpList(args);
		request.setAttribute("userInsttList", userInsttList);
	 */
	//	request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
		
		return "wrks/sstm/usr/info";
	}

	// 사용자관리 조건검색
	@RequestMapping(value="/wrks/sstm/usr/info/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model ,HttpServletRequest request ,HttpServletResponse response) throws Exception
	{
		Map<String, String> args = new HashMap<String, String>();
		args.put("USER_ID", (String)request.getParameter("userId"));
		args.put("USER_NAME", (String)request.getParameter("userName"));
		args.put("USE_YN", (String)request.getParameter("useYn"));
		args.put("pageNo"     , (String)request.getParameter("page"));
		args.put("rowsPerPage", (String)request.getParameter("rows"));
		args.put("sidx"       , (String)request.getParameter("sidx"));
		args.put("sord"       , (String)request.getParameter("sord"));
		
		List<Map<String, String>> list =  usrInfoService.selectUserList(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String)request.getParameter("page"));

		return map;
	}

	// 사용자관리 ip주소체계 검색
	@RequestMapping(value="/wrks/sstm/usr/info/selectIpTyCd.json")
	@ResponseBody
	public Map<String, Object> listIpInGubun(ModelMap model ,HttpServletRequest request ,HttpServletResponse response) throws Exception
	{
		Map<String, Object> args = new HashMap<String, Object>();
		String ipTyCd = (String)request.getParameter("ipTyCd");
		args.put("cdGrpId", ipTyCd);
		args.put("cdTy", "C");
		args.put("orderBy", "ORDER BY SORT_ORDR ASC");
		List<Map<String, String>> list =  codeCmcdService.grpList(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		return map;
	}

	// 사용자관리 사용자별 그룹 조회(등록된 그룹 이외)
	@RequestMapping(value="/wrks/sstm/usr/info/list_grp.json")
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
		List<Map<String, String>> result_rows = usrInfoService.getCmGroupList(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", pageNo);
		map.put("rows", result_rows);
		return map;
	}

	// 사용자관리 사용자별 그룹 조회
	@RequestMapping(value="/wrks/sstm/usr/info/list_user_grp.json")
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
		List<Map<String, String>> result_rows = usrInfoService.getCmGrpUserList(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", pageNo);
		map.put("rows", result_rows);
		return map;
	}

	// 사용자아이디 중복 확인
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/usr/info/checkUserId.json")
	@ResponseBody
	public Map<String, Object> checkUserId(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception
	{
		//LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		//String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("DSTRT_CD", prprtsService.getString("DSTRT_CD"));
		map.put("USER_ID" , (String)request.getParameter("userId"));
		//map.put("UPD_USER_ID", sesUserId);

		int ret = usrInfoService.selectUserIdCnt(map);
		
		Map<String, Object> map_ret = new HashMap<String, Object>();
		map_ret.put("session", 1);
		map_ret.put("msg", ret);

		return map_ret;
	}

	// 사용자관리 입력
	@RequestMapping(value="/wrks/sstm/usr/info/insert.json")
	@ResponseBody
	public Map<String, Object> registerUser(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception
	{
		// DBEncrypt 적용 (Egov)
//		String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
		String ssoLoginTag = prprtsService.getString("SSO_LOGIN", "UCP");
		String dbEncryptTag = prprtsService.getString("DB_ENCRYPT", "UCP");
//		if ("UCP".equals(dbEncryptTag)) {
//			adminPw = EgovFileScrty.encryptPassword(adminPw, saltText);
//		}

		String dstrtCd = prprtsService.getString("DSTRT_CD");
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();
		String adminPw = EgovStringUtil.nullConvert(request.getParameter("adminPw"));
		String userId = EgovStringUtil.nullConvert((String)request.getParameter("userId"));
		String password = EgovStringUtil.nullConvert((String)request.getParameter("password"));

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if ("".equals(password)) {
			mapRet.put("session", 0);
			mapRet.put("msg", "비밀번호의 값이 없습니다.");
			return mapRet;
		}

		if ("".equals(adminPw) && !"SWIP".equals(adminPw)) {
			mapRet.put("session", 2);
			mapRet.put("msg", "관리자 비밀번호의 값이 없습니다.");
			return mapRet;
		}
		// 관리자비밀번호 비교
		String sesUserPw = lgnVO.getUserPwd();		//System.out.println(sesUserPw+"=="+adminPw);
		//if(!(adminPw.equals(sesUserPw))) {
		if (!sesUserPw.equals(cmmService.getPwd(adminPw, "R", sesUserId))) {
			mapRet.put("session", 2);
			mapRet.put("msg", "관리자 비밀번호의 값이 일치하지 않습니다.");
			return mapRet;
		}

		String insttCd = (String)request.getParameter("insttCd");
		if ( "".equalsIgnoreCase(insttCd)) {	// 소속기관을 선택하지 않았을 때
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("dstrtCd", dstrtCd);
			paraMap.put("insttNm", (String)request.getParameter("insttNmInput"));
			Map<String, String> insttMap = usrInfoService.registerInsttInfo(paraMap);	// 소속기관을 등록하고 아이디를 가져온다.
			insttCd = insttMap.get("cdId").toString();
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DSTRT_CD", dstrtCd);
		map.put("USER_ID", userId);
		map.put("USER_NM_KO", (String)request.getParameter("userNmKo"));
		map.put("USER_NM_EN", (String)request.getParameter("userNmEn"));
		map.put("USE_TY_CD", (String)request.getParameter("useTyCd"));
		map.put("MOBL_NO", (String)request.getParameter("moblNo"));
		map.put("EMAIL", (String)request.getParameter("email"));
		map.put("OFFC_TEL_NO", (String)request.getParameter("offcTelNo"));
		//map.put("INSTT_NM", insttNm);
		map.put("INSTT_CD", insttCd);
		map.put("DEPT_NM", (String)request.getParameter("deptNm"));
		map.put("RANK_NM", (String)request.getParameter("rankNm"));
		map.put("RPSB_WORK", (String)request.getParameter("rpsbWork"));
		map.put("REMARK", (String)request.getParameter("remark"));
		map.put("RGS_USER_ID", sesUserId);
		//map.put("IP_ADRES", (String)request.getParameter("ipAdres"));
		//map.put("IP_TY_CD", (String)request.getParameter("ipTyCd"));
		//map.put("IP_CD", (String)request.getParameter("ipCd"));

		map.put("dbEncryptTag", dbEncryptTag);
		map.put("ssoLoginTag", ssoLoginTag);

//		// DBEncrypt 적용 (Egov)
//		if ("UCP".equals(dbEncryptTag)){
//			map.put("PASSWORD", EgovFileScrty.encryptPassword(password, saltText));
//		}else{
//			map.put("PASSWORD", password);
//		}
		String pwd = cmmService.getPwd(password, "C", "");
		map.put("PASSWORD", pwd);

		// play password
		String playPwd = new String(Base64.encodeBase64(password.getBytes()));		//System.out.println("== playPwd => "+playPwd);
		String playPwdDec = new String(Base64.decodeBase64(playPwd.getBytes()));	//System.out.println("== playPwdDec => "+playPwdDec);
		map.put("playPwd", playPwd);

		map.put("userApproveYn", prprtsService.getString("USER_APPROVE_YN")); // 사용자승인 사용여부

		if (!CommonUtil.checkDataFilterObj(map))
		{
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}  // End if

		//사용자 그룹
		String[] grpIds = request.getParameterValues("grpId");
		String[] authLvls = request.getParameterValues("authLvl");

		List<Map<String, String>> grplist = new ArrayList<Map<String, String>>();
		int grpCnt = grpIds.length;

		if( grpCnt > 0 )
		{
			for(int i = 0; i<grpCnt; i++)
			{
				Map<String, String> map_item_id = new HashMap<String, String>();
				map_item_id.put("DSTRT_CD", dstrtCd);
				map_item_id.put("GRP_ID", grpIds[i]);
				map_item_id.put("USER_ID", userId);
				map_item_id.put("USE_TY_CD", "Y");
				map_item_id.put("AUTH_LVL",  authLvls[i]);
				map_item_id.put("RGS_USER_ID", sesUserId);
				map_item_id.put("UPD_USER_ID", sesUserId);
				grplist.add(map_item_id);
			}  // End for
		}  // End if

		// 사용자별 지구
		String[] DSTRT_CD = request.getParameterValues("dstrtCd");
		Map<String, String> dstrtMap = new HashMap<String, String>();
		int dstetCnt = DSTRT_CD.length;

		if( dstetCnt > 0 )
		{
			for(int i=0; i<dstetCnt; i++){
				dstrtMap.put("DSTRT_CD", DSTRT_CD[i]);
			}

			dstrtMap.put("USER_ID", userId);
			dstrtMap.put("RGS_USER_ID", sesUserId);
		}  // End if

		// 사용자등록
		int insertResult = 0;
		try {
			insertResult = usrInfoService.insertUser(map, grplist, dstrtMap);
			
			if (insertResult == -1) {
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 아이디입니다.");
				return mapRet;
			}

			if(insertResult == 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "등록에 실패하였습니다.");
				return mapRet;
			}
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
			} catch (DataIntegrityViolationException e) {
				if (e.getRootCause().getMessage().indexOf("UNIQUE constraint violation") >= 0) {
					logger.error("--> DuplicateKeyException >>>> {}", e.getCause().getMessage());
					mapRet.put("session", 0);
					mapRet.put("msg", "이미 등록된 코드입니다.");
				} else {
					logger.error("--> DataIntegrityViolationException >>>> {}", e.getCause().getMessage());
					mapRet.put("session", 0);
					mapRet.put("msg", "DATA 오류가 발생했습니다.");
				}
			} catch (Exception e) {
				logger.error("--> Exception >>>> {}", e.getCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
			return mapRet;
	}

	// 사용자관리 사용자그룹 추가
	@SuppressWarnings({ "unused" })
	@RequestMapping(value="/wrks/sstm/usr/info/insert_grp.json")
	@ResponseBody
	public Map<String, Object> insertGrp(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception
	{
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();
		String[] grpIds = request.getParameterValues("grpId");
		String[] authLvls = request.getParameterValues("authLvl");
		String userId = (String)request.getParameter("userId");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if(grpIds.length > 0)
		{
			for(int i = 0; i< grpIds.length; i++)
			{
				Map<String, String> map_item_id = new HashMap<String, String>();
				map_item_id.put("GRP_ID", grpIds[i]);
				map_item_id.put("USER_ID", userId);
				map_item_id.put("USE_TY_CD", "Y");
				map_item_id.put("AUTH_LVL", authLvls[i]);
				map_item_id.put("RGS_USER_ID", sesUserId);
				map_item_id.put("UPD_USER_ID", sesUserId);

				list.add(map_item_id);
			}  // End for
		}  // End if

		Map<String, Object> mapRet = new HashMap<String, Object>();
		
		int ret;
		try {
			ret = usrInfoService.insertMultiGrpUser(list);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");

			Map<String, String> map = new HashMap<String, String>();
			map.put("USER_ID", (String)request.getParameter("userId"));
	
			List<Map<String, String>> grpUserList = new ArrayList<Map<String,String>>();
			/*그룹별사용자 테이블에서 등록된 그룹아이디를 가져오는부분*/
			grpUserList = usrInfoService.getGrpId(map);
	
			map.clear();
			List<Map<String, String>> dstrtCdList = new ArrayList<Map<String,String>>();
	
			for (int i=0; i<grpUserList.size(); i++)
			{
				List<Map<String, String>> a = new ArrayList<Map<String,String>>();
				map.put("GRP_ID", grpUserList.get(i).get("GRP_ID"));
				/*등록된 지구코드를 가져오는 부분*/
				a = usrInfoService.getDstrtCdList(map);
				dstrtCdList.addAll(a);
			}  // End for
	
			//map.clear();
			//for(int i =0; i<dstrtCdList.size(); i++) {
				//map.put("USER_ID", userId);
				//map.put("DSTRT_CD", dstrtCdList.get(i).get("DSTRT_CD"));
				//map.put("RGS_USER_ID", sesUserId);
	
				/*사용자지구삭제*/
				//usrInfoService.deleteUserDstrt(map);
				/*사용자지구입력*/
				//usrInfoService.insertUserDstrt(map);
			//}  // End for

		} catch (DataIntegrityViolationException e) {
			if (e.getRootCause().getMessage().indexOf("UNIQUE constraint violation") >= 0) {
				logger.error("--> DuplicateKeyException >>>> {}", e.getCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 코드입니다.");
			} else {
				logger.error("--> DataIntegrityViolationException >>>> {}", e.getCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "DATA 오류가 발생했습니다.");
			}
		} catch (Exception e) {
			logger.error("--> Exception >>>> {}", e.getCause().getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}

		return mapRet;
	}


	// 사용자관리 수정
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/usr/info/update.json")
	@ResponseBody
	public Map<String, Object> modifyUser(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> mapRet = new HashMap<String, Object>();

		String dstrtCd = prprtsService.getString("DSTRT_CD");
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();
		String adminPw = EgovStringUtil.nullConvert(request.getParameter("adminPw"));
		if ("".equals(adminPw) && !"SWIP".equals(adminPw)) {
			mapRet.put("session", 2);
			mapRet.put("msg", "관리자 비밀번호의 값이 없습니다.");
			return mapRet;
		}

		// 관리자비밀번호 비교
		String sesUserPw = lgnVO.getUserPwd();
		//if(!(adminPw.equals(sesUserPw))) {
		if (!sesUserPw.equals(cmmService.getPwd(adminPw, "R", sesUserId))) {
			mapRet.put("session", 2);
			mapRet.put("msg", "관리자 비밀번호의 값이 일치하지 않습니다.");
			return mapRet;
		}

		String insttCd = (String)request.getParameter("insttCd");
		if ( "".equalsIgnoreCase(insttCd)) {	// 소속기관을 선택하지 않았을 때
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("dstrtCd", dstrtCd);
			paraMap.put("insttNm", (String)request.getParameter("insttNmInput"));
			Map<String, String> insttMap = usrInfoService.registerInsttInfo(paraMap);	// 소속기관을 등록하고 아이디를 가져온다.
			insttCd = insttMap.get("cdId").toString();
		}
		
		// DBEncrypt 적용 (Egov)
//		String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
		String ssoLoginTag = prprtsService.getString("SSO_LOGIN", "UCP");
		String dbEncryptTag = prprtsService.getString("DB_ENCRYPT", "UCP");
		
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("USER_ID", EgovStringUtil.nullConvert((String)request.getParameter("userId")));
		map.put("USER_NM_KO", EgovStringUtil.nullConvert((String)request.getParameter("userNmKo")));
		map.put("USER_NM_EN", EgovStringUtil.nullConvert((String)request.getParameter("userNmEn")));
		map.put("USE_TY_CD", EgovStringUtil.nullConvert((String)request.getParameter("useTyCd")));
		map.put("MOBL_NO", EgovStringUtil.nullConvert((String)request.getParameter("moblNo")));
		map.put("EMAIL", EgovStringUtil.nullConvert((String)request.getParameter("email")));
		map.put("OFFC_TEL_NO", EgovStringUtil.nullConvert((String)request.getParameter("offcTelNo")));
		//map.put("INSTT_NM", insttNm);
		map.put("INSTT_CD", insttCd);
		map.put("DEPT_NM", EgovStringUtil.nullConvert((String)request.getParameter("deptNm")));
		map.put("RANK_NM", EgovStringUtil.nullConvert((String)request.getParameter("rankNm")));
		map.put("RPSB_WORK", EgovStringUtil.nullConvert((String)request.getParameter("rpsbWork")));
		map.put("REMARK", EgovStringUtil.nullConvert((String)request.getParameter("remark")));
		map.put("UPD_USER_ID", sesUserId);
		//map.put("IP_ADRES", (String)request.getParameter("ipAdres"));
		//map.put("IP_TY_CD", (String)request.getParameter("ipTyCd"));
		//map.put("IP_CD", (String)request.getParameter("ipCd"));

		//2016.03.16 modify space
		map.put("dbEncryptTag", dbEncryptTag);
		map.put("ssoLoginTag", ssoLoginTag);

//		int ret = usrInfoService.update(map);
//		Map<String, Object> map_ret = new HashMap();
//		map_ret.put("session", 1);
//		map_ret.put("msg", "저장하였습니다.");
//		return map_ret;
//		if(CommonUtil.checkDataFilterObj(map) == false)
		if(!CommonUtil.checkDataFilterObj(map))
		{
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		String password = EgovStringUtil.nullConvert((String)request.getParameter("password"));
		if ( null == password || "".equals(password) ) {
			map.put("PASSWORD", "");
		} else {
			// player password
			//String playPwd = SecurityAny.encryptSHA256(password);
			String playPwd = new String(Base64.encodeBase64(password.getBytes()));		//System.out.println("== playPwd => "+playPwd);
			String playPwdDec = new String(Base64.decodeBase64(playPwd.getBytes()));	//System.out.println("== playPwdDec => "+playPwdDec);
			map.put("playPwd", playPwd);

//			if ("UCP".equals(dbEncryptTag)){
//				map.put("PASSWORD", EgovFileScrty.encryptPassword(password, saltText));
//			} else {
//				map.put("PASSWORD", password);
//			}
			String pwd = cmmService.getPwd(password, "C", "");
			map.put("PASSWORD", pwd);
		}

		// 사용자 그룹
		List<Map<String, String>> grplist = new ArrayList<Map<String, String>>();
		if((String)request.getParameter("grpId") != null){

			String[] grpIds = request.getParameterValues("grpId");
			String[] authLvls = request.getParameterValues("authLvl");
			//List<Map<String, String>> grplist = new ArrayList<Map<String, String>>();
			int grpIdsCnt = grpIds.length;
			if(grpIdsCnt > 0)
			{
				for(int i = 0; i< grpIdsCnt; i++)
				{
					Map<String, String> map_item_id = new HashMap<String, String>();
					map_item_id.put("GRP_ID", grpIds[i]);
					map_item_id.put("USER_ID", (String)request.getParameter("userId"));
					map_item_id.put("USE_TY_CD", "Y");
					map_item_id.put("AUTH_LVL",  authLvls[i]);
					map_item_id.put("RGS_USER_ID", sesUserId);
					map_item_id.put("UPD_USER_ID", sesUserId);

					grplist.add(map_item_id);
				}  // End for
			}  // End if

		}

		int updateResult;
		try {
			
			updateResult = usrInfoService.updateUser(map, grplist);
			
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
			
			} catch (DataIntegrityViolationException e) {
			if (e.getRootCause().getMessage().indexOf("UNIQUE constraint violation") >= 0) {
				logger.error("--> DuplicateKeyException >>>> {}", e.getCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 코드입니다.");
			} else {
				logger.error("--> DataIntegrityViolationException >>>> {}", e.getCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "DATA 오류가 발생했습니다.");
			}
		} catch (Exception e) {
			logger.error("--> Exception >>>> {}", e.getCause().getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	// 사용자관리 삭제
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/usr/info/deleteComplete.json")
	@ResponseBody
	public Map<String, Object> deleteComplete(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception
	{
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("USER_ID" , (String)request.getParameter("userId"));
		map.put("UPD_USER_ID", sesUserId);

		int ret = usrInfoService.deleteUser(map);
		Map<String, Object> map_ret = new HashMap<String, Object>();
		map_ret.put("session", 1);
		if ( ret == -1 ) {
			map_ret.put("msg", "신청이력이 있으므로 삭제할 수 없습니다.");
		} else {
			map_ret.put("msg", "삭제하였습니다.");
		}

		return map_ret;
	}

	// 사용자관리 삭제
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/usr/info/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception
	{
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("USE_TY_CD" , "D");
		map.put("USER_ID" , (String)request.getParameter("userId"));
		map.put("UPD_USER_ID", sesUserId);

		int ret = usrInfoService.update(map);
		Map<String, Object> map_ret = new HashMap<String, Object>();
		map_ret.put("session", 1);
		if ( ret == 1 ) {
			map_ret.put("msg", "삭제하였습니다.");
		} else {
			map_ret.put("msg", "삭제할 수 없습니다.");
		}

		return map_ret;
	}

	// 사용자관리 다중삭제
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/usr/info/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception
	{
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();
		String[] id = request.getParameterValues("userId");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < id.length; i++)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("USE_TY_CD" , "D");
			map.put("USER_ID" , id[i]);
			map.put("UPD_USER_ID", sesUserId);
			list.add(map);
		}  // End for


//		int ret = usrInfoService.deleteMulti(list);
//		Map<String, Object> map_ret = new HashMap();
//		map_ret.put("session", 1);
//		map_ret.put("msg", "삭제하였습니다.");
//
//		return map_ret;

		Map<String, Object> mapRet = new HashMap<String, Object>();
			int ret;
			try {
				ret = usrInfoService.deleteMulti(list);
			mapRet.put("session", 1);
			mapRet.put("msg", "삭제하였습니다.");
			} catch (DataIntegrityViolationException e) {
				if (e.getRootCause().getMessage().indexOf("UNIQUE constraint violation") >= 0) {
					logger.error("--> DuplicateKeyException >>>> {}", e.getCause().getMessage());
					mapRet.put("session", 0);
					mapRet.put("msg", "이미 등록된 코드입니다.");
				} else {
					logger.error("--> DataIntegrityViolationException >>>> {}", e.getCause().getMessage());
					mapRet.put("session", 0);
					mapRet.put("msg", "DATA 오류가 발생했습니다.");
				}
			} catch (Exception e) {
				logger.error("--> Exception >>>> {}", e.getCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
			return mapRet;
	}

	// 사용자관리 사용자그룹 다중삭제
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/usr/info/deleteMulti_user_grp.json")
	@ResponseBody
	public Map<String, Object> deleteMultiUserGrp(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception
	{
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		String[] userIds = request.getParameterValues("userId");
		String[] grpIds = request.getParameterValues("grpId");
		String[] dstrtCd = request.getParameterValues("dstrtCd");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for(int i = 0; i< userIds.length; i++)
		{
			Map<String, String> map_id = new HashMap<String, String>();
			map_id.put("USER_ID", userIds[i]);
			map_id.put("GRP_ID_BAK", grpIds[i]);
			map_id.put("DSTRT_CD", dstrtCd[i]);

			list.add(map_id);
		}  // End for

		Map<String, Object> mapRet = new HashMap<String, Object>();
			int ret;
			/*그룹별사용자 삭제*/
			try {
				ret = usrInfoService.deleteMultiGrpUser(list);

			// 사용자별지구 삭제
//				for(int i=0; i<list.size(); i++)
//				{
//					Map<String, String> map = new HashMap<String, String>();
//					map = list.get(i);
//					ret = usrInfoService.deleteUserDstrt(map);
//				}  // End for

				Map<String, String> grpUserMap = new HashMap<String, String>();
				List<Map<String, String>> grpUserList = new ArrayList<Map<String,String>>();
				grpUserMap.put("USER_ID", userIds[0]);
				grpUserMap.put("RGS_USER_ID", sesUserId);

				// 사용자별지구를 다시입력하기위한 그룹별사용자 조회
//				grpUserList = usrInfoService.getDstrtCd(grpUserMap);

//				for(int i=0; i<grpUserList.size(); i++) {
//					String cd = grpUserList.get(i).get("DSTRT_CD");
//					grpUserMap.put("DSTRT_CD", cd);
//					usrInfoService.insertUserDstrt(grpUserMap);
//				}

				mapRet.put("session", 1);
				mapRet.put("msg", "삭제하였습니다.");
			} catch (DataIntegrityViolationException e) {
				if (e.getRootCause().getMessage().indexOf("UNIQUE constraint violation") >= 0) {
					logger.error("--> DuplicateKeyException >>>> {}", e.getCause().getMessage());
					mapRet.put("session", 0);
					mapRet.put("msg", "이미 등록된 코드입니다.");
				} else {
					logger.error("--> DataIntegrityViolationException >>>> {}", e.getCause().getMessage());
					mapRet.put("session", 0);
					mapRet.put("msg", "DATA 오류가 발생했습니다.");
				}
			} catch (Exception e) {
				logger.error("--> Exception >>>> {}", e.getCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
			return mapRet;
	}

	// 사용자관리 사용자 사용승인
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/usr/info/approveUser.json")
	@ResponseBody
	public Map<String, Object> approveUser(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception
	{
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();
		String userId = (String)request.getParameter("userId");
		String useTyCd = (String)request.getParameter("useTyCd");
		
		map.put("USER_ID" , userId);
		map.put("USE_TY_CD", useTyCd);

		int updateResult = usrInfoService.update(map);
		
		Map<String, Object> map_ret = new HashMap<String, Object>();
		map_ret.put("session", 1);
		map_ret.put("msg", "사용을 승인하였습니다.");

		return map_ret;
	}

	// 사용자관리 사용자비밀번호 초기화
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/usr/info/initPassword.json")
	@ResponseBody
	public Map<String, Object> initPassword(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception
	{
		String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
		String dbEncryptTag = prprtsService.getString("DB_ENCRYPT", "UCP");
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();
		String userId = (String)request.getParameter("userId");
		map.put("USER_ID" , userId);
		map.put("UPD_USER_ID", sesUserId);
		
		String password = userId+"1@";

		// DBEncrypt 적용 (Egov)
		if ("UCP".equals(dbEncryptTag)){
			map.put("PASSWORD", EgovFileScrty.encryptPassword(password, saltText));
		}else{
			map.put("PASSWORD", password);
		}

		String playPwd = new String(Base64.encodeBase64(password.getBytes()));
		map.put("playPwd", playPwd);
		
		map.put("dbEncryptTag", dbEncryptTag);

		int updateResult = usrInfoService.update(map);
		
		Map<String, Object> map_ret = new HashMap<String, Object>();
		map_ret.put("session", 1);
		map_ret.put("msg", "비밀번호를 "+password+" 로 초기화하였습니다.");

		return map_ret;
	}

	
	

	// 사용자 이름, 휴대전화, 이메일 암복호화
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/usr/info/crypto.json")
	@ResponseBody
	public Map<String, Object> crypto(ModelMap model, HttpServletRequest request ,HttpServletResponse response) throws Exception
	{
		String option = (String)request.getParameter("option");
		
		String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
		String dbEncryptTag = prprtsService.getString("DB_ENCRYPT", "UCP");

		//String algorithmKey = "QumcJheRvfHvzd1JtHJ+FS+wh2yniv3lBX5i0458oiU=";
		String algorithmKey = saltText;
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, String> args = new HashMap<String, String>();
		List<Map<String, String>> list =  usrInfoService.selectAllUserList(args);

		Map<String, Object> map = new HashMap<String, Object>();			
		map.put("UPD_USER_ID", sesUserId);
		
		for ( int i=0 ; i<list.size() ; i++ ) {
			//if ( i<3 ) {
				Map<String, String> info = list.get(i);
				map.put("USER_ID", info.get("userId").toString());
//				// java 암호화함수 사용
//				map.put("USER_NM_KO", EgovCryptoUtil.getEgovCrypto(info.get("userNmKo").toString(), option, algorithmKey, "Y"));			
//				map.put("MOBL_NO", EgovCryptoUtil.getEgovCrypto(info.get("moblNo").toString(), option, algorithmKey, "Y"));			
//				map.put("EMAIL", EgovCryptoUtil.getEgovCrypto(info.get("email").toString(), option, algorithmKey, "Y"));
				// dbms 암호화함수 사용
				map.put("USER_NM_KO", info.get("userNmKo"));			
				map.put("MOBL_NO", info.get("moblNo"));			
				map.put("EMAIL", info.get("email"));
				map.put("crypto", option);		
				map.put("saltText", saltText);
				
				logger.info("--> crypto(), userInfo({}) >>>> {}", i, map.toString());
				int updateResult = usrInfoService.update(map);
			//}
		}
		
		Map<String, Object> map_ret = new HashMap<String, Object>();
		map_ret.put("session", 1);
		map_ret.put("msg", "처리하였습니다.");

		return map_ret;
	}

	
	
	
	
	
	
	
	
	
	
	
	

}