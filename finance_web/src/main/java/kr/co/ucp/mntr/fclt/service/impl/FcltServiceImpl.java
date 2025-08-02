/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : FcltServiceImpl.java
 * @Description : 시설물 관리 관련 서비스 구현
 * @Version : 1.0
 * Copyright (c) 2018 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2018. 09. 03. SaintJuny 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.fclt.service.impl;

import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovStringUtil;

import kr.co.ucp.mntr.cmm.util.ExcelUtil;
import kr.co.ucp.mntr.cmm.util.FileUtil;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.mntr.cmm.util.StringUtils;
import kr.co.ucp.mntr.fclt.service.FcltService;
import kr.co.ucp.mntr.fclt.service.FcltSrchVO;
import kr.co.ucp.mntr.fclt.service.FcltVO;
import kr.co.ucp.mntr.fclt.service.UpdErrorVO;
import kr.co.ucp.mntr.fclt.service.UploadVO;
import kr.co.ucp.mntr.gis.util.GisUtil;
import kr.co.ucp.utl.sim.service.EgovFileScrty;

@Service("fcltService")
public class FcltServiceImpl implements FcltService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="config")
	private Properties config;

    @Resource(name = "prprtsService")
    private PrprtsService prprtsService;

	@Resource(name="gisUtil")
	private GisUtil gisUtil;

	@Resource(name="fileUtil")
	private FileUtil fileUtil;

	@Resource(name="fcltMapper")
	private FcltMapper fcltMapper;

	@Resource(name="uploadManageMapper")
	private UploadManageMapper uploadManageMapper;

	@Resource(name="facilityIdGnr")
	private EgovIdGnrService fcltIdGnr;

	private final String NO_MATCH_COLUMN_NAME = "NO_MATCH_COLUMN_NAME";
	private final String NO_MATCH_COMMENTS = "NO_MATCH_COMMENTS";

	@Override
	public FcltVO selectFcltDetail(FcltSrchVO vo) throws Exception {
		return fcltMapper.selectFcltDetail(vo);
	}

	@Override
	public List<EgovMap> selectFcltStatusList() throws Exception {
		LoginVO loginVO = SessionUtil.getUserInfo();
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", loginVO.getUserId());
		params.put("grpId", loginVO.getGrpId());
		params.put("authLvl", loginVO.getAuthLvl());
		return fcltMapper.selectFcltStatusList(params);
	}

	@Override
	public List<EgovMap> selectFcltStatus(FcltSrchVO vo) throws Exception {
		LoginVO loginVO = SessionUtil.getUserInfo();
		vo.setUserId(loginVO.getUserId());
		vo.setGrpId(loginVO.getGrpId());
		vo.setAuthLvl(loginVO.getAuthLvl());
		return fcltMapper.selectFcltStatus(vo);
	}

	@Override
	public List<EgovMap> selectFcltColumnList(FcltSrchVO vo) throws Exception {
		return fcltMapper.selectFcltColumnList(vo);
	}

	@Override
	public String selectFcltDefaultColumns() throws Exception {
		return fcltMapper.selectFcltDefaultColumns();
	}

	@Override
	public List<?> excelDownNullList(FcltSrchVO searchVO) throws Exception {
		return fcltMapper.excelDownNullList(searchVO);
	}

	@Override
	public List<?> excelDownFcltList(FcltSrchVO searchVO) throws Exception {
		return fcltMapper.excelDownFcltList(searchVO);
	}

	/**
	 * <pre>
	 * 1. 파일전송
	 * 2. 업로드 실패 리턴 : -1
	 * 3. 데이터 변환(엑셀파일 -> 맵데이터)
	 * 		변환 실패 리턴 : 파일이 존재하지 않을 경우:-2 / 데이터가 존제하지 않을 경우:-3
	 * 4. 컬럼명 체크
	 * 8. 컬럼명이 맞지 않을 경우 리턴 : -4
	 * 7. 데이터 적합성 체크(데이터 전수 체크 - 센터 체크 포함)
	 * 8. 데이터 비적합 리턴 : -5
	 * 9. UploadCheck 테이블에서 동일 사용자의 작업 내역 삭제
	 * 10. UploadCheck 테이블에 등록
	 * 11. 업로드 파일 삭제
	 * </pre>
	 */
	@Override
	public String fcltExcelUploadValidation(UploadVO vo, MultipartFile file) throws Exception {
		// 1. 파일전송
		String fileName = fileUtil.saveFile(file);
		// 2. 업로드 실패 리턴 : -1
		if (fileName == null || "".equals(fileName)) {
			return "-1";
		}
		String path = fileUtil.getRootFilePath() + fileUtil.getDefaultFilePath() + fileName;
		// 3. 데이터 변환(엑셀파일 -> 맵데이터) vo.setDataList()에 담김
		String result = convertExcel2Map(path, vo);
		if (!"".equals(result)) {
			return result;
		}
		// 4. 컬럼명 체크
		result = columnNameCheck(vo);
		if (!"".equals(result)) {
			return result;
		}
		result = makeDataForDb(vo);
		// upchk -6 에러일 경우 오류무시업로드 할 수 없으므로 오류무시업로드 버튼 나오지 않도록 예외처리해야함. -7 에러는
		// 등록구분이 잘못입력되었음.
		if (!"".equals(result)) {
			if ("-7".equals(result))
				return "-7";
			String result2 = workForUploadCheck(vo);
			if ("-6".equals(result2)) {
				result = (Integer.toString(Integer.parseInt(result) + Integer.parseInt(result2)));
			}
		} else {
			result = workForUploadCheck(vo);
		}

		return result;
	}

	private String convertExcel2Map(String filePath, UploadVO vo) {
		String result = "";
		try {
			ExcelUtil exlUtil = ExcelUtil.getInstance(filePath);

			if (!exlUtil.isExcel(filePath)) {
				result = "-2";
			}

			List<Map<Integer, Object>> list = exlUtil.read(filePath);
			if (list == null || list.size() < 2) {
				result = "-3";
			}

			if ("".equals(result)) {
				Iterator<Map<Integer, Object>> iteratorList = list.iterator();
				while (iteratorList.hasNext()) {
					Map<Integer, Object> map = iteratorList.next();
					Collection<Object> collection = map.values();
					Iterator<Object> iteratorValues = collection.iterator();
					boolean isEmptyRow = true;
					while (iteratorValues.hasNext()) {
						Object obj = iteratorValues.next();
						String string = EgovStringUtil.nullConvert(obj);
						if (!"".equals(string)) {
							isEmptyRow = false;
						}
					}
					if (isEmptyRow) {
						iteratorList.remove();
						logger.info("======= Removed Empty Row");
					}
				}
				fileUtil.deleteFile(filePath);
				vo.setDataList(list);
			}
		} catch (Exception e) {
			logger.error("convertExcel2Map Exception : {} ", e.getMessage());
			result = "-2";
		}
		return result;
	}

	private String columnNameCheck(UploadVO vo) throws Exception {

		String orgColumn = ""; // VO 엑셀다운로드 헤더명(컬럼명)들
		String orgColumnChk = ""; // VO 엑셀다운로드 오류 컬럼 색깔 표시
		String orgColumnId = ""; // VO 실테이블 컬럼ID(엑셀 다운로드 쿼리에서 사용하는 컬럼ID)

		String columnId = ""; // 실테이블 컬럼ID들 UPLOAD_CHECK에서 사용
		String columnIdUpload = ""; // 실테이블 UPDATE할 컬럼ID들
		String errColumn = "";

		// 시설물마스터 전체컬럼 가져옴
		List<EgovMap> fcltColumnList = fcltMapper.selectFcltColumnListForExcelUpload(); // 실테이블 컬럼ID와 컬럼명들
		int fcltColumnListSize = (fcltColumnList == null || fcltColumnList.isEmpty()) ? 0 : fcltColumnList.size();

		List<String> fcltColumnNameList = new ArrayList<String>();
		List<String> fcltCommentsList = new ArrayList<String>();

		for (int i = 0; i < fcltColumnListSize; i++) {
			fcltColumnNameList.add(EgovStringUtil.nullConvert(fcltColumnList.get(i).get("columnName")));
			fcltCommentsList.add(EgovStringUtil.nullConvert(fcltColumnList.get(i).get("comments")));
		}

		try {
			List<Map<Integer, Object>> dataList = vo.getDataList();
			if (dataList == null || dataList.isEmpty()) {
				return "-3";
			}
			// 엑셀 헤더 리스트
			Map<Integer, Object> excelFileCommentsMap = dataList.get(0); // 엑셀

			// 헤더명들(컬럼명들)
			int excelFileCommentsMapSize = excelFileCommentsMap.size();

			// 엑셀 헤더 하나씩 테이블컬럼 전체에 있는지 확인하여 목록만듦
			for (int i = 0; i < excelFileCommentsMapSize; i++) { // 엑셀 헤더명들(컬럼명들)
				String excelFileComments = EgovStringUtil.nullConvert(excelFileCommentsMap.get(i)); // 엑셀 헤더명들(컬럼명들)
				String[] excelFileColumn = new String[2];
				// 실테이블 컬럼명과 컬럼ID 세트
				for (int j = 0; j < fcltColumnListSize; j++) { // 실테이블 컬럼명들
					String fcltComments = fcltCommentsList.get(j); // 실테이블 컬럼명
					if (fcltComments.equals(excelFileComments)) {
						// 실테이블 컬럼명과 컬럼ID 세트
						excelFileColumn[0] = fcltCommentsList.get(j);
						excelFileColumn[1] = fcltColumnNameList.get(j);
					}
				}

				if (excelFileColumn[0] == null || excelFileColumn[1] == null) {
					excelFileColumn[0] = NO_MATCH_COMMENTS;
					excelFileColumn[1] = NO_MATCH_COLUMN_NAME;
				}

				String comments = excelFileColumn[0]; // 실테이블 컬럼명과 컬럼ID 세트에서
														// 컬럼명을 header에 입력
				String commentsAs = comments; // 실테이블 컬럼명에 AS컬럼명 저장
				commentsAs = commentsAs.trim();
				commentsAs = commentsAs.replaceAll(" ", "");
				commentsAs = commentsAs.replaceAll("(^\\p{Z}+|\\p{Z}+$)", "");
				commentsAs = commentsAs.replaceAll("\\(", "");
				commentsAs = commentsAs.replaceAll("\\)", "");

				String columnName = excelFileColumn[1]; // 실테이블 컬럼ID
				columnId = columnId + columnName + ",";// 실테이블 컬럼ID들

				if (!NO_MATCH_COMMENTS.equals(comments)) {

					// DECODE(RGSUPD_TY, 'I', '신규', '수정') AS 등록수정
					if ("RGSUPD_TY".equals(columnName)) {
						orgColumn = orgColumn + comments + ","; // 컬럼명(헤더명) 실테이블 컬럼명 입력
						orgColumnChk = orgColumn + comments + "_CHK,"; // 컬럼명_CHK
						columnName = "DECODE(" + columnName + ",'I','신규','수정') AS " + commentsAs + ", " + columnName
								+ "_CHK AS " + commentsAs + "_CHK";
						orgColumnId = orgColumnId + columnName + ","; // 컬럼명 AS
					} else if ("FCLT_ID".equals(columnName)) {
						orgColumn = orgColumn + comments + ","; // 컬럼명(헤더명) 실테이블 컬럼명 입력
						orgColumnChk = orgColumn + comments + "_CHK,"; // 컬럼명_CHK
						columnName = columnName + " AS " + commentsAs + ", " + columnName + "_CHK AS " + commentsAs
								+ "_CHK";
						orgColumnId = orgColumnId + columnName + ","; // 컬럼명 AS
					} else if ("RGS_USER_ID".equals(columnName)) {

					} else if ("RGS_DATE".equals(columnName)) {

					} else if ("UPD_USER_ID".equals(columnName)) {
						columnIdUpload = columnIdUpload + columnName + ",";
					} else if ("UPD_DATE".equals(columnName)) {
						columnIdUpload = columnIdUpload + columnName + ",";
					} else {
						orgColumn = orgColumn + comments + ","; // 컬럼명(헤더명) 실테이블 컬럼명 입력
						orgColumnChk = orgColumn + comments + "_CHK,"; // 컬럼명_CHK
						columnIdUpload = columnIdUpload + columnName + ",";
						columnName = columnName + " AS " + commentsAs + ", " + columnName + "_CHK AS " + commentsAs
								+ "_CHK";
						orgColumnId = orgColumnId + columnName + ","; // 컬럼명 AS
					}

				} else { // 헤더명이 잘못 입력되어 없을경우
					errColumn = errColumn + excelFileComments + ",";
				}
			}

			if (!"".equals(orgColumn))
				vo.setOrgColumn(orgColumn.substring(0, orgColumn.lastIndexOf(',')));
			if (!"".equals(orgColumnChk))
				vo.setOrgColumnChk(orgColumnChk.substring(0, orgColumnChk.lastIndexOf(',')));
			if (!"".equals(columnId))
				vo.setColumnId(columnId.substring(0, columnId.lastIndexOf(',')));
			if (!"".equals(orgColumnId))
				vo.setOrgColumnId(orgColumnId.substring(0, orgColumnId.lastIndexOf(','))); // 컬럼명 AS
			if (!"".equals(columnIdUpload))
				vo.setColumnIdUpload(columnIdUpload.substring(0, columnIdUpload.lastIndexOf(',')));
			if (!"".equals(errColumn))
				vo.setErrColumn(errColumn.substring(0, errColumn.lastIndexOf(',')));
			// 엑셀헤더 하나씩 테이블컬럼전체에 대해 없으면 바로 return
			for (int i = 0; i < excelFileCommentsMapSize; i++) {
				String excelFileComments = excelFileCommentsMap.get(i).toString();

				String[] excelFileColumn = new String[2];
				for (int j = 0; j < fcltColumnListSize; j++) {
					if (excelFileComments.equals(fcltCommentsList.get(j))) {
						excelFileColumn[0] = fcltCommentsList.get(j);
						excelFileColumn[1] = fcltColumnNameList.get(j);
					}
				}

				if (excelFileColumn[0] == null || excelFileColumn[1] == null) {
					excelFileColumn[0] = NO_MATCH_COMMENTS;
					excelFileColumn[1] = NO_MATCH_COLUMN_NAME;
				}

				String comments = excelFileColumn[0];

				if (!comments.equals(excelFileComments)) {
					logger.debug(
							"=======columnNameCheck===IF====excelFileComments=" + excelFileComments + "," + comments);
					return "-4";
				}
			}

		} catch (Exception e) {
			logger.error("columnNameCheck Exception : {} ", e.getMessage());
			e.printStackTrace();
			return "-4";
		}
		return "";
	}

	private String makeDataForDb(UploadVO vo) {
		UploadValidCheck validChk = new UploadValidCheck(uploadManageMapper, new EgovFileScrty(), config);
		int errorCnt = 0;
		String columnNms = "";
		try {
			List<UpdErrorVO> errorList = new ArrayList<UpdErrorVO>();
			List<EgovMap> eMapList = new ArrayList<EgovMap>();
			List<Map<Integer, Object>> dataList = vo.getDataList();
			if (dataList != null && !dataList.isEmpty()) {
				for (int idx = 1; idx < dataList.size(); idx++) {
					List<UpdErrorVO> subList = new ArrayList<UpdErrorVO>();
					Map<Integer, Object> dataMap = dataList.get(idx);

					String columnIds = vo.getColumnId();
					// logger.debug("=======makeDataForDb=======columnIds="+columnIds);
					EgovMap eMap = validChk.uploadValidCheck(idx, dataMap, subList, columnIds);

					// 등록구분이 I,U,D 아니면 리턴
					String rgsupdTy = EgovStringUtil.nullConvert(eMap.get("rgsupdTy"));
					logger.debug("======= data: {}", eMap.toString());
					if (!"I".equals(rgsupdTy) && !"U".equals(rgsupdTy) && !"D".equals(rgsupdTy)) {
						logger.debug("=======makeDataForDb=======eMap.get(rgsupdTy)=" + eMap.toString());
						return "-7";
					}
					columnNms = (String) eMap.get("columnNm");
					// eMap.remove("columnNm");
					eMap.put("updUserId", vo.getUpdUserId());

					if (subList.size() > 0) {
						errorList.addAll(subList);
						errorCnt++;
					}

					if (eMap != null)
						eMapList.add(eMap);

				}
			}

			vo.setErrorList(errorList);
			vo.seteMapList(eMapList);
			vo.setColumnNm(columnNms);
			vo.setTotCnt(dataList.size() - 1);
			vo.setNorCnt(dataList.size() - 1 - errorCnt);
			vo.setErrCnt(errorCnt);
			if (errorList.size() > 0)
				return "-5";
		} catch (Exception e) {
			logger.error("makeDataForDb Exception : {} ", e.getMessage());
			return "-5";
		}
		return "";
	}

	private String workForUploadCheck(UploadVO vo) throws Exception {
		//String strMaxNum = fileUtil.getProperties("bulk_insert_max");
        String strMaxNum = fileUtil.getProperties("BULK_INSERT_MAX");
		int maxInsertNum = Integer.parseInt(strMaxNum);

		try {
			uploadManageMapper.deleteUploadCheck(vo.getUpdUserId());
			List<EgovMap> list = vo.geteMapList();
			int size = list.size();
			int toIdx = (size <= maxInsertNum) ? size : maxInsertNum;
			for (int fromIdx = 0; fromIdx < list.size(); fromIdx += maxInsertNum) {
				List<EgovMap> subList = list.subList(fromIdx, toIdx);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("list", subList);
				uploadManageMapper.insertUploadCheck2(map);

				toIdx += maxInsertNum;
				toIdx = (size <= toIdx) ? size : toIdx;
			}
		} catch (Exception e) {
			logger.error("workForUploadCheck Exception : {} ", e.getMessage());
			e.printStackTrace();
			return "-6";
		}
		return "1";
	}

	@Override
	public List<EgovMap> selectFacilityUpchkList(FcltSrchVO searchVO) throws Exception {
		return fcltMapper.selectFacilityUpchkList(searchVO);
	}

	@Override
	public String selectUserValidation(Map<String, Object> args) throws Exception {
		return fcltMapper.selectUserValidation(args);
	}

	@Override
	public String fcltExcelUploadProc(UploadVO vo) throws Exception {
		int ret = 0;
		try {

			String dbType = EgovStringUtil.nullConvert(config.get("Globals.dbType"));
			if ("maria_mysql".equals(dbType)) {
				vo.setNextFcltSeq(fcltIdGnr.getNextStringId());
				String columnIdUpload = vo.getColumnIdUpload();
				if (!"".equals(columnIdUpload)) {
					vo.setColumnIdUploads(columnIdUpload.split(Pattern.quote(",")));
				}
			}
			int iret = uploadManageMapper.insertFcltList(vo);
			int uret = uploadManageMapper.uploadFcltList(vo);
			vo.setRegCnt(iret);
			vo.setUpdCnt(uret);
			ret = iret + uret;
			vo.setAllCnt(ret);
			uploadManageMapper.insertFcltHis(vo);
			logger.info("fcltExcelUploadProc : {}", vo.toString());
		} catch (Exception e) {
			logger.error("fcltExcelUploadProc Exception : {}", e.getMessage());
			return "-1";
		}
		return "1";
	}

	@Override
	public List<EgovMap> selectFcltList(FcltSrchVO vo) throws Exception {
		LoginVO loginVO = SessionUtil.getUserInfo();
		vo.setUserId(loginVO.getUserId());
		vo.setGrpId(loginVO.getGrpId());
		vo.setAuthLvl(loginVO.getAuthLvl());

		String searchFcltId = EgovStringUtil.nullConvert(vo.getSearchFcltId());
		logger.info("--> searchFcltId : {}", searchFcltId);
		if (!"".equals(searchFcltId)) {
			vo.setSearchFcltId(StringUtils.avoidRegexp(searchFcltId));
		}
		logger.info("--> searchFcltId : {}", vo.getSearchFcltId());

        String vmsLinkYn = EgovStringUtil.nullConvert(prprtsService.getString("VMS_LINK_YN"));
		if ("Y".equals(vmsLinkYn)) {
			vo.setVmsLinkYn(vmsLinkYn);
		}

		Point2D.Double tp = new Point2D.Double();

		String strLon = EgovStringUtil.nullConvert(vo.getLon());
		String strLat = EgovStringUtil.nullConvert(vo.getLat());

		if (!"".equals(strLon) && !"".equals(strLat)) {

			tp = gisUtil.convertToWgs84(strLon, strLat);

			vo.setRadius(vo.getRadius() / 1000);
			vo.setLon(String.valueOf(tp.x));
			vo.setLat(String.valueOf(tp.y));
		}

		List<EgovMap> buf = fcltMapper.selectFcltList(vo);

		DecimalFormat df = new DecimalFormat(".#########");
		for (EgovMap itr : buf) {
			String pointX = EgovStringUtil.nullConvert(itr.get("pointX"));
			String pointY = EgovStringUtil.nullConvert(itr.get("pointY"));
			if (!"".equals(pointX) && !"".equals(pointY)) {
				tp = gisUtil.convertByWgs84(pointX, pointY);
				itr.put("pointX", df.format(tp.getX()));
				itr.put("pointY", df.format(tp.getY()));
			} else {
				itr.put("pointX", "");
				itr.put("pointY", "");
			}
		}
		return buf;
	}

	@Override
	public int selectFcltListTotCnt(FcltSrchVO vo) throws Exception {
		LoginVO loginVO = SessionUtil.getUserInfo();
		vo.setUserId(loginVO.getUserId());
		vo.setGrpId(loginVO.getGrpId());
		vo.setAuthLvl(loginVO.getAuthLvl());

		return fcltMapper.selectFcltListTotCnt(vo);
	}

	@Override
	public List<EgovMap> selectFcltUsedDistributionFcltUsedTyList(Map<String, Object> params) throws Exception {
		return fcltMapper.selectFcltUsedDistributionFcltUsedTyList(params);
	}

	@Override
	public List<EgovMap> selectFcltUsedDistributionGeoData(Map<String, Object> params) throws Exception {
		LoginVO loginVO = SessionUtil.getUserInfo();
		params.put("userId", loginVO.getUserId());
		params.put("grpId", loginVO.getGrpId());
		params.put("authLvl", loginVO.getAuthLvl());

		String sSearchSigunguCd = EgovStringUtil.nullConvert(params.get("searchSigunguCd"));
		if ("".equals(sSearchSigunguCd)) {
			params.put("searchSigunguCd", null);
		} else {
			params.put("searchSigunguCd", sSearchSigunguCd.split(","));
		}

		String sSearchFcltUsedTyCd = EgovStringUtil.nullConvert(params.get("searchFcltUsedTyCd"));
		params.put("searchFcltUsedTyCd", sSearchFcltUsedTyCd.split(","));
		return fcltMapper.selectFcltUsedDistributionGeoData(params);
	}

	@Override
	public List<EgovMap> selectSggEmdBoundList(Map<String, Object> params) throws Exception {
		String sSigunguCd = config.getProperty("configure.sigunguCd", "");
		String[] sigunguCd = sSigunguCd.split(",");
		params.put("sigunguCd", sigunguCd);
		return fcltMapper.selectSggEmdBoundList(params);
	}

	@Override
	public List<EgovMap> selectFcltRegHisList(FcltSrchVO vo) throws Exception {
		return fcltMapper.selectFcltRegHisList(vo);
	}

	@Override
	public int selectFcltRegHisListTotCnt(FcltSrchVO vo) throws Exception {
		return fcltMapper.selectFcltRegHisListTotCnt(vo);
	}

	@Override
	public EgovMap updateFcltPoint(Map<String, String> map) throws Exception {
		String userId = SessionUtil.getUserId();
		map.put("userId", userId);
		int result = fcltMapper.updateFcltPoint(map);
		EgovMap egovMap = new EgovMap();
		egovMap.put("size", result);
		return egovMap;
	}

	@Override
	public EgovMap updatePresetBdwStartNum(Map<String, String> map) throws Exception {
		String userId = SessionUtil.getUserId();
		map.put("userId", userId);
		int result = fcltMapper.updatePresetBdwStartNum(map);
		EgovMap egovMap = new EgovMap();
		egovMap.put("size", result);
		return egovMap;
	}

	@Override
	public String insertFclt(FcltVO vo) throws Exception {
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		String fcltKndCd = vo.getFcltKndCd();
		String nextFcltSeq = "";
		String dbType = EgovStringUtil.nullConvert(config.get("Globals.dbType"));
		if ("maria_mysql".equals(dbType)) {
			nextFcltSeq = fcltIdGnr.getNextStringId();
		} else {
			nextFcltSeq = fcltMapper.selectFcltSeq();
		}
		//String sFcltId = fcltKndCd + ucpId + year + nextFcltSeq;
		String sFcltId = fcltKndCd + year + nextFcltSeq;
		vo.setFcltId(sFcltId);
		String userId = SessionUtil.getUserId();
		vo.setUserId(userId);
		int i = fcltMapper.insertFclt(vo);
		return sFcltId;
	}

	@Override
	public int updateFclt(FcltVO vo) throws Exception {

		int i = fcltMapper.updateFclt(vo);
		if (i > 0) {
			String areaCd = EgovStringUtil.nullConvert(vo.getAreaCd());
			if (!"".equals(areaCd)) {
				fcltMapper.mergeFcltArea(vo);
			}
		}
		return i;
	}

	@Override
	public List<EgovMap> selectExcelUploadCode() throws Exception {
		return fcltMapper.selectExcelUploadCode();
	}

	@Override
	public void downloadExcelUploadForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		fileUtil.fileDownload(request, response);
	}
}
