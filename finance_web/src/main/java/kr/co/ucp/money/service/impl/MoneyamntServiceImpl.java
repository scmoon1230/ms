package kr.co.ucp.money.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.money.service.MoneyamntMapper;
import kr.co.ucp.money.service.MoneyamntService;

@Service("moneyamntService")
public class MoneyamntServiceImpl implements MoneyamntService {

	@Resource(name="moneyamntMapper")
	private MoneyamntMapper moneyamntMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectMoney(Map<String, String> args) throws Exception {
		return moneyamntMapper.selectMoney(args);
	}

	// 합계
	@Override
	public String selectTotalAmnt(Map<String, String> args) throws Exception {
		return moneyamntMapper.selectTotalAmnt(args);
	}

	// 입력
	@Override
	public int insertMoney(Map<String, Object> args)  throws Exception {
		return moneyamntMapper.insertMoney(args);
	}

	// 수정
	@Override
	public int updateMoney(Map<String, Object> args) throws Exception {
		return moneyamntMapper.updateMoney(args);
	}

	// 삭제
	@Override
	public int deleteMoney(Map<String, String> args) throws Exception {
		return moneyamntMapper.deleteMoney(args);
	}

	// 다중삭제
	@Override
	public int deleteMoneyMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			moneyamntMapper.deleteMoney(arg);
		}

		return 1;
	}
	
}
