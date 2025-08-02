package kr.co.ucp.fin.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.fin.service.FinweekMapper;
import kr.co.ucp.fin.service.FinweekService;

@Service("finweekService")
public class FinweekServiceImpl implements FinweekService {

	@Resource(name="finweekMapper")
	private FinweekMapper finweekMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectFinweek(Map<String, String> args) throws Exception {
		return finweekMapper.selectFinweek(args);
	}

}
