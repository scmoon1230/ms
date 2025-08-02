package kr.co.ucp.fin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.fin.service.FinmngMapper;
import kr.co.ucp.fin.service.FinchangeMapper;
import kr.co.ucp.fin.service.FinchangeService;

@Service("finchangeService")
public class FinchangeServiceImpl implements FinchangeService {

	@Resource(name="finchangeMapper")
	private FinchangeMapper finchangeMapper;

	@Resource(name="finmngMapper")
	private FinmngMapper finmngMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectFinchange(Map<String, String> args) throws Exception {
		return finchangeMapper.selectFinchange(args);
	}

}
