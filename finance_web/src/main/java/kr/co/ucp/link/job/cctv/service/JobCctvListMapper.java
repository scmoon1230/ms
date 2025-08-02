package kr.co.ucp.link.job.cctv.service;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.co.ucp.cmmn.CamelMap;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper("jobCctvListMapper")
public interface JobCctvListMapper {

	List<CamelMap> selectCctvList(Map<String, Object> params) throws Exception;

	int updateCctv(Map<String, Object> params) throws Exception;

	int insertCctv(Map<String, Object> params) throws Exception;
	
}
