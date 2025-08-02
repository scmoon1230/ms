package kr.co.ucp.sheet.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.sheet.service.SheetsumyearMapper;
import kr.co.ucp.sheet.service.SheetsumyearService;

@Service("sheetsumyearService")
public class SheetsumyearServiceImpl implements SheetsumyearService {

	@Resource(name="sheetsumyearMapper")
	private SheetsumyearMapper sheetsumyearMapper;

	// 조건검색
	@Override
	public Map<String, String> selectDurationYear(Map<String, String> args) throws Exception {
		return sheetsumyearMapper.selectDurationYear(args);
	}

	// 조건검색
	@Override
	public List<Map<String, String>> selectSheetSumyear(Map<String, String> args) throws Exception {
		return sheetsumyearMapper.selectSheetSumyear(args);
	}

	// 합계
	@Override
	public List<Map<String, String>> selectTotalAmnt(Map<String, String> args) throws Exception {
		return sheetsumyearMapper.selectTotalAmnt(args);
	}

}
