package kr.co.ucp.plan.service;

import java.util.List;
import java.util.Map;

public interface PlanmngService {

	Map<String, String> selectDurationYear(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectPlanmng(Map<String, String> args) throws Exception;

	int insertPlan(Map<String, Object> args) throws Exception;

	int updatePlan(Map<String, Object> args) throws Exception;

	int deletePlan(Map<String, String> args) throws Exception;

	int deletePlanMulti(List<Map<String, String>> args) throws Exception;
	
}
