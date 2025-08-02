package kr.co.ucp.plan.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.plan.service.PlanmngMapper;
import kr.co.ucp.plan.service.PlanmngService;

@Service("planmngService")
public class PlanmngServiceImpl implements PlanmngService {

	@Resource(name="planmngMapper")
	private PlanmngMapper planmngMapper;

	// 조건검색
	@Override
	public Map<String, String> selectDurationYear(Map<String, String> args) throws Exception {
		return planmngMapper.selectDurationYear(args);
	}

	// 조건검색
	@Override
	public List<Map<String, String>> selectPlanmng(Map<String, String> args) throws Exception {
		return planmngMapper.selectPlanmng(args);
	}

	// 입력
	@Override
	public int insertPlan(Map<String, Object> args)  throws Exception {
		return planmngMapper.insertPlan(args);
	}

	// 수정
	@Override
	public int updatePlan(Map<String, Object> args) throws Exception {
		return planmngMapper.updatePlan(args);
	}

	// 삭제
	@Override
	public int deletePlan(Map<String, String> args) throws Exception {
		return planmngMapper.deletePlan(args);
	}

	// 다중삭제
	@Override
	public int deletePlanMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			planmngMapper.deletePlan(arg);
		}

		return 1;
	}
	
}
