package kr.co.ucp.yearend.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.yearend.service.YearendhometaxMapper;
import kr.co.ucp.yearend.service.YearendhometaxService;

@Service("yearendhometaxService")
public class YearendhometaxServiceImpl implements YearendhometaxService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="yearendhometaxMapper")
	private YearendhometaxMapper yearendhometaxMapper;

	@Override
	public List<Map<String, String>> stanYyList(Map<String, String> args) throws Exception {
		return yearendhometaxMapper.stanYyList(args);
	}

	// 조건검색
	@Override
	public List<Map<String, String>> selectMoney(Map<String, String> args) throws Exception {
		return yearendhometaxMapper.selectMoney(args);
	}

}
