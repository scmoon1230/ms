package kr.co.ucp.sheet.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.sheet.service.SheetcashtotalallMapper;
import kr.co.ucp.sheet.service.SheetcashtotalallService;

@Service("sheetcashtotalallService")
public class SheetcashtotalallServiceImpl implements SheetcashtotalallService {

	@Resource(name="sheetcashtotalallMapper")
	private SheetcashtotalallMapper sheetcashtotalallMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectSheetCashtotalall(Map<String, String> args) throws Exception {
		return sheetcashtotalallMapper.selectSheetCashtotalall(args);
	}

	// 입력
	@Override
	public int insertSheet(Map<String, Object> args)  throws Exception {
		return sheetcashtotalallMapper.insertSheet(args);
	}

	// 수정
	@Override
	public int updateSheet(Map<String, Object> args) throws Exception {
		return sheetcashtotalallMapper.updateSheet(args);
	}

	// 삭제
	@Override
	public int deleteSheet(Map<String, String> args) throws Exception {
		return sheetcashtotalallMapper.deleteSheet(args);
	}

	// 다중삭제
	@Override
	public int deleteSheetMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			sheetcashtotalallMapper.deleteSheet(arg);
		}

		return 1;
	}
	
}
