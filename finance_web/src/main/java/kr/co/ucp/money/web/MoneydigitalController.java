package kr.co.ucp.money.web;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.ucp.cmm.LoginVO;
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
import kr.co.ucp.money.service.MoneydigitalService;

@Controller
public class MoneydigitalController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="moneydigitalService")
	MoneydigitalService moneydigitalService;

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
	@RequestMapping(value="/money/digital.do")
	public String view(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

        String toDate = EgovDateUtil.getToday();
        request.setAttribute("currentDay", EgovDateUtil.formatDate(toDate, "-"));
        //request.setAttribute("currentDay", "2024-03-24");
        
		Map<String, String> args = new HashMap<String, String>();
		args.put("pageNo"     , "1");
		args.put("rowsPerPage", "999");
		args.put("sord"       , "ASC");
		args.put("sidx"       , "ACCT_GB");
		request.setAttribute("AcctgbList", acctgbService.selectAcctgb(args));		// 재정
		
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "money/moneydigital";
	}

	// 조건검색
	@RequestMapping(value="/money/digital/list.json")
	@ResponseBody
	public Map<String, Object> list(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"     , (String) request.getParameter("stanYmd"));
		args.put("stanGb"      , (String) request.getParameter("stanGb"));
		args.put("acctGb"      , (String) request.getParameter("acctGb"));
		args.put("birthDay"    , (String) request.getParameter("birthDay"));
		args.put("memberName"  , (String) request.getParameter("memberName"));
		args.put("closeYn"     , (String) request.getParameter("closeYn"));
		//if( "true".equalsIgnoreCase((String) request.getParameter("noClose"))) {
		//	args.put("closeYn"     , "N");
		//}
		
		args.put("pageNo"     , (String) request.getParameter("page"));
		args.put("rowsPerPage", (String) request.getParameter("rows"));
		args.put("sidx"       , (String) request.getParameter("sidx"));
		args.put("sord"       , (String) request.getParameter("sord"));
		
		logger.debug(args.toString());

		List<Map<String, String>> list = moneydigitalService.selectMastUpload(args);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("page", (String) request.getParameter("page"));

		return map;
	}

	// 합계
	@SuppressWarnings("unused")
	@RequestMapping(value="/money/digital/getSum.json")
	@ResponseBody
	public Map<String, Object> getSum(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> args = new HashMap<String, String>();
		args.put("stanYmd"     , (String) request.getParameter("stanYmd"));
		args.put("stanGb"      , (String) request.getParameter("stanGb"));
		args.put("acctGb"      , (String) request.getParameter("acctGb"));
		args.put("birthDay"    , (String) request.getParameter("birthDay"));
		args.put("memberName"  , (String) request.getParameter("memberName"));
		args.put("closeYn"     , (String) request.getParameter("closeYn"));
		//if( "true".equalsIgnoreCase((String) request.getParameter("noClose"))) {
		//	args.put("closeYn"     , "N");
		//}
		
		Map<String, Object> mapRet = new HashMap<String, Object>();

		try {
			String totalAmnt = moneydigitalService.selectTotalAmnt(args);
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

	// 등록
	@SuppressWarnings("unused")
	@RequestMapping(value="/money/digital/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, String> map = new HashMap<String, String>();

		map.put("memberId", EgovStringUtil.nullConvert(request.getParameter("memberId")));
		map.put("memberNo", EgovStringUtil.nullConvert(request.getParameter("memberNo")));
		map.put("memberName", EgovStringUtil.nullConvert(request.getParameter("memberName")));
		map.put("sexType", EgovStringUtil.nullConvert(request.getParameter("sexType")));
		map.put("telNo", EgovStringUtil.nullConvert(request.getParameter("telNo")));
		map.put("hphoneNo", EgovStringUtil.nullConvert(request.getParameter("hphoneNo")));
		map.put("positionCode", EgovStringUtil.nullConvert(request.getParameter("positionCode")));
		map.put("deptCode", EgovStringUtil.nullConvert(request.getParameter("deptCode")));
		map.put("regionCode", EgovStringUtil.nullConvert(request.getParameter("regionCode")));
		map.put("addr", EgovStringUtil.nullConvert(request.getParameter("addr")));
		map.put("familyRemark", EgovStringUtil.nullConvert(request.getParameter("familyRemark")));


		Map<String, Object> mapRet = new HashMap<String, Object>();

//		if (!CommonUtil.checkDataFilterObj(map)) {
//			mapRet.put("session", 1);
//			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
//			return mapRet;
//		}

		int insertResult;
		try {
			insertResult = moneydigitalService.insertMastUpload(map);
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
//	@SuppressWarnings("unused")
//	@RequestMapping(value="/money/digital/update.json")
//	@ResponseBody
//	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
//
//		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
//		String sesUserId = lgnVO.getUserId();
//
//		Map<String, Object> map = new HashMap<String, Object>();
//
//		map.put("memberId", EgovStringUtil.nullConvert(request.getParameter("memberId")));
//		map.put("memberNo", EgovStringUtil.nullConvert(request.getParameter("memberNo")));
//		map.put("memberName", EgovStringUtil.nullConvert(request.getParameter("memberName")));
//		map.put("sexType", EgovStringUtil.nullConvert(request.getParameter("sexType")));
//		map.put("telNo", EgovStringUtil.nullConvert(request.getParameter("telNo")));
//		map.put("hphoneNo", EgovStringUtil.nullConvert(request.getParameter("hphoneNo")));
//		map.put("positionCode", EgovStringUtil.nullConvert(request.getParameter("positionCode")));
//		map.put("deptCode", EgovStringUtil.nullConvert(request.getParameter("deptCode")));
//		map.put("regionCode", EgovStringUtil.nullConvert(request.getParameter("regionCode")));
//		map.put("addr", EgovStringUtil.nullConvert(request.getParameter("addr")));
//		map.put("familyRemark", EgovStringUtil.nullConvert(request.getParameter("familyRemark")));
//
//		Map<String, Object> mapRet = new HashMap<String, Object>();
//
//		if (!CommonUtil.checkDataFilterObj(map)) {
//			mapRet.put("session", 1);
//			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
//			return mapRet;
//		}
//
//		int updateResult;
//		try {
//			updateResult = moneydigitalService.updateMastUpload(map);
//			mapRet.put("session", 1);
//			mapRet.put("msg", "저장하였습니다.");
//		}
//		catch (DataIntegrityViolationException e) {
//			logger.error(e.getRootCause().getMessage());
//			mapRet.put("session", 0);
//			mapRet.put("msg", "처리중 오류가 발생했습니다.");
//		}
//		catch (UncategorizedSQLException e) {
//			if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
//				mapRet.put("session", 0);
//				mapRet.put("msg", "이미 등록된 코드입니다.");
//			}
//			else {
//				logger.error(e.getRootCause().getMessage());
//				mapRet.put("session", 0);
//				mapRet.put("msg", "처리중 오류가 발생했습니다.");
//			}
//		}
//		catch (Exception e) {
//			logger.error(e.getMessage());
//			mapRet.put("session", 0);
//			mapRet.put("msg", "처리중 오류가 발생했습니다.");
//		}
//		return mapRet;
//	}

	// 삭제
	@RequestMapping(value="/money/digital/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		
		map.put("memberId", EgovStringUtil.nullConvert(request.getParameter("memberId")));
		map.put("memberNo", EgovStringUtil.nullConvert(request.getParameter("memberNo")));

		int result = moneydigitalService.deleteMastUpload(map);

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
	@RequestMapping(value="/money/digital/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		String[] moneyInfo = request.getParameterValues("moneyInfo");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < moneyInfo.length; i++) {
			String[] info = moneyInfo[i].split(":");
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("stanYmd", info[0]);
			mapId.put("stanGb", info[1]);
			mapId.put("seqNo", info[2]);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = moneydigitalService.deleteMastUploadMulti(list);
			mapRet.put("session", 1);
			mapRet.put("msg", "삭제하였습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	// 헌금등록처리
	@SuppressWarnings("unused")
	@RequestMapping(value="/money/digital/register.json")
	@ResponseBody
	public Map<String, Object> register(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		String[] moneyInfo = request.getParameterValues("moneyInfo");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < moneyInfo.length; i++) {
			String[] info = moneyInfo[i].split(":");
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("stanYmd", info[0]);
			mapId.put("stanGb", info[1]);
			mapId.put("seqNo", info[2]);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = moneydigitalService.insertMastMoneyMulti(list);
			mapRet.put("session", 1);
			mapRet.put("msg", "등록처리하였습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	// 헌금등록취소
	@SuppressWarnings("unused")
	@RequestMapping(value="/money/digital/remove.json")
	@ResponseBody
	public Map<String, Object> remove(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		String[] moneyInfo = request.getParameterValues("moneyInfo");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < moneyInfo.length; i++) {
			String[] info = moneyInfo[i].split(":");
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("stanYmd", info[0]);
			mapId.put("stanGb", info[1]);
			mapId.put("seqNo", info[2]);

			list.add(mapId);
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		int ret;
		try {
			ret = moneydigitalService.deleteMastMoneyMulti(list);
			if ( ret == 0 ) {
				mapRet.put("session", 1);
				mapRet.put("msg", "이미 마감처리되어 등록을 취소할 수 없습니다.");
			} else {
				mapRet.put("session", 1);
				mapRet.put("msg", "등록취소하였습니다.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("session", 0);
			mapRet.put("msg", "처리중 오류가 발생했습니다.");
		}
		return mapRet;
	}

	// 엑셀업로드
	@RequestMapping(value = "/money/digital/upload.excel")
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
								int cells = row.getPhysicalNumberOfCells();	logger.debug("physicalNumberOfCells => {}",cells);

								Map<String, String> map = new HashMap<String, String>();
								//map.put("moblId", java.util.UUID.randomUUID().toString());
								
								for (int cellnum = 0; cellnum < cells; cellnum++) {
									Cell cell = row.getCell(cellnum);

									String colName = "";
									switch (cellnum) {
										case 0:		colName = "stanYmd";		break;	// 기준일자
										case 1:		colName = "stanGb";			break;	// 헌금구분:1(일반),2(청년)
										case 2:		colName = "moneyTime";		break;	// 헌금일시
										case 3:		colName = "memberName";		break;	// 회원명
										case 4:		colName = "birthDay";		break;	// 생년월일
										case 5:		colName = "moneyAmt";		break;	// 기부금액
										case 6:		colName = "hpNo";			break;	// 휴대폰번호
										case 7:		colName = "moneyType";		break;	// 헌금종류번호
									/*	case 8:		colName = "termGb";			break;
										case 9:		colName = "settleGb";		break;
										case 10:	colName = "prayTitle";		break;
										case 11:	colName = "etcRemark";		break;
										case 12:	colName = "";				break;
										case 13:	colName = "";				break;
										case 14:	colName = "";				break;	*/
										default:								break;
									}
									if ( !"".equalsIgnoreCase(colName)) {
										String val = "";
										if (cell != null) {
											val = EgovStringUtil.nullConvert(ExcelValue.getStrCellVal(cell));
										}
										//logger.debug("cell({}) => {} : {}",rownum,colName,val);
										map.put(colName, val);
										
										//if ( "birthDay".equalsIgnoreCase(colName)) {
										//	String tmp = ExcelValue.getStrCellVal(cell);
										//	val = tmp.substring(2,4)+tmp.substring(5,7)+tmp.substring(8,10);
										//	map.put("memberNo", val);						logger.debug("cell({}) => memberNo : {}",rownum,map.get("memberNo"));
										//}
									}
								}

								map.put("rgsUserId", sesUserId);
								//map.put("updUserId", sesUserId);
								
								if(map.get("memberName") != null && map.get("moneyAmt") != null) {
									if( !"".equalsIgnoreCase(map.get("memberName")) && !"".equalsIgnoreCase(map.get("moneyAmt")) ) {
										logger.debug("map({}) => {}",rownum,map.toString());
										list.add(map);
									}
								}
							}
						}

						Map<String, Object> mapCnt = moneydigitalService.excelUpload(list);

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
						e.printStackTrace();
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
