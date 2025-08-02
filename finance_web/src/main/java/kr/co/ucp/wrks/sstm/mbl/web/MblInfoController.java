package kr.co.ucp.wrks.sstm.mbl.web;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.cmm.util.ExcelValue;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.mbl.service.MblInfoService;

/**
 * 모바일 기기의 정보를 관리
 *
 * @author 대전도안 김정원
 * @version 1.00 2014-01-25
 * @since JDK 1.7.0_45(x64)
 * @revision
 */

/**
 * ----------------------------------------------------------
 *
 * @Class Name : MblInfoController
 * @Description : 모바일 기기정보
 * @Version : 1.0 Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 *
 *               <pre>
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-01-22   김정원       최초작성
 *
 * ----------------------------------------------------------
 *               </pre>
 */
@Controller
public class MblInfoController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	/** TRACE */
	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Resource(name="mblInfoService")
	private MblInfoService mblInfoService;

	@Resource(name="codeCmcdService")
	private CodeCmcdService codeCmcdService;

	/*
	 * 기기정보 리스트
	 */
	@RequestMapping(value="/wrks/sstm/mbl/info.do")
	public String loginUsrView(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		/* 모바일기기 종류 */
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("cdGrpId", "MOBL");
		args.put("cdTy", "C");
		args.put("orderBy", "ORDER BY CD_ID ASC");

		request.setAttribute("mppList", codeCmcdService.grpList(args));

		/* 모바일기기 OS유형 */
		args.clear();
		args.put("cdGrpId", "MOBL_OS");
		args.put("cdTy", "C");
		args.put("orderBy", "ORDER BY CD_ID ASC");
		request.setAttribute("mppOsList", codeCmcdService.grpList(args));

		/* 요일코드 */
		args.clear();
		args.put("cdGrpId", "DAY_TY_CD");
		args.put("cdTy", "C");
		args.put("orderBy", "ORDER BY CD_ID ASC");
		request.setAttribute("dayTyList", codeCmcdService.grpList(args));

		request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/* 사용유형 */

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());
		/* row per page 설정 끝 */

		return "wrks/sstm/mbl/info";
	}

	@RequestMapping(value="/wrks/sstm/mbl/info/getUser.json")
	@ResponseBody
	public Map<String, String> getUser(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = (String) request.getSession().getAttribute("userId");

		if (userId == null) {
			userId = "userId1";
		}

		Map<String, String> mapD = mblInfoService.getUser(userId);

		return mapD;
	}

	/*
	 * 기기정보 리스트
	 */
	@RequestMapping(value="/wrks/sstm/mbl/info/list.json")
	@ResponseBody
	public Map<String, Object> getList(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String moblNo = (String) request.getParameter("moblNo");
		String moblKndCd = (String) request.getParameter("moblKndCd");
		String userNm = (String) request.getParameter("userNm");
		String useTyCd = (String) request.getParameter("useTyCd");
		String pageNo = (String) request.getParameter("page");
		String rowsPerPage = (String) request.getParameter("rows");
		String sidx = (String) request.getParameter("sidx");
		String sord = (String) request.getParameter("sord");

		Map<String, String> args = new HashMap<String, String>();

		args.put("MOBL_NO", moblNo);
		args.put("moblKndCd", moblKndCd);
		args.put("userNm", userNm);
		args.put("useTyCd", useTyCd);
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = mblInfoService.getList(args);

		map.put("page", pageNo);
		map.put("rows", resultRows);

		return map;
	}

	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/info/listdetail.json")
	@ResponseBody
	public Map<String, Object> getListDetail(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String mppId = (String) request.getParameter("strMppId");

		Map<String, Object> map = new HashMap<String, Object>();

		List<Map<String, String>> resultRows = mblInfoService.getListDetail(mppId);

		return map;
	}

	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/info/listmaptype.json")
	@ResponseBody
	public Map<String, Object> getListMapType(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = mblInfoService.getListMapType();

		return map;
	}

	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/info/listostype.json")
	@ResponseBody
	public Map<String, Object> getListOsType(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> resultRows = mblInfoService.getListOsType();

		return map;
	}

	/*
	 * 기기정보 입력
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/info/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		String moblId = java.util.UUID.randomUUID().toString();

		/* 모바일기기 정보등록 */
		map.put("moblId", moblId);
		map.put("moblNo", (String) request.getParameter("moblNo"));
		map.put("moblKndCd", (String) request.getParameter("moblKndCd"));
		map.put("moblOsTyCd", (String) request.getParameter("moblOsTyCd"));
		map.put("userNm", (String) request.getParameter("userNm"));
		map.put("userPsitnNm", (String) request.getParameter("userPsitnNm"));
		map.put("useTyCd", (String) request.getParameter("useTyCd"));
		map.put("userEmail", (String) request.getParameter("userEmail"));
		map.put("pushId", (String) request.getParameter("pushId"));
		map.put("moblUuid", (String) request.getParameter("moblUuid"));
		map.put("etcInfo", (String) request.getParameter("etcInfo"));
		map.put("rgsUserId", sesUserId);
		map.put("updUserId", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		/* 수신시간등록 */
		String[] dayTyCd = request.getParameterValues("dayTyCd");
		String[] rcvStartHh = request.getParameterValues("rcvStartHh");
		String[] rcvEndHh = request.getParameterValues("rcvEndHh");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < dayTyCd.length; i++) {
			Map<String, String> mapTime = new HashMap<String, String>();

			mapTime.put("moblId", moblId);
			mapTime.put("dayTyCd", dayTyCd[i]);
			mapTime.put("rcvStartHh", rcvStartHh[i]);
			mapTime.put("rcvEndHh", rcvEndHh[i]);
			mapTime.put("rgsUserId", sesUserId);

			if (!CommonUtil.checkDataFilter(mapTime)) {
				mapRet.put("session", 1);
				mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
				return mapRet;
			}

			list.add(mapTime);
		}

		int insertResult;
		try {
			insertResult = mblInfoService.insert(map, list);
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			if (e.getRootCause().getMessage().indexOf("UNIQUE constraint violation") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 코드입니다.");
				return mapRet;
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
				mapRet.put("msg", "이미 등록된 아이디입니다.");
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

	/*
	 * 기기정보 수정
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/info/update.json")
	@ResponseBody
	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		String moblId = (String) request.getParameter("moblId");

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("moblNo", (String) request.getParameter("moblNo"));
		map.put("moblKndCd", (String) request.getParameter("moblKndCd"));
		map.put("moblOsTyCd", (String) request.getParameter("moblOsTyCd"));
		map.put("userNm", (String) request.getParameter("userNm"));
		map.put("userPsitnNm", (String) request.getParameter("userPsitnNm"));
		map.put("useTyCd", (String) request.getParameter("useTyCd"));
		map.put("userEmail", (String) request.getParameter("userEmail"));
		map.put("pushId", (String) request.getParameter("pushId"));
		map.put("moblUuid", (String) request.getParameter("moblUuid"));
		map.put("moblId", moblId);
		map.put("etcInfo", (String) request.getParameter("etcInfo"));
		map.put("rgsUserId", sesUserId);
		map.put("updUserId", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int ret = mblInfoService.update(map);

		if (ret > 0) {
			String[] dayTyCd = request.getParameterValues("dayTyCd");
			String[] rcvStartHh = request.getParameterValues("rcvStartHh");
			String[] rcvEndHh = request.getParameterValues("rcvEndHh");

			List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			for (int i = 0; i < dayTyCd.length; i++) {
				Map<String, String> mapTime = new HashMap<String, String>();

				mapTime.put("MOBL_ID", moblId);
				mapTime.put("DAY_TY_CD", dayTyCd[i]);
				mapTime.put("RCV_START_HH", rcvStartHh[i]);
				mapTime.put("RCV_END_HH", rcvEndHh[i]);
				mapTime.put("UPD_USER_ID", sesUserId);

				list.add(mapTime);
			}
			int res = mblInfoService.updateRcvTime(list);
		}
		mapRet.put("session", 1);
		mapRet.put("msg", "업데이트하였습니다.");

		return mapRet;
	}

	/*
	 * 기기정보 삭제
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/info/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("moblIdBak", (String) request.getParameter("moblId"));
		map.put("updUserId", sesUserId);

		/* 모바일 계정정보 삭제 */
		int ret = mblInfoService.delete(map);

		if (ret > 0) {
			/* 모바일 메세지 수신시간 삭제 */
			int res = mblInfoService.deleteRcvTime(map);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();

		mapRet.put("session", 1);
		mapRet.put("msg", "삭제하였습니다.");

		return mapRet;
	}

	/*
	 * 기기정보 다중삭제
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/mbl/info/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		String[] moblIds = request.getParameterValues("moblId");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < moblIds.length; i++) {
			Map<String, Object> mapId = new HashMap<String, Object>();

			mapId.put("moblIdBak", moblIds[i]);
			mapId.put("updUserId", sesUserId);

			list.add(mapId);
		}

		int ret = mblInfoService.deleteMulti(list);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		mapRet.put("session", 1);
		mapRet.put("msg", "삭제하였습니다.");

		return mapRet;
	}

	/*
	 * 기기정보 엑셀업로드
	 */
	@RequestMapping(value = "/wrks/sstm/mbl/info/upload.excel")
	@ResponseBody
	public Map<String, Object> excelUpload(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		final MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		final Map<String, MultipartFile> files = multiRequest.getFileMap();

		InputStream fis = null;

		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		MultipartFile file;

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, Object> mapRet = new HashMap<String, Object>();

		int inCnt = 0;
		while (itr.hasNext()) {
			Entry<String, MultipartFile> entry = itr.next();

			file = entry.getValue();

			if (!"".equals(file.getOriginalFilename())) {
				// 업로드 파일에 대한 확장자를 체크
				if (file.getOriginalFilename().endsWith(".xls") || file.getOriginalFilename().endsWith(".XLS") || file.getOriginalFilename().endsWith(".xlsx") || file.getOriginalFilename().endsWith(".XLSX")) {

					try {
						fis = file.getInputStream();

						Workbook work = WorkbookFactory.create(fis);

						Sheet sheet = work.getSheetAt(0);

						int rows = sheet.getLastRowNum();

						for (int rownum = 1; rownum <= rows; rownum++) {
							Row row = sheet.getRow(rownum);

							if (row != null) {
								int cells = row.getPhysicalNumberOfCells();

								Map<String, String> map = new HashMap<String, String>();
								map.put("moblId", java.util.UUID.randomUUID().toString());

								for (int cellnum = 0; cellnum < cells; cellnum++) {
									Cell cell = row.getCell(cellnum);

									String colName = "";
									switch (cellnum) {
										case 0:
											colName = "moblNo";
											break;
										case 1:
											colName = "moblKndCd";
											break;
										case 2:
											colName = "moblOsTyCd";
											break;
										case 3:
											colName = "userNm";
											break;
										case 4:
											colName = "userPsitnNm";
											break;
										case 5:
											colName = "userEmail";
											break;
										case 6:
											colName = "useTyCd";
											break;
										case 7:
											colName = "pushId";
											break;
										case 8:
											colName = "moblUuid";
											break;
										default:
											break;
									}

									if (cell != null) {
										map.put(colName, ExcelValue.getStrCellVal(cell));
//										switch (cell.getCellType()) {
//											case XSSFCell.CELL_TYPE_FORMULA:
//												map.put(colName, String.valueOf(cell.getNumericCellValue()));
//												break;
//
//											case XSSFCell.CELL_TYPE_STRING:
//												map.put(colName, cell.getStringCellValue());
//												break;
//
//											case HSSFCell.CELL_TYPE_BLANK:
//												map.put(colName, String.valueOf(cell.getBooleanCellValue()));
//												break;
//
//											case XSSFCell.CELL_TYPE_ERROR:
//												map.put(colName, String.valueOf(cell.getErrorCellValue()));
//												break;
//											default:
//												break;
//										}
									}
								}

								map.put("rgsUserId", sesUserId);
								map.put("updUserId", sesUserId);
								if(map.get("userNm") != null && map.get("moblNo") != null) {
								list.add(map);
								}
							}
						}

						Map<String, Object> mapCnt = mblInfoService.excelUpload(list);

						if ((Integer) mapCnt.get("error") != 1) {
							mapRet.put("session", 0);
							mapRet.put("msg", "업로드 시 에러가 발생하였습니다. 관리자에게 문의하세요.");
							return mapRet;
						}
						inCnt = mapCnt.get("inCnt") == null ? 0 : (Integer) mapCnt.get("inCnt");

						mapRet.put("session", 1);
						mapRet.put("msg", "저장하였습니다.( 입력 : " + list.size() + " / 추가,수정 : " + inCnt + " )");
					}
					catch (Exception e) {
						throw e;
					}
					finally {
						if (fis != null) {
							fis.close();
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
		return mapRet;
	}

	/*
	 * 기기정보 모바일 수신 시간조회
	 */
	@RequestMapping(value="/wrks/sstm/mbl/info/rcvTime.json")
	@ResponseBody
	public Map<String, Object> rcvTime(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();

		args.put("MOBL_ID", (String) request.getParameter("moblId"));
		args.put("div", (String) request.getParameter("div"));

		List<Map<String, String>> list = mblInfoService.rcvTimeList(args);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("session", 1);
		map.put("rows", list);

		return map;
	}

	@RequestMapping(value = "/wrks/sstm/mbl/info/userList.json")
	@ResponseBody
	public Map<String, Object> userList(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> args = new HashMap<String, Object>();

		args.put("userId", EgovStringUtil.nullConvert(request.getParameter("userId")));
		args.put("grpId", EgovStringUtil.nullConvert(request.getParameter("grpId")));
		List<Map<String, String>> list = mblInfoService.getUserList(args);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("session", 1);
		map.put("rows", list);

		return map;
	}
	
}
