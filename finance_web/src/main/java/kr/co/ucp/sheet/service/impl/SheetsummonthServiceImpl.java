package kr.co.ucp.sheet.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.sheet.service.SheetsummonthMapper;
import kr.co.ucp.sheet.service.SheetsummonthService;

@Service("sheetsummonthService")
public class SheetsummonthServiceImpl implements SheetsummonthService {

	@Resource(name="sheetsummonthMapper")
	private SheetsummonthMapper sheetsummonthMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectSheetSummonth(Map<String, String> args) throws Exception {
		return sheetsummonthMapper.selectSheetSummonth(args);
	}

	// 합계
	@Override
	public List<Map<String, String>> selectTotalAmnt(Map<String, String> args) throws Exception {
		return sheetsummonthMapper.selectTotalAmnt(args);
	}

}
