package kr.co.ucp.link.job.cctv.service;

import java.util.Map;

public interface JobCctvStateService {
	
    String jobCctvStateChk(String pDstrtCd) throws Exception;

    String rsData(Map<String, Object> rsMap) throws Exception;
    
}
