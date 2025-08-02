
package kr.co.ucp.tvo.cctv.service;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CctvMgmtMapper {
	
	List<EgovMap> selectTvoCctvList(Map<String, Object> params);

	int updateCctv(Map<String, Object> params);

//	List<EgovMap> selectApiDataList(Map<String, Object> params);
	
}
