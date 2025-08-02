
package kr.co.ucp.tvo.cctv.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;
import java.util.Map;

public interface CctvMgmtService {
	
	List<EgovMap> selectTvoCctvList(Map<String, Object> params) throws Exception;

	int updateCctv(Map<String, Object> params) throws Exception;

//	List<EgovMap> selectApiDataList(Map<String, Object> params) throws Exception;

}
