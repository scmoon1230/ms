package kr.co.ucp.tvo.out.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.tvo.out.service.OutSrchVO;

public interface OutService {

	// 반출
	int selectOutRqstListTotCnt(OutSrchVO vo) throws Exception;
	List<EgovMap> selectOutRqstList(OutSrchVO vo) throws Exception;

	int selectOutRqstAprvListTotCnt(OutSrchVO vo) throws Exception;
	List<EgovMap> selectOutRqstAprvList(OutSrchVO vo) throws Exception;


	// 반출대기건수
	EgovMap selectOutRqstWorkCnt() throws Exception;
	// 반출영상 다운로드 시간
	int selectOutRqstVdoDurationSum() throws Exception;

	
	EgovMap selectOutRqstDtl(Map<String, String> params) throws Exception;

	int updateOutRqst(Map<String, String> params) throws Exception;
	int resetOutRqst(Map<String, String> params) throws Exception;
	int deleteCompleteOutRqst(Map<String, String> params) throws Exception;
	Map<String, String> registerOutRqst(Map<String, String> params) throws Exception;
	Map<String, String> approveOutRqst(Map<String, String> params) throws Exception;
//	Map<String, String> modifyOutRqst(Map<String, String> params) throws Exception;
	Map<String, String> removeOutRqst(Map<String, String> params) throws Exception;
	
	// 반출파일
	int selectOutFileListTotCnt(OutSrchVO vo) throws Exception;
	List<EgovMap> selectOutFileList(OutSrchVO vo) throws Exception;

	List<EgovMap> selectOutFileNeedMp4List() throws Exception;
	
	EgovMap selectOutFileDtl(Map<String, String> params) throws Exception;
	
//	int insertOutFile(Map<String, String> params) throws Exception;
	int updateOutFile(Map<String, String> params) throws Exception;
//	int deleteOutFile(Map<String, String> params) throws Exception;
	Map<String, String> registerOutFile(Map<String, String> params) throws Exception;
	Map<String, String> modifyMovieFile(Map<String, String> params) throws Exception;
	void removeFile(EgovMap tvoOutFile) throws Exception;	// 영상파일 삭제
	
	// 반출연장
	int selectOutExtnListTotCnt(OutSrchVO vo) throws Exception;
	List<EgovMap> selectOutExtnList(OutSrchVO vo) throws Exception;

	int selectOutExtnAprvListTotCnt(OutSrchVO vo) throws Exception;
	List<EgovMap> selectOutExtnAprvList(OutSrchVO vo) throws Exception;
		
	int selectOutExtnHisListTotCnt(OutSrchVO vo) throws Exception;
	List<EgovMap> selectOutExtnHisList(OutSrchVO vo) throws Exception;

	EgovMap selectOutExtnDtl(Map<String, String> params) throws Exception;
	
	Map<String, String> registerOutExtn(Map<String, String> params) throws Exception;
	Map<String, String> approveOutExtn(Map<String, String> params) throws Exception;
	
	// 다운로드이력
	int insertOutFileDownloadHis(Map<String, String> params) throws Exception;
	
	// 마크애니 원본영상 요청
	Map<String, Object> requestMaOrgVdo(Map<String, String> params) throws Exception;
	
	// 마크애니 원본영상 요청결과 처리
	void actMaResultMap(String resultRootPath, String upperRootPath, Map<String, Object> resultMap) throws Exception;

	Map<String, String> sendRequestEncrypt(Map<String, String> params) throws Exception;
	
	// 마크애니 영상암호화 요청
	Map<String, Object> requestMaEncrypt(Map<String, String> params) throws Exception;

	// mp4 파일로 변환
	void transOrgVdoFile(EgovMap tvoOutFile) throws Exception;
	
	// 보관기간 경과한 파일 삭제
	void deleteExpiredVdo() throws Exception;
	
	// 마크애니 order.xml을 떨어뜨리고 읽어올 경로 조회
	String getMaOrderRootPaths() throws Exception;
	
}
