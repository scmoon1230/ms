package kr.co.ucp.link.job.cctvlog.service;

import java.util.List;
import java.util.Map;

public interface JobCctvLogCnnctService {
	
	String jobCctvLogCnnctChk(String pDstrtCd) throws Exception;
	
	List<Map<String, Object>> selectCctvLogCnnctSend(Map<String, Object> map) throws Exception;
	
}
