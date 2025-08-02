package kr.co.ucp.money.web;

import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.CmmService;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.cmm.util.ExcelValue;
import kr.co.ucp.egov.com.utl.fcc.service.EgovDateUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.env.service.AcctgbService;
import kr.co.ucp.env.service.DeptService;
import kr.co.ucp.env.service.MoneyService;
import kr.co.ucp.env.service.PositionService;
import kr.co.ucp.env.service.RegionService;
import kr.co.ucp.env.service.WorshipService;
import kr.co.ucp.money.service.MoneymngService;

@Controller
public class MoneymngController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="cmmService")
	CmmService cmmService;
	
	@Resource(name="moneymngService")
	MoneymngService moneymngService;

	@Resource(name="acctgbService")
	AcctgbService acctgbService;

	@Resource(name="moneyService")
	MoneyService moneyService;

	@Resource(name="worshipService")
	WorshipService worshipService;

	@Resource(name="positionService")
	PositionService positionService;

	@Resource(name="deptService")
	DeptService deptService;

	@Resource(name="regionService")
	RegionService regionService;

	// 리스트
	@RequestMapping(value="/money/mng.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

        String toDate = EgovDateUtil.getToday();
        request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
        
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"       , "ASC");

		args.put("sidx"       , "ACCT_GB");
		request.setAttribute("AcctgbList", acctgbService.selectAcctgb(args));		// 재정

		args.put("stanYmd"    , EgovDateUtil.getToday());
		args.put("sidx"       , "MONEY_CODE");
		request.setAttribute("moneyList", moneyService.selectMoney(args));		// 헌금

		args.put("sidx"       , "WORSHIP_CODE");
		request.setAttribute("worshipList", worshipService.selectWorship(args));		// 예배
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "money/moneymng";
	}

	// 조건검색
	@RequestMapping(value="/money/mng/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"     , (String) request.getParameter("stanYmd"));
		args.put("startYmd"    , (String) request.getParameter("startYmd"));
		args.put("endYmd"      , (String) request.getParameter("endYmd"));
		args.put("acctGb"      , (String) request.getParameter("acctGb"));
		args.put("moneyCode"   , (String) request.getParameter("moneyCode"));
		args.put("worshipCode" , (String) request.getParameter("worshipCode"));
		args.put("positionCode", (String) request.getParameter("positionCode"));
		args.put("deptCode"    , (String) request.getParameter("deptCode"));
		args.put("regionCode"  , (String) request.getParameter("regionCode"));
		args.put("memberNo"    , (String) request.getParameter("memberNo"));
		args.put("memberName"  , (String) request.getParameter("memberName"));
		args.put("userId"      , (String) request.getParameter("userId"));
		
		if( "true".equalsIgnoreCase((String) request.getParameter("userIdYn"))) {
			LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			args.put("userId"     , lgnVO.getUserId());
		}

		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = moneymngService.selectMoneyMng(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 합계
	@SuppressWarnings("unused")
	@RequestMapping(value="/money/mng/getSum.json")
	@ResponseBody
	public Map<String, Object> getSum(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"     , (String) request.getParameter("stanYmd"));
		args.put("startYmd"    , (String) request.getParameter("startYmd"));
		args.put("endYmd"      , (String) request.getParameter("endYmd"));
		args.put("acctGb"      , (String) request.getParameter("acctGb"));
		args.put("moneyCode"   , (String) request.getParameter("moneyCode"));
		args.put("worshipCode" , (String) request.getParameter("worshipCode"));
		args.put("positionCode", (String) request.getParameter("positionCode"));
		args.put("deptCode"    , (String) request.getParameter("deptCode"));
		args.put("regionCode"  , (String) request.getParameter("regionCode"));
		args.put("memberNo"    , (String) request.getParameter("memberNo"));
		args.put("memberName"  , (String) request.getParameter("memberName"));
		args.put("userId"      , (String) request.getParameter("userId"));
		
		if( "true".equalsIgnoreCase((String) request.getParameter("userIdYn"))) {
			LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			args.put("userId"     , lgnVO.getUserId());
		}
		
		Map<String, Object> mapRet = new HashMap<String, Object>();

		try {
			String totalAmnt = moneymngService.selectTotalAmnt(args);
			mapRet.put("session", 1);
			mapRet.put("totalAmnt", totalAmnt);
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	// 엑셀
	@RequestMapping(value="/money/mng/excel.json")
	public void excel(ModelMap model, @RequestParam Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"     , (String) request.getParameter("stanYmd"));
		args.put("startYmd"    , (String) request.getParameter("startYmd"));
		args.put("endYmd"      , (String) request.getParameter("endYmd"));
		args.put("acctGb"      , (String) request.getParameter("acctGb"));
		args.put("moneyCode"   , (String) request.getParameter("moneyCode"));
		args.put("worshipCode" , (String) request.getParameter("worshipCode"));
		args.put("positionCode", (String) request.getParameter("positionCode"));
		args.put("deptCode"    , (String) request.getParameter("deptCode"));
		args.put("regionCode"  , (String) request.getParameter("regionCode"));
		args.put("memberNo"    , (String) request.getParameter("memberNo"));
		args.put("memberName"  , (String) request.getParameter("memberName"));
		args.put("userId"      , (String) request.getParameter("userId"));
		
		if( "true".equalsIgnoreCase((String) request.getParameter("userIdYn"))) {
			LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			args.put("userId"     , lgnVO.getUserId());
		}

		args.put("pageNo"	  , "1");
		args.put("rowsPerPage", "9999999");
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = moneymngService.selectMoneyMng(args);

		model.put("title", "");	//model.put("title", "헌금입력");
		model.put("fileName", "헌금입력") ;
		model.put("titleKey", EgovStringUtil.nullConvert(map.get("titleKey")));
		model.put("titleHeader", URLDecoder.decode(EgovStringUtil.nullConvert(map.get("titleHeader")), "utf-8"));
		model.put("dataList", list);

        cmmService.buildExcelDocument(model, request, response);
	}

	// 등록
	@SuppressWarnings("unused")
	@RequestMapping(value="/money/mng/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stanYmd"    , EgovStringUtil.nullConvert(request.getParameter("stanYmd")));
		map.put("moneyCode"  , EgovStringUtil.nullConvert(request.getParameter("moneyCode")));
		map.put("worshipCode", EgovStringUtil.nullConvert(request.getParameter("worshipCode")));
		map.put("infoType"   , EgovStringUtil.nullConvert(request.getParameter("infoType")));
		map.put("infoData"   , EgovStringUtil.nullConvert(request.getParameter("infoData")));
		map.put("userId"     , sesUserId);
		//logger.info(map.toString());
		
		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int insertResult;
		try {
			insertResult = moneymngService.insertMoneyMng(map);
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
				logger.error(e.getRootCause().getMessage());
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

	// 수정
	@SuppressWarnings("unused")
	@RequestMapping(value="/money/mng/update.json")
	@ResponseBody
	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stanYmd"    , EgovStringUtil.nullConvert(request.getParameter("stanYmd")));
		map.put("moneyCode"  , EgovStringUtil.nullConvert(request.getParameter("moneyCode")));
		map.put("detSeq"     , EgovStringUtil.nullConvert(request.getParameter("detSeq")));
		
		if ( !"".equalsIgnoreCase(EgovStringUtil.nullConvert(request.getParameter("newStanYmd")))) {
			map.put("newStanYmd"  , EgovStringUtil.nullConvert(request.getParameter("newStanYmd")));		// 기준일자 변경
			map.put("newMoneyCode"  , EgovStringUtil.nullConvert(request.getParameter("newMoneyCode")));	// 헌금분류 변경
			
		} else if ( !"".equalsIgnoreCase(EgovStringUtil.nullConvert(request.getParameter("newMoneyCode")))) {
			map.put("newMoneyCode"  , EgovStringUtil.nullConvert(request.getParameter("newMoneyCode")));	// 헌금분류 변경
		}
				
		map.put("worshipCode", EgovStringUtil.nullConvert(request.getParameter("worshipCode")));
		map.put("moneyAmt"   , EgovStringUtil.nullConvert(request.getParameter("moneyAmt")));

		map.put("memberId"   , EgovStringUtil.nullConvert(request.getParameter("memberId")));
		map.put("memberName"   , EgovStringUtil.nullConvert(request.getParameter("memberName")));
		map.put("idExist"   , EgovStringUtil.nullConvert(request.getParameter("idExist")));
		
		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 1);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		int updateResult;
		try {
			updateResult = moneymngService.updateMoneyMng(map);
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
				logger.error(e.getRootCause().getMessage());
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

	// 삭제
	@RequestMapping(value="/money/mng/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		map.put("stanYmd"   , EgovStringUtil.nullConvert(request.getParameter("stanYmd")));
		map.put("moneyCode" , EgovStringUtil.nullConvert(request.getParameter("moneyCode")));
		map.put("detSeq"    , EgovStringUtil.nullConvert(request.getParameter("detSeq")));

		int result = moneymngService.deleteMoneyMng(map);

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
	@RequestMapping(value="/money/mng/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		String[] moneyInfo = request.getParameterValues("moneyInfo");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < moneyInfo.length; i++) {
			String[] info = moneyInfo[i].split(":");
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("stanYmd", info[0]);
			mapId.put("moneyCode", info[1]);
			mapId.put("detSeq", info[2]);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = moneymngService.deleteMoneyMngMulti(list);
			mapRet.put("session", 1);
			mapRet.put("msg", "삭제하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			logger.error(e.getRootCause().getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 에러가 발생했습니다.");
			}
			else {
				logger.error(e.getRootCause().getMessage());
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

	// 다중수정
	@SuppressWarnings("unused")
	@RequestMapping(value="/money/mng/modifyMulti.json")
	@ResponseBody
	public Map<String, Object> modifyMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		
		String[] moneyInfo = request.getParameterValues("moneyInfo");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < moneyInfo.length; i++) {
			String[] info = moneyInfo[i].split(":");
			Map<String, String> mapId = new HashMap<String, String>();
			
			mapId.put("newWorshipCode", EgovStringUtil.nullConvert(request.getParameter("newWorshipCode")));
			mapId.put("newMoneyCode", EgovStringUtil.nullConvert(request.getParameter("newMoneyCode")));
			
			mapId.put("stanYmd", info[0]);
			mapId.put("moneyCode", info[1]);
			mapId.put("detSeq", info[2]);
			
			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = moneymngService.modifyMoneyMngMulti(list);
			mapRet.put("session", 1);
			mapRet.put("msg", "수정하였습니다.");
		}
		catch (DataIntegrityViolationException e) {
			logger.error(e.getRootCause().getMessage());
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		catch (UncategorizedSQLException e) {
			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
				mapRet.put("session", 0);
				mapRet.put("msg", "처리중 에러가 발생했습니다.");
			}
			else {
				logger.error(e.getRootCause().getMessage());
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

	// 엑셀업로드
	@RequestMapping(value = "/money/mng/upload.excel")
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
								//map.put("moblId", java.util.UUID.randomUUID().toString());

								for (int cellnum = 0; cellnum < cells; cellnum++) {
									Cell cell = row.getCell(cellnum);

									String colName = "";
									switch (cellnum) {
										case 0:		colName = "stanYmd";		break;
										case 1:		colName = "moneyCode";		break;
										case 2:		colName = "worshipCode";	break;
										case 3:		colName = "memberName";		break;
										case 4:		colName = "moneyAmt";		break;
										case 5:		colName = "memberNo";		break;
										default:								break;
									}
									if (cell != null) {
										String cellValue = ExcelValue.getStrCellVal(cell);
										cellValue = cellValue.replaceAll(" ", "");
										map.put(colName, cellValue);
									}
								}

								map.put("rgsUserId", sesUserId);
								//map.put("updUserId", sesUserId);
								logger.debug(map.toString());
								
								if(map.get("memberName") != null && map.get("moneyAmt") != null) {
									list.add(map);
								}
							}
						}

						Map<String, Object> mapCnt = moneymngService.excelUpload(list);

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

}
