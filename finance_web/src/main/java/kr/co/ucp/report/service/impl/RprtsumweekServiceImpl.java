package kr.co.ucp.report.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.report.service.RprtsumweekMapper;
import kr.co.ucp.report.service.RprtsumweekService;

@Service("rprtsumweekService")
public class RprtsumweekServiceImpl implements RprtsumweekService {

	@Resource(name="rprtsumweekMapper")
	private RprtsumweekMapper rprtsumweekMapper;

	// 조건검색
	@Override
	public Map<String, String> selectDurationLastWeek(Map<String, String> args) throws Exception {
		return rprtsumweekMapper.selectDurationLastWeek(args);
	}

	// 조건검색
	@Override
	public Map<String, String> selectDurationThisWeek(Map<String, String> args) throws Exception {
		return rprtsumweekMapper.selectDurationThisWeek(args);
	}

	// 조건검색
	@Override
	public Map<String, String> selectDurationSumm(Map<String, String> args) throws Exception {
		return rprtsumweekMapper.selectDurationSumm(args);
	}

	
	
	// 조건검색
	@Override
	public List<Map<String, String>> selectMoneyLastWeek(Map<String, String> args) throws Exception {
		return rprtsumweekMapper.selectMoneyLastWeek(args);
	}
	// 조건검색
	@Override
	public List<Map<String, String>> selectMoneyThisWeek(Map<String, String> args) throws Exception {
		return rprtsumweekMapper.selectMoneyThisWeek(args);
	}
	// 조건검색
	@Override
	public List<Map<String, String>> selectMoneySumm(Map<String, String> args) throws Exception {
		return rprtsumweekMapper.selectMoneySumm(args);
	}

	
	
	// 합계
	@Override
	public String selectLastWeekAmnt(Map<String, String> args) throws Exception {
		return rprtsumweekMapper.selectLastWeekAmnt(args);
	}
	// 합계
	@Override
	public String selectThisWeekAmnt(Map<String, String> args) throws Exception {
		return rprtsumweekMapper.selectThisWeekAmnt(args);
	}
	// 합계
	@Override
	public String selectTotalAmnt(Map<String, String> args) throws Exception {
		return rprtsumweekMapper.selectTotalAmnt(args);
	}

}
