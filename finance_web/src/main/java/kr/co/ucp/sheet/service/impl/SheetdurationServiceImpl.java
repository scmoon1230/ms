package kr.co.ucp.sheet.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.sheet.service.SheetdurationMapper;
import kr.co.ucp.sheet.service.SheetdurationService;

@Service("sheetdurationService")
public class SheetdurationServiceImpl implements SheetdurationService {

	@Resource(name="sheetdurationMapper")
	private SheetdurationMapper sheetdurationMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectSheetDuration(Map<String, String> args) throws Exception {
		return sheetdurationMapper.selectSheetDuration(args);
	}

	// 합계
	@Override
	public String selectSheetDurationTotalAmnt(Map<String, String> args) throws Exception {
		return sheetdurationMapper.selectSheetDurationTotalAmnt(args);
	}

}
