package kr.co.ucp.sheet.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.sheet.service.SheetcashtotalMapper;
import kr.co.ucp.sheet.service.SheetcashtotalService;

@Service("sheetcashtotalService")
public class SheetcashtotalServiceImpl implements SheetcashtotalService {

	@Resource(name="sheetcashtotalMapper")
	private SheetcashtotalMapper sheetcashtotalMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectSheetCashtotal(Map<String, String> args) throws Exception {
		return sheetcashtotalMapper.selectSheetCashtotal(args);
	}

	// 입력
	@Override
	public int insertSheet(Map<String, Object> args)  throws Exception {
		return sheetcashtotalMapper.insertSheet(args);
	}

	// 수정
	@Override
	public int updateSheet(Map<String, Object> args) throws Exception {
		return sheetcashtotalMapper.updateSheet(args);
	}

	// 삭제
	@Override
	public int deleteSheet(Map<String, String> args) throws Exception {
		return sheetcashtotalMapper.deleteSheet(args);
	}

	// 다중삭제
	@Override
	public int deleteSheetMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			sheetcashtotalMapper.deleteSheet(arg);
		}

		return 1;
	}
	
}
