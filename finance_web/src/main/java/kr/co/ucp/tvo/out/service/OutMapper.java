package kr.co.ucp.tvo.out.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.tvo.out.service.OutSrchVO;

@Mapper("outMapper")
public interface OutMapper {

	// 반출
	int selectOutRqstListTotCnt(OutSrchVO vo) throws Exception;
	List<EgovMap> selectOutRqstList(OutSrchVO vo) throws Exception;

//	int selectTvoOutRqstAprvListTotCnt(OutSrchVO vo) throws Exception;
//	List<EgovMap> selectTvoOutRqstAprvList(OutSrchVO vo) throws Exception;

	EgovMap selectOutRqstWorkCnt() throws Exception;
	List<EgovMap> selectOutRqstVdoDuration() throws Exception;
	
	
	EgovMap selectOutRqstDtl(Map<String, String> params) throws Exception;
	EgovMap selectNewOutRqstNo(Map<String, String> params) throws Exception;
	
	int insertTvoOutRqst(Map<String, String> params) throws Exception;
	int updateTvoOutRqst(Map<String, String> params) throws Exception;
	int deleteTvoOutRqst(Map<String, String> params) throws Exception;

	// 반출파일
	int selectOutFileListTotCnt(OutSrchVO vo) throws Exception;
	List<EgovMap> selectOutFileList(OutSrchVO vo) throws Exception;

	List<EgovMap> selectOutFileNeedMp4List() throws Exception;
	
	EgovMap selectOutFileDtl(Map<String, String> params) throws Exception;
	
	int insertTvoOutFile(Map<String, String> params) throws Exception;
	int updateTvoOutFile(Map<String, String> params) throws Exception;
	int deleteTvoOutFile(Map<String, String> params) throws Exception;

	// 반출연장
	int selectOutExtnListTotCnt(OutSrchVO vo) throws Exception;
	List<EgovMap> selectOutExtnList(OutSrchVO vo) throws Exception;

//	int selectTvoOutExtnAprvListTotCnt(OutSrchVO vo) throws Exception;
//	List<EgovMap> selectTvoOutExtnAprvList(OutSrchVO vo) throws Exception;
	
	int selectOutExtnHisListTotCnt(OutSrchVO vo) throws Exception;
	List<EgovMap> selectOutExtnHisList(OutSrchVO vo) throws Exception;

	
	EgovMap selectOutExtnDtl(Map<String, String> params) throws Exception;
	
	int insertTvoOutProdExtn(Map<String, String> params) throws Exception;
	int updateTvoOutProdExtn(Map<String, String> params) throws Exception;
	int deleteTvoOutProdExtn(Map<String, String> params) throws Exception;
	
	// 다운로드이력
	int insertOutFileDownloadHis(Map<String, String> params) throws Exception;
	
	//EgovMap selectMaskingEndYn(Map<String, String> params) throws Exception;

	List<EgovMap> selectSvrConnIpList(OutSrchVO vo) throws Exception;
	
	EgovMap selectUserInfo(Map<String, String> params) throws Exception;
	
}