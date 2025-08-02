/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : FcltService.java
 * @Description : 시설물 관리 관련 서비스
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
package kr.co.ucp.mntr.fclt.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface FcltService {
	List<EgovMap> selectFcltStatus(FcltSrchVO vo) throws Exception;

	List<EgovMap> selectFcltStatusList() throws Exception;

	List<EgovMap> selectFcltList(FcltSrchVO vo) throws Exception;

	int selectFcltListTotCnt(FcltSrchVO vo) throws Exception;

	List<EgovMap> selectFcltUsedDistributionGeoData(Map<String, Object> map) throws Exception;

	List<EgovMap> selectSggEmdBoundList(Map<String, Object> map) throws Exception;

	List<?> selectFcltRegHisList(FcltSrchVO vo) throws Exception;

	int selectFcltRegHisListTotCnt(FcltSrchVO vo) throws Exception;

	EgovMap updateFcltPoint(Map<String, String> map) throws Exception;

	EgovMap updatePresetBdwStartNum(Map<String, String> map) throws Exception;

	FcltVO selectFcltDetail(FcltSrchVO vo) throws Exception;

	String insertFclt(FcltVO vo) throws Exception;

	int updateFclt(FcltVO vo) throws Exception;

	List<EgovMap> selectFcltUsedDistributionFcltUsedTyList(Map<String, Object> params) throws Exception;

	String fcltExcelUploadValidation(UploadVO vo, MultipartFile mpf) throws Exception;

	List<EgovMap> selectFacilityUpchkList(FcltSrchVO searchVO) throws Exception;

	String selectUserValidation(Map<String, Object> args) throws Exception;

	String fcltExcelUploadProc(UploadVO vo) throws Exception;

	List<EgovMap> selectExcelUploadCode() throws Exception;

	void downloadExcelUploadForm(HttpServletRequest request, HttpServletResponse response) throws Exception;

	List<EgovMap> selectFcltColumnList(FcltSrchVO vo) throws Exception;

	String selectFcltDefaultColumns() throws Exception;

	List<?> excelDownNullList(FcltSrchVO searchVO) throws Exception;

	List<?> excelDownFcltList(FcltSrchVO searchVO) throws Exception;
}
