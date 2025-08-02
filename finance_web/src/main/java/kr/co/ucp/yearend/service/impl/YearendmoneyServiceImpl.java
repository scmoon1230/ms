package kr.co.ucp.yearend.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.yearend.service.YearendmoneyMapper;
import kr.co.ucp.yearend.service.YearendmoneyService;

@Service("yearendmoneyService")
public class YearendmoneyServiceImpl implements YearendmoneyService {

	@Resource(name="yearendmoneyMapper")
	private YearendmoneyMapper yearendmoneyMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectMoney(Map<String, String> args) throws Exception {
		return yearendmoneyMapper.selectMoney(args);
	}

}
