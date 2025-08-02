package kr.co.ucp.wrks.sstm.menu.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.menu.service.MenuIconService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 메뉴아이콘 관리
 *
 * @author 대전도안 김정원
 * @version 1.00 2014-01-25
 * @since JDK 1.7.0_45(x64)
 * @revision
 */

/**
 * ----------------------------------------------------------
 *
 * @Class Name : MenuUserController
 * @Description : 메뉴아이콘 관리
 * @Version : 1.0 Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 *
 *               <pre>
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2016-03-31     고태영   최초작성
 * 2019-09-17     SaintJuny 시큐어 코딩 처리
 * ----------------------------------------------------------
 *               </pre>
 */
@Controller
public class MenuIconController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="menuIconService")
	private MenuIconService menuIconService;

	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
//	@Resource(name="propertiesService")
//	protected EgovPropertyService propertiesService;

	/** TRACE */
	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Resource(name="codeCmcdService")
	CodeCmcdService codeCmcdService;

	/*
	 * 메뉴아이콘 리스트
	 */
	@RequestMapping(value="/wrks/sstm/menu/icon.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();

		/* 아이콘구분 설정 */
		args.clear();
		args.put("cdGrpId", "ICON");
		args.put("cdTy", "C");
		args.put("orderBy", "ORDER BY CD_ID ASC");

		request.setAttribute("codeTyList", codeCmcdService.grpList(args));

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
		/* row per page 설정 끝 */

		return "wrks/sstm/menu/icon";
	}

	/*
	 * 메뉴아이콘 조건검색
	 */
	@RequestMapping(value="/wrks/sstm/menu/icon/list.json")
	@ResponseBody
	public Map<String, Object> list(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		String iconTy = (String) request.getParameter("iconTy");
		String pageNo = (String) request.getParameter("page");
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");
		String userId = (String) request.getParameter("userId");
		String rowsPerPage = (String) request.getParameter("rows");

		Map<String, String> args = new HashMap<String, String>();

		/* 파라미터 설정 */
		args.put("ICON_TY", iconTy);
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);
		args.put("userId", userId);

		List<Map<String, String>> list = menuIconService.list(args);

		map.put("rows", list);
		map.put("page", pageNo);

		return map;
	}

	/*
	 * 메뉴아이콘 입력/수정
	 */
	@RequestMapping(value="/wrks/sstm/menu/icon/image.upload")
	@ResponseBody
	public Map<String, Object> upload(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		request.setCharacterEncoding("UTF-8");
		final MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> mapRet = new HashMap<String, Object>();

		String seqNo = (String) request.getParameter("seqNo");

		map.put("SEQ_NO", (String) request.getParameter("seqNo"));
		map.put("ICON_TY", (String) request.getParameter("iconTy"));
		map.put("ON_ICON", (String) request.getParameter("onIcon"));
		map.put("OFF_ICON", (String) request.getParameter("offIcon"));

		map.put("UPD_USER_ID", sesUserId);
		map.put("RGS_USER_ID", sesUserId);

		String stateTag = (String) request.getParameter("state");

		// InsertMode
		if ("I".equals(stateTag)) {
			menuIconService.insert(map);
			int maxSeqNo = menuIconService.getMaxSeqNo();
			seqNo = String.valueOf(maxSeqNo);
		}
		else {
			menuIconService.update(map);
		}

		// 파일 업로드
		//String saveDir = propertiesService.getString("Globals.dirWrksHome").trim() + propertiesService.getString("Globals.dirIcon").trim();
		String saveDir = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_ICON");

		if (saveDir == null || saveDir.equals("")) {
			mapRet.put("session", 1);
			mapRet.put("msg", "이미지 파일을 저장할 위치 정보가 없습니다.");
			return mapRet;
		}

		final Map<String, MultipartFile> files = multiRequest.getFileMap();
		InputStream fis = null;

		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		MultipartFile file;

		while (itr.hasNext()) {

			Entry<String, MultipartFile> entry = itr.next();

			file = entry.getValue();

			String originFileName = file.getOriginalFilename();
			if (originFileName != null) {
				// 업로드 파일에 대한 확장자를 체크
				if (originFileName.toUpperCase().endsWith(".PNG")) {
					String fileName = "";

					if (file.getName().indexOf("ON_ICON") != -1) {
						fileName = saveDir + File.separator + seqNo + "_on.png";
					}
					else if (file.getName().indexOf("OFF_ICON") != -1) {
						fileName = saveDir + File.separator + seqNo + "_off.png";
					}
					/*
					 * fileName = fileName.replaceAll("/", ""); fileName = fileName.replaceAll("\\\\",""); fileName = fileName.replaceAll("&", "");
					 */
					OutputStream fos = new FileOutputStream(fileName);

					fis = file.getInputStream();

					try {
						byte[] buffer = new byte[1024];

						while (true) {
							int count = fis.read(buffer);
							if (count == -1) {
								break;
							}
							fos.write(buffer, 0, count);
						}
					}
					catch (Exception e) {
						throw e;
					}
					finally {
						if (fis != null) {
							fis.close();
						}
						if (fos != null) {
							fos.close();
						}
					}

				}
				else {
					mapRet.put("session", 1);
					mapRet.put("msg", "처리할 수 없는 파일입니다.");
					return mapRet;
				}
			}
		}
		mapRet.put("session", 1);
		mapRet.put("msg", "저장하였습니다.");

		return mapRet;
	}

	/*
	 * RSS 삭제
	 */

	@RequestMapping(value="/wrks/sstm/menu/icon/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("UPD_USER_ID", sesUserId);
		map.put("SEQ_NO", request.getParameter("seqNo"));

		int result = menuIconService.delete(map);

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

	/*
	 * RSS 다중삭제
	 */

	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/menu/icon/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();
		String[] seqNoArr = request.getParameterValues("seqNo");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < seqNoArr.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("UPD_USER_ID", sesUserId);
			mapId.put("SEQ_NO", seqNoArr[i]);
			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = menuIconService.deleteMulti(list);
			mapRet.put("session", 1);
			mapRet.put("msg", "삭제하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			logger.error("deleteMulti DataIntegrityViolationException : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 에러가 발생했습니다.");
			}
			else {
				logger.error("deleteMulti UncategorizedSQLException : {}", e.getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 오류가 발생했습니다.");
			}
		}
		catch (Exception e) {
			logger.error("deleteMulti Exception : {}", e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}
}