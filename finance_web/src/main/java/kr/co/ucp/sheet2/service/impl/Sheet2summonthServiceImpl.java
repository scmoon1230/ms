package kr.co.ucp.sheet2.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.sheet2.service.Sheet2summonthMapper;
import kr.co.ucp.sheet2.service.Sheet2summonthService;

@Service("sheet2summonthService")
public class Sheet2summonthServiceImpl implements Sheet2summonthService {

	@Resource(name="sheet2summonthMapper")
	private Sheet2summonthMapper sheet2summonthMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectSheet2Summonth(Map<String, String> args) throws Exception {
		return sheet2summonthMapper.selectSheet2Summonth(args);
	}

	// 합계
	@Override
	public List<Map<String, String>> selectTotalAmnt(Map<String, String> args) throws Exception {
		return sheet2summonthMapper.selectTotalAmnt(args);
	}

	// 입력
	@Override
	public int insertSheet(Map<String, Object> args)  throws Exception {
		return sheet2summonthMapper.insertSheet(args);
	}

	// 수정
	@Override
	public int updateSheet(Map<String, Object> args) throws Exception {
		return sheet2summonthMapper.updateSheet(args);
	}

	// 삭제
	@Override
	public int deleteSheet(Map<String, String> args) throws Exception {
		return sheet2summonthMapper.deleteSheet(args);
	}

	// 다중삭제
	@Override
	public int deleteSheetMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			sheet2summonthMapper.deleteSheet(arg);
		}

		return 1;
	}
	
}
