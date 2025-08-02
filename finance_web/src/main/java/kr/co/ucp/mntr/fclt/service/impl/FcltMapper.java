package kr.co.ucp.mntr.fclt.service.impl;

import java.util.List;
import java.util.Map;

import kr.co.ucp.mntr.fclt.service.FcltSrchVO;
import kr.co.ucp.mntr.fclt.service.FcltVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Mapper("fcltMapper")
public interface FcltMapper {

	List<EgovMap> selectFcltStatusList(Map<String, String> params) throws Exception;
	List<EgovMap> selectFcltStatus(FcltSrchVO vo) throws Exception;

	List<EgovMap> selectFcltList(FcltSrchVO vo) throws Exception;
	int selectFcltListTotCnt(FcltSrchVO vo) throws Exception;

	List<EgovMap> selectFcltUsedDistributionGeoData(Map<String, Object> params) throws Exception;
	List<EgovMap> selectSggEmdBoundList(Map<String, Object> params) throws Exception;

	List<EgovMap> selectFcltRegHisList(FcltSrchVO vo) throws Exception;
	int selectFcltRegHisListTotCnt(FcltSrchVO vo) throws Exception;

	public int updateFcltPoint(Map<String, String> map) throws Exception;
	public int updatePresetBdwStartNum(Map<String, String> map) throws Exception;
	FcltVO selectFcltDetail(FcltSrchVO vo) throws Exception;
	String selectFcltSeq() throws Exception;
	int insertFclt(FcltVO vo) throws Exception;
	int updateFclt(FcltVO vo) throws Exception;

	List<EgovMap> selectFcltUsedDistributionFcltUsedTyList(Map<String, Object> params) throws Exception;
	int  mergeFcltArea(FcltVO vo) throws Exception;

	List<EgovMap> selectFcltColumnList(FcltSrchVO vo) throws Exception;
	String selectFcltDefaultColumns() throws Exception;
	List<EgovMap> selectFcltColumnListForExcelUpload() throws Exception;
	List<EgovMap> selectFcltColumnListForExcelDownload() throws Exception;
	List<EgovMap> excelDownFcltList(FcltSrchVO vo) throws Exception;
	List<EgovMap> excelDownCodeList(FcltVO vo) throws Exception;
	List<EgovMap> excelDownNullList(FcltSrchVO vo) throws Exception;
	List<EgovMap> selectFcltAllColumns(FcltVO vo) throws Exception;
	List<EgovMap> selectFacilityUpchkList(FcltSrchVO vo) throws Exception;
	String selectUserValidation(Map<String, Object> args) throws Exception;
	List<EgovMap> selectExcelUploadCode() throws Exception;
}
