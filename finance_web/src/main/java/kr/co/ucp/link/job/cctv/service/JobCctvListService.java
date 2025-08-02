package kr.co.ucp.link.job.cctv.service;

import java.util.List;
import java.util.Map;

import kr.co.ucp.cmmn.CamelMap;

public interface JobCctvListService {

	String jobCctvListChk(String pDstrtCd) throws Exception;

	List<CamelMap> selectCctvList(Map<String, Object> params) throws Exception;

	int updateCctv(Map<String, Object> cctv) throws Exception;

	int insertCctv(Map<String, Object> cctv) throws Exception;
}
