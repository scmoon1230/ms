package kr.co.ucp.link.base.user.service;

import java.util.HashMap;
import java.util.Map;

public interface BaseUserWideService {
	
	HashMap<String, Object> updateUserList(Map<String, Object> rqMap) throws Exception;
	
}
