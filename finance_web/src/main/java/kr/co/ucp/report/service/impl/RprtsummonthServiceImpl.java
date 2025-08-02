package kr.co.ucp.report.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.report.service.RprtsummonthMapper;
import kr.co.ucp.report.service.RprtsummonthService;

@Service("rprtsummonthService")
public class RprtsummonthServiceImpl implements RprtsummonthService {

	@Resource(name="rprtsummonthMapper")
	private RprtsummonthMapper rprtsummonthMapper;

	// 조건검색
	@Override
	public Map<String, String> selectDurationMonth(Map<String, String> args) throws Exception {
		return rprtsummonthMapper.selectDurationMonth(args);
	}

	// 조건검색
	@Override
	public Map<String, String> selectDurationSumm(Map<String, String> args) throws Exception {
		return rprtsummonthMapper.selectDurationSumm(args);
	}

	// 조건검색
	@Override
	public List<Map<String, String>> selectMoneyMonth(Map<String, String> args) throws Exception {
		return rprtsummonthMapper.selectMoneyMonth(args);
	}

	// 조건검색
	@Override
	public List<Map<String, String>> selectMoneySumm(Map<String, String> args) throws Exception {
		return rprtsummonthMapper.selectMoneySumm(args);
	}

	// 합계
	@Override
	public String selectMonthAmnt(Map<String, String> args) throws Exception {
		return rprtsummonthMapper.selectMonthAmnt(args);
	}

	// 합계
	@Override
	public String selectTotalAmnt(Map<String, String> args) throws Exception {
		return rprtsummonthMapper.selectTotalAmnt(args);
	}

	// 입력
	@Override
	public int insertMoney(Map<String, Object> args)  throws Exception {
		return rprtsummonthMapper.insertMoney(args);
	}

	// 수정
	@Override
	public int updateMoney(Map<String, Object> args) throws Exception {
		return rprtsummonthMapper.updateMoney(args);
	}

	// 삭제
	@Override
	public int deleteMoney(Map<String, String> args) throws Exception {
		return rprtsummonthMapper.deleteMoney(args);
	}

	// 다중삭제
	@Override
	public int deleteMoneyMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			rprtsummonthMapper.deleteMoney(arg);
		}

		return 1;
	}
	
}
