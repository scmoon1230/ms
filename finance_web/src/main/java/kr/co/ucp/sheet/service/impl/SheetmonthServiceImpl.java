package kr.co.ucp.sheet.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.sheet.service.SheetmonthMapper;
import kr.co.ucp.sheet.service.SheetmonthService;

@Service("sheetmonthService")
public class SheetmonthServiceImpl implements SheetmonthService {

	@Resource(name="sheetmonthMapper")
	private SheetmonthMapper sheetmonthMapper;

	// 조건검색
	@Override
	public Map<String, String> selectDurationMonth(Map<String, String> args) throws Exception {
		return sheetmonthMapper.selectDurationMonth(args);
	}

	// 조건검색
	@Override
	public Map<String, String> selectDurationSumm(Map<String, String> args) throws Exception {
		return sheetmonthMapper.selectDurationSumm(args);
	}

	// 조건검색
	@Override
	public List<Map<String, String>> selectMoneyMonth(Map<String, String> args) throws Exception {
		return sheetmonthMapper.selectMoneyMonth(args);
	}

	// 입력
	@Override
	public int insertMoney(Map<String, Object> args)  throws Exception {
		return sheetmonthMapper.insertMoney(args);
	}

	// 수정
	@Override
	public int updateMoney(Map<String, Object> args) throws Exception {
		return sheetmonthMapper.updateMoney(args);
	}

	// 삭제
	@Override
	public int deleteMoney(Map<String, String> args) throws Exception {
		return sheetmonthMapper.deleteMoney(args);
	}

	// 다중삭제
	@Override
	public int deleteMoneyMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			sheetmonthMapper.deleteMoney(arg);
		}

		return 1;
	}
	
}
