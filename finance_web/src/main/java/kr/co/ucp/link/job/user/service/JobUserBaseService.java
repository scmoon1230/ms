package kr.co.ucp.link.job.user.service;

import java.util.HashMap;
import java.util.Map;

public interface JobUserBaseService {

	String jobUserBaseChk(String pDstrtCd) throws Exception;

	HashMap<String, Object> updateUserList(Map<String, Object> rqMap) throws Exception;
	
}