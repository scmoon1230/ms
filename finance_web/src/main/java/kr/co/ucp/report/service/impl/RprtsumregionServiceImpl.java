package kr.co.ucp.report.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.report.service.RprtsumregionMapper;
import kr.co.ucp.report.service.RprtsumregionService;

@Service("rprtsumregionService")
public class RprtsumregionServiceImpl implements RprtsumregionService {

	@Resource(name="rprtsumregionMapper")
	private RprtsumregionMapper rprtsumregionMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectDeptRegion(Map<String, String> args) throws Exception {
		return rprtsumregionMapper.selectDeptRegion(args);
	}

	// 조건검색
//	@Override
//	public Map<String, String> selectDurationYear(Map<String, String> args) throws Exception {
//		return rprtsumregionMapper.selectDurationYear(args);
//	}

	// 조건검색
	@Override
	public Map<String, String> selectNextMonth(Map<String, String> args) throws Exception {
		return rprtsumregionMapper.selectNextMonth(args);
	}

	// 조건검색
	@Override
	public List<Map<String, String>> selectMoneyMonth(Map<String, String> args) throws Exception {
		return rprtsumregionMapper.selectMoneyMonth(args);
	}

	// 조건검색
	@Override
	public List<Map<String, String>> selectMoneyYear(Map<String, String> args) throws Exception {
		return rprtsumregionMapper.selectMoneyYear(args);
	}

	// 합계
	@Override
	public String selectMonthAmnt(Map<String, String> args) throws Exception {
		return rprtsumregionMapper.selectMonthAmnt(args);
	}

	// 합계
	@Override
	public String selectTotalAmnt(Map<String, String> args) throws Exception {
		return rprtsumregionMapper.selectTotalAmnt(args);
	}

}
