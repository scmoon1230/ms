package kr.co.ucp.link.base.user.service;

import java.util.List;
import java.util.Map;

public interface BaseUserBaseService {
	
	List<Map<String, Object>> selectUserList(Map<String, Object> map) throws Exception;
	
}
