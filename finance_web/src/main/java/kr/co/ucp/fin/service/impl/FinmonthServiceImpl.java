package kr.co.ucp.fin.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.fin.service.FinmonthMapper;
import kr.co.ucp.fin.service.FinmonthService;

@Service("finmonthService")
public class FinmonthServiceImpl implements FinmonthService {

	@Resource(name="finmonthMapper")
	private FinmonthMapper finmonthMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectFinmonth(Map<String, String> args) throws Exception {
		return finmonthMapper.selectFinmonth(args);
	}

}
