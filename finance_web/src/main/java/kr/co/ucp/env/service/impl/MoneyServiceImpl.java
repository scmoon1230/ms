package kr.co.ucp.env.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.env.service.MoneyMapper;
import kr.co.ucp.env.service.MoneyService;

@Service("moneyService")
public class MoneyServiceImpl implements MoneyService {

	@Resource(name="moneyMapper")
	private MoneyMapper moneyMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectMoney(Map<String, String> args) throws Exception {
		return moneyMapper.selectMoney(args);
	}

	// 입력
	@Override
	public int insertMoney(Map<String, Object> args)  throws Exception {
		return moneyMapper.insertMoney(args);
	}

	// 수정
	@Override
	public int updateMoney(Map<String, Object> args) throws Exception {
		return moneyMapper.updateMoney(args);
	}

	// 삭제
	@Override
	public int deleteMoney(Map<String, String> args) throws Exception {
		return moneyMapper.deleteMoney(args);
	}

	// 다중삭제
	@Override
	public int deleteMoneyMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			moneyMapper.deleteMoney(arg);
		}

		return 1;
	}

}
