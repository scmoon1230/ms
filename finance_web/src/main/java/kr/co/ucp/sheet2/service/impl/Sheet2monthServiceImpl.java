package kr.co.ucp.sheet2.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.sheet2.service.Sheet2monthMapper;
import kr.co.ucp.sheet2.service.Sheet2monthService;

@Service("sheet2monthService")
public class Sheet2monthServiceImpl implements Sheet2monthService {

	@Resource(name="sheet2monthMapper")
	private Sheet2monthMapper sheet2monthMapper;

	// 조건검색
	@Override
	public Map<String, String> selectDurationMonth(Map<String, String> args) throws Exception {
		return sheet2monthMapper.selectDurationMonth(args);
	}

	// 조건검색
	@Override
	public Map<String, String> selectDurationSumm(Map<String, String> args) throws Exception {
		return sheet2monthMapper.selectDurationSumm(args);
	}

	// 조건검색
	@Override
	public List<Map<String, String>> selectMoneyMonth(Map<String, String> args) throws Exception {
		return sheet2monthMapper.selectMoneyMonth(args);
	}

	// 입력
	@Override
	public int insertMoney(Map<String, Object> args)  throws Exception {
		return sheet2monthMapper.insertMoney(args);
	}

	// 수정
	@Override
	public int updateMoney(Map<String, Object> args) throws Exception {
		return sheet2monthMapper.updateMoney(args);
	}

	// 삭제
	@Override
	public int deleteMoney(Map<String, String> args) throws Exception {
		return sheet2monthMapper.deleteMoney(args);
	}

	// 다중삭제
	@Override
	public int deleteMoneyMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			sheet2monthMapper.deleteMoney(arg);
		}

		return 1;
	}
	
}
