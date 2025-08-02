package kr.co.ucp.env.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.env.service.DeptMapper;
import kr.co.ucp.env.service.DeptService;

@Service("deptService")
public class DeptServiceImpl implements DeptService {

	@Resource(name="deptMapper")
	private DeptMapper deptMapper;

	// 교구코드 조건검색
	@Override
	public List<Map<String, String>> selectDept(Map<String, String> args) throws Exception {
		return deptMapper.selectDept(args);
	}

	// 교구코드 입력
	@Override
	public int insertDept(Map<String, Object> args)  throws Exception {
		return deptMapper.insertDept(args);
	}

	// 교구코드 수정
	@Override
	public int updateDept(Map<String, Object> args) throws Exception {
		return deptMapper.updateDept(args);
	}

	// 교구코드 삭제
	@Override
	public int deleteDept(Map<String, Object> args) throws Exception {
		return deptMapper.deleteDept(args);
	}

	// 교구코드 다중삭제
	@Override
	public int deleteDeptMulti(List<Map<String, Object>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, Object> arg = (Map<String, Object>)list.get(i);
			deptMapper.deleteDept(arg);
		}

		return 1;
	}

	
	// 교구구역개수 입력
	@Override
	public int insertDeptRegion(Map<String, Object> args)  throws Exception {
		return deptMapper.insertDeptRegion(args);
	}

	// 교구구역개수 수정
//	@Override
//	public int updateDeptRegion(Map<String, Object> args) throws Exception {
//		return deptMapper.updateDeptRegion(args);
//	}

	// 교구구역개수 삭제
	@Override
	public int deleteDeptRegion(Map<String, Object> args) throws Exception {
		return deptMapper.deleteDeptRegion(args);
	}

	// 교구코드 다중삭제
	@Override
	public int deleteDeptRegionMulti(List<Map<String, Object>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, Object> arg = (Map<String, Object>)list.get(i);
			deptMapper.deleteDeptRegion(arg);
		}

		return 1;
	}

}
