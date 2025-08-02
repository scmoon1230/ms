package kr.co.ucp.fin.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.fin.service.FinmastMapper;
import kr.co.ucp.fin.service.FinmastService;

@Service("finmastService")
public class FinmastServiceImpl implements FinmastService {

	@Resource(name="finmastMapper")
	private FinmastMapper finmastMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectFinmastList(Map<String, String> args) throws Exception {
		return finmastMapper.selectFinmastList(args);
	}

}
