package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

public interface DeptService {

	List<Map<String, String>> selectDept(Map<String, String> args) throws Exception;

	int insertDept(Map<String, Object> args) throws Exception;

	int updateDept(Map<String, Object> args) throws Exception;

	int deleteDept(Map<String, Object> args) throws Exception;

	int deleteDeptMulti(List<Map<String, Object>> args) throws Exception;

	
	int insertDeptRegion(Map<String, Object> args) throws Exception;

	//int updateDeptRegion(Map<String, Object> args) throws Exception;

	int deleteDeptRegion(Map<String, Object> args) throws Exception;

	int deleteDeptRegionMulti(List<Map<String, Object>> args) throws Exception;

}
