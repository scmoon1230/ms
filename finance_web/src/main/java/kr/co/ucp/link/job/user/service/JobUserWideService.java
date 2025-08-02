package kr.co.ucp.link.job.user.service;

import java.util.List;
import java.util.Map;

public interface JobUserWideService {

	String jobUserWideChk(String pDstrtCd) throws Exception;
	
	List<Map<String, Object>> selectUserList(Map<String, Object> map) throws Exception;
	
}