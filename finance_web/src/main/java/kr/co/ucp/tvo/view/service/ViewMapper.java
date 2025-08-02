package kr.co.ucp.tvo.view.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.tvo.view.service.ViewRqstVO;
import kr.co.ucp.tvo.view.service.ViewSrchVO;

@Mapper("viewMapper")
public interface ViewMapper {

	// 열람
	int selectViewRqstListTotCnt(ViewSrchVO vo) throws Exception;
	List<EgovMap> selectViewRqstList(ViewSrchVO vo) throws Exception;

//	int selectViewRqstAprvListTotCnt(ViewSrchVO vo) throws Exception;
//	List<EgovMap> selectViewRqstAprvList(ViewSrchVO vo) throws Exception;
	
	EgovMap selectViewRqstDtl(Map<String, String> params) throws Exception;
	EgovMap selectNewViewRqstNo(Map<String, String> params) throws Exception;
	
	int insertTvoViewRqst(ViewRqstVO vo) throws Exception;
	int updateTvoViewRqst(ViewRqstVO vo) throws Exception;
	int updateTvoViewRqst(Map<String, String> params) throws Exception;
	int deleteTvoViewRqst(ViewRqstVO vo) throws Exception;
	
	// 열람연장
	int selectViewExtnHisListTotCnt(ViewSrchVO vo) throws Exception;
	List<EgovMap> selectViewExtnHisList(ViewSrchVO vo) throws Exception;
	
	int selectViewExtnListTotCnt(ViewSrchVO vo) throws Exception;
	List<EgovMap> selectViewExtnList(ViewSrchVO vo) throws Exception;

//	int selectViewProdExtnAprvListTotCnt(ViewSrchVO vo) throws Exception;
//	List<EgovMap> selectViewProdExtnAprvList(ViewSrchVO vo) throws Exception;

	EgovMap selectViewExtnDtl(Map<String, String> params) throws Exception;
	
	int insertTvoViewProdExtn(Map<String, String> params) throws Exception;
	int updateTvoViewProdExtn(Map<String, String> params) throws Exception;
	int deleteTvoViewProdExtn(Map<String, String> params) throws Exception;

	
	List<EgovMap> selectAllTypeRqstList(ViewSrchVO vo) throws Exception;
//	List<EgovMap> selectAllTypeWaitList(ViewSrchVO vo) throws Exception;
	
}
