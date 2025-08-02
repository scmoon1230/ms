package kr.co.ucp.tvo.view.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.tvo.view.service.ViewRqstVO;
import kr.co.ucp.tvo.view.service.ViewSrchVO;

public interface ViewService {

	// 열람
	int selectViewRqstListTotCnt(ViewSrchVO vo) throws Exception;
	List<EgovMap> selectViewRqstList(ViewSrchVO vo) throws Exception;

	int selectViewRqstAprvListTotCnt(ViewSrchVO vo) throws Exception;
	List<EgovMap> selectViewRqstAprvList(ViewSrchVO vo) throws Exception;
	
	EgovMap selectViewRqstDtl(Map<String, String> params) throws Exception;
	EgovMap selectNewViewRqstNo(Map<String, String> params) throws Exception;
	
	int registerViewRqst(ViewRqstVO vo) throws Exception;
	int updateViewRqst(ViewRqstVO vo) throws Exception;
	int modifyViewRqst(Map<String, String> params) throws Exception;
	int resetViewRqst(Map<String, String> params) throws Exception;
	int deleteCompleteViewRqst(Map<String, String> params) throws Exception;
	int approveViewRqst(Map<String, String> params) throws Exception;
	int removeViewRqst(ViewRqstVO vo) throws Exception;
	

	// 열람연장
	int selectViewExtnHisListTotCnt(ViewSrchVO vo) throws Exception;
	List<EgovMap> selectViewExtnHisList(ViewSrchVO vo) throws Exception;

	int selectViewExtnListTotCnt(ViewSrchVO vo) throws Exception;
	List<EgovMap> selectViewExtnList(ViewSrchVO vo) throws Exception;

	int selectViewExtnAprvListTotCnt(ViewSrchVO vo) throws Exception;
	List<EgovMap> selectViewExtnAprvList(ViewSrchVO vo) throws Exception;

	EgovMap selectViewExtnDtl(Map<String, String> params) throws Exception;
	
	Map<String, String> registerViewExtn(Map<String, String> params) throws Exception;
	Map<String, String> approveViewExtn(Map<String, String> params) throws Exception;
	
	
	List<EgovMap> selectAllTypeRqstList(ViewSrchVO vo) throws Exception;
//	List<EgovMap> selectAllTypeWaitList(ViewSrchVO vo) throws Exception;
	
}
