package kr.co.ucp.wrks.sstm.usr.service;

import java.util.List;
import java.util.Map;

public interface UsrApproveService {

	int approve(Map<String, Object> map) throws Exception;
	
	int notApprove(Map<String, Object> map) throws Exception;
	
	// 사용자관리 사용자별 그룹 조회(등록된 그룹 이외)
//	List<Map<String, String>> getCmGroupList(Map<String, String> args) throws Exception;
	
	// 사용자관리 사용자별 그룹 조회
	List<Map<String, String>> getCmGrpUserList(Map<String, String> args) throws Exception;
	
}
