/**
 * -----------------------------------------------------------------
 *
 * @Class Name : MpgInfoController.java
 * @Description :
 * @Version : 1.0 Copyright (c) 2017 by KR.CO.UCP All rights reserved.
 * @Modification Information
 *
 *               <pre>
 * -----------------------------------------------------------------
 * DATE           AUTHOR      DESCRIPTION
 * -----------------------------------------------------------------
 * 2016. 4. 26.   강인한    New Write
 * 2019. 8. 18.   SaintJuny 시큐어 코딩 처리
 * -----------------------------------------------------------------
 *               </pre>
 */
package kr.co.ucp.wrks.sstm.mpg.web;

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
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.mntr.cmm.service.CmmCodeService;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.grp.service.GrpCodeService;
import kr.co.ucp.wrks.sstm.mpg.service.MpgInfoService;

@Controller
public class MpgInfoController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	@Resource(name="grpCodeService")
	private GrpCodeService grpCodeService;

	@Resource(name="mpgInfoService")
	private MpgInfoService mpgInfoService;

	// 리스트
	@RequestMapping(value="/wrks/sstm/mpg/mpgInfoList.do")
	public String cctvPtzHisList(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();

		/* 지구코드조회 */
		request.setAttribute("dstrtList", grpCodeService.getDstrtList(null));

		// 네트워크 구분
		args.put("cdGrpId", "MP_NETWORK_TY");
		args.put("cdTy", "C");
		args.put("orderBy", "ORDER BY CD_NM_KO ASC");
		request.setAttribute("networkTy", codeCmcdService.grpList(args));

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
		return "wrks/sstm/mpg/mpgInfoList";
	}

	@RequestMapping(value="/wrks/sstm/mpg/mpgInfoListData.json")
	@ResponseBody
	public Map<String, Object> mpgInfoListData(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String pageNo = request.getParameter("page");
		String rowsPerPage = request.getParameter("rows");
		String sidx = request.getParameter("sidx");
		String sord = request.getParameter("sord");

		// 페이징 조건
		String networkTy = request.getParameter("networkTy");
		String networkNm = request.getParameter("networkNm");
		String dstrtCd = request.getParameter("dstrtCd");

		// 검색조건
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		
		args.put("networkTy", networkTy);
		args.put("networkNm", networkNm);
		args.put("dstrtCd", dstrtCd);
		List<Map<String, String>> list = mpgInfoService.selectMpgInfoList(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", pageNo);
		return map;
	}

	// 추가
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mpg/mpgInfoList/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("networkId", request.getParameter("networkId"));
		map.put("networkNm", request.getParameter("networkNm"));
		map.put("networkTy", request.getParameter("networkTy"));
		map.put("networkIp", request.getParameter("networkIp"));
		map.put("networkMpIp", request.getParameter("networkMpIp"));
		map.put("dstrtCd", request.getParameter("dstrtCd"));
		map.put("rgsUserId", sesUserId);
		map.put("updUserId", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int insertResult;
		try {
			insertResult = mpgInfoService.insertMpgInfo(map);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		}
		catch (DataIntegrityViolationException e) {

			if (e.getRootCause().getMessage().indexOf("UNIQUE constraint violation") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 코드입니다.");
			}
			else {
				logger.error(e.getRootCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}

		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 코드입니다.");
			}
			else {
				logger.error("insert UncategorizedSQLException : {}", e.getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error("insert Exception : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	// 수정
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mpg/mpgInfoList/update.json")
	@ResponseBody
	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("networkId", request.getParameter("networkId"));
		map.put("networkNm", request.getParameter("networkNm"));
		map.put("networkTy", request.getParameter("networkTy"));
		map.put("networkIp", request.getParameter("networkIp"));
		map.put("networkMpIp", request.getParameter("networkMpIp"));
		map.put("rgsUserId", "admin");
		map.put("updUserId", "admin");

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int updateResult;
		try {
			updateResult = mpgInfoService.updateMpgInfo(map);
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

	// 삭제
	@RequestMapping(value="/wrks/sstm/mpg/mpgInfoList/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("networkId", (String) request.getParameter("networkId"));
		int result = mpgInfoService.deleteMpgInfo(map);

		Map<String, Object> mapRet = new HashMap<String, Object>();

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

	// 다중삭제
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mpg/mpginfoList/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		String[] networkId = request.getParameterValues("networkId");
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < networkId.length; i++) {
			Map<String, Object> mapId = new HashMap<String, Object>();
			mapId.put("networkId", networkId[i]);
			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = mpgInfoService.deleteMpgInfoMulti(list);
			mapRet.put("session", 1);
			mapRet.put("msg", "삭제하였습니다.");
	    }
		catch (DataIntegrityViolationException e) {
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
}
