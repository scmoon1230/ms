package kr.co.ucp.report.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.report.service.RprtmemsortMapper;
import kr.co.ucp.report.service.RprtmemsortService;

@Service("rprtmemsortService")
public class RprtmemsortServiceImpl implements RprtmemsortService {

	@Resource(name="rprtmemsortMapper")
	private RprtmemsortMapper rprtmemsortMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectRprtMemsort(Map<String, String> args) throws Exception {
		return rprtmemsortMapper.selectRprtMemsort(args);
	}

	// 합계
	@Override
	public String selectRprtMemsortTotalAmnt(Map<String, String> args) throws Exception {
		return rprtmemsortMapper.selectRprtMemsortTotalAmnt(args);
	}

	
	
	// 조건검색
	@Override
	public List<Map<String, String>> selectRprtMemsorthigh(Map<String, String> args) throws Exception {
		return rprtmemsortMapper.selectRprtMemsorthigh(args);
	}

}
