package kr.co.ucp.money.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.money.service.MoneyregionMapper;
import kr.co.ucp.money.service.MoneyregionService;

@Service("moneyregionService")
public class MoneyregionServiceImpl implements MoneyregionService {

	@Resource(name="moneyregionMapper")
	private MoneyregionMapper moneyregionMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectMoneyRegion(Map<String, String> args) throws Exception {
		return moneyregionMapper.selectMoneyRegion(args);
	}

	// 합계
	@Override
	public String selectTotalAmnt(Map<String, String> args) throws Exception {
		return moneyregionMapper.selectTotalAmnt(args);
	}

	// 입력
	@Override
	public int insertMoney(Map<String, Object> args)  throws Exception {
		return moneyregionMapper.insertMoney(args);
	}

	// 수정
	@Override
	public int updateMoney(Map<String, Object> args) throws Exception {
		return moneyregionMapper.updateMoney(args);
	}

	// 삭제
	@Override
	public int deleteMoneyRegion(Map<String, String> args) throws Exception {
		return moneyregionMapper.deleteMoneyRegion(args);
	}

	// 다중삭제
	@Override
	public int deleteMoneyRegionMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			moneyregionMapper.deleteMoneyRegion(arg);
		}

		return 1;
	}
	
}
