package kr.co.ucp.sheet.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.sheet.service.SheetinvenMapper;
import kr.co.ucp.sheet.service.SheetinvenService;

@Service("sheetinvenService")
public class SheetinvenServiceImpl implements SheetinvenService {

	@Resource(name="sheetinvenMapper")
	private SheetinvenMapper sheetinvenMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectSheetInven(Map<String, String> args) throws Exception {
		return sheetinvenMapper.selectSheetInven(args);
	}

	// 합계
	@Override
	public String selectSheetInvenTotalAmnt(Map<String, String> args) throws Exception {
		return sheetinvenMapper.selectSheetInvenTotalAmnt(args);
	}

}
