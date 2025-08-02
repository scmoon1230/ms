package kr.co.ucp.sheet.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.sheet.service.SheetsumMapper;
import kr.co.ucp.sheet.service.SheetsumService;

@Service("sheetsumService")
public class SheetsumServiceImpl implements SheetsumService {

	@Resource(name="sheetsumMapper")
	private SheetsumMapper sheetsumMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectSheetSum(Map<String, String> args) throws Exception {
		return sheetsumMapper.selectSheetSum(args);
	}

}
